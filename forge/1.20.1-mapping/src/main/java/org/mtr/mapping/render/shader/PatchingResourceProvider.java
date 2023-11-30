package org.mtr.mapping.render.shader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.apache.commons.io.IOUtils;
import org.mtr.mapping.holder.ResourceManager;
import org.mtr.mapping.render.tool.GlStateTracker;
import org.mtr.mapping.tool.DummyClass;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class PatchingResourceProvider implements ResourceProvider {

	private final ResourceProvider resourceFactory;

	public PatchingResourceProvider(ResourceManager resourceManager) {
		resourceFactory = resourceManager.data;
	}

	@Override
//#if MC_VERSION >= "11900"
	public Optional<Resource> getResource(ResourceLocation identifier) {
//#else
//    public Resource getResource(ResourceLocation resourceLocation) throws IOException {
//#endif
		final ResourceLocation newIdentifier = identifier.getPath().contains("_modelmat") ? new ResourceLocation(identifier.getNamespace(), identifier.getPath().replace("_modelmat", "")) : identifier;
		final Optional<Resource> resource = resourceFactory.getResource(newIdentifier);

		if (resource.isEmpty()) {
			return Optional.empty();
		} else {
			try (final InputStream inputStream = resource.get().open()) {
				final String returningContent;

				if (newIdentifier.getPath().endsWith(".json")) {
					final JsonObject dataObject = JsonParser.parseString(IOUtils.toString(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
					dataObject.addProperty("vertex", dataObject.get("vertex").getAsString() + "_modelmat");
					final JsonArray attributeArray = dataObject.get("attributes").getAsJsonArray();
					for (int i = 0; i < 6 - attributeArray.size(); i++) {
						attributeArray.add("Dummy" + i);
					}
					attributeArray.add("ModelMat");
					returningContent = dataObject.toString();
				} else if (newIdentifier.getPath().endsWith(".vsh")) {
					returningContent = patchVertexShaderSource(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
				} else {
					return resource;
				}

//#if MC_VERSION >= "11903"
				return Optional.of(new Resource(resource.get().source(), () -> new ByteArrayInputStream(returningContent.getBytes(StandardCharsets.UTF_8))));
//#elif MC_VERSION >= "11900"
//            return Optional.of(new Resource(srcResource.get().sourcePackId(), () -> newContentStream));
//#else
//            return new SimpleResource(srcResource.getSourceName(), resourceLocation, newContentStream, null);
//#endif
			} catch (Exception e) {
				DummyClass.logException(e);
				return Optional.empty();
			}
		}
//#else
//            Resource srcResource = source.getResource(resourceLocation);
//            srcInputStream = srcResource.getInputStream();
//#endif
	}

	private static String patchVertexShaderSource(String sourceContent) {
		final String[] contentParts = sourceContent.split("void main");
		contentParts[0] = contentParts[0].replace("uniform mat4 ModelViewMat;", "uniform mat4 ModelViewMat;\nin mat4 ModelMat;");
		if (GlStateTracker.isGl4ES()) {
			contentParts[0] = contentParts[0].replace("ivec2", "vec2");
		}
		contentParts[1] = contentParts[1]
				.replaceAll("\\bPosition\\b", "(MODELVIEWMAT * ModelMat * vec4(Position, 1.0)).xyz")
				.replaceAll("\\bNormal\\b", "normalize(mat3(MODELVIEWMAT * ModelMat) * Normal)")
				.replace("ModelViewMat", "mat4(1.0)")
				.replace("MODELVIEWMAT", "ModelViewMat");
		return contentParts[0] + "void main" + contentParts[1];
	}
}
