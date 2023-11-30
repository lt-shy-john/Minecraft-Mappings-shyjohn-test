package org.mtr.mapping.render.vertex;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import org.lwjgl.opengl.GL33;
import org.mtr.mapping.holder.Matrix4f;
import org.mtr.mapping.holder.OverlayTexture;
import org.mtr.mapping.holder.Vector3f;
import org.mtr.mapping.render.tool.GlStateTracker;
import org.mtr.mapping.render.tool.Utilities;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;

public final class VertexAttributeState {

	private final Integer color;
	private final Integer lightmapUV;
	private final Vector3f position;
	private final Float textureU, textureV;
	private final Integer overlayUV;
	private final Vector3f normal;
	private final Matrix4f matrix4f;

	public VertexAttributeState(int lightmapUV, Matrix4f matrix4f) {
		color = 0xFFFFFFFF;
		this.lightmapUV = Utilities.exchangeLightmapUVBits(lightmapUV);
		position = null;
		textureU = null;
		textureV = null;
		overlayUV = Utilities.exchangeLightmapUVBits(OverlayTexture.getDefaultUvMapped());
		normal = null;
		this.matrix4f = matrix4f;
	}

	public VertexAttributeState(int lightmapUV) {
		color = null;
		this.lightmapUV = lightmapUV;
		position = null;
		textureU = null;
		textureV = null;
		overlayUV = null;
		normal = null;
		matrix4f = null;
	}

	public VertexAttributeState() {
		color = null;
		lightmapUV = null;
		position = null;
		textureU = null;
		textureV = null;
		overlayUV = null;
		normal = null;
		matrix4f = null;
	}

	public void apply() {
		for (final VertexAttributeType vertexAttributeType : VertexAttributeType.values()) {
			switch (vertexAttributeType) {
				case POSITION:
					if (position == null) {
						continue;
					}
					GL33.glVertexAttrib3f(vertexAttributeType.location, position.getX(), position.getY(), position.getZ());
					break;
				case COLOR:
					if (color == null) {
						continue;
					}
					GL33.glVertexAttrib4f(vertexAttributeType.location, ((color >>> 24) & 0xFF) / 255f, ((color >>> 16) & 0xFF) / 255f, ((color >>> 8) & 0xFF) / 255f, (color & 0xFF) / 255f);
					break;
				case UV_TEXTURE:
					if (textureU == null || textureV == null) {
						continue;
					}
					GL33.glVertexAttrib2f(vertexAttributeType.location, textureU, textureV);
					break;
				case UV_OVERLAY:
					if (overlayUV == null) {
						continue;
					}
					if (!GlStateTracker.isGl4ES()) {
						GL33.glVertexAttribI2i(vertexAttributeType.location, (short) (overlayUV >>> 16), (short) (int) overlayUV);
					} else {
						// GL4ES doesn't have binding for Attrib*i
						GL33.glVertexAttrib2f(vertexAttributeType.location, (short) (overlayUV >>> 16), (short) (int) overlayUV);
					}
					break;
				case UV_LIGHTMAP:
					if (lightmapUV == null) {
						continue;
					}
					if (!GlStateTracker.isGl4ES()) {
						GL33.glVertexAttribI2i(vertexAttributeType.location, (short) (lightmapUV >>> 16), (short) (int) lightmapUV);
					} else {
						// GL4ES doesn't have binding for Attrib*i
						GL33.glVertexAttrib2f(vertexAttributeType.location, (short) (lightmapUV >>> 16), (short) (int) lightmapUV);
					}
					break;
				case NORMAL:
					if (normal == null) {
						continue;
					}
					GL33.glVertexAttrib3f(vertexAttributeType.location, normal.getX(), normal.getY(), normal.getZ());
					break;
				case MATRIX_MODEL:
					if (matrix4f == null) {
						continue;
					}
					if (Utilities.canUseCustomShader()) {
						final FloatBuffer floatBuffer = ByteBuffer.allocate(64).asFloatBuffer();
						Utilities.store(matrix4f, floatBuffer);
						GL33.glVertexAttrib4f(vertexAttributeType.location, floatBuffer.get(0), floatBuffer.get(1), floatBuffer.get(2), floatBuffer.get(3));
						GL33.glVertexAttrib4f(vertexAttributeType.location + 1, floatBuffer.get(4), floatBuffer.get(5), floatBuffer.get(6), floatBuffer.get(7));
						GL33.glVertexAttrib4f(vertexAttributeType.location + 2, floatBuffer.get(8), floatBuffer.get(9), floatBuffer.get(10), floatBuffer.get(11));
						GL33.glVertexAttrib4f(vertexAttributeType.location + 3, floatBuffer.get(12), floatBuffer.get(13), floatBuffer.get(14), floatBuffer.get(15));
					} else {
						final ShaderProgram shaderProgram = RenderSystem.getShader();
						if (shaderProgram != null && shaderProgram.modelViewMat != null) {
							shaderProgram.modelViewMat.set(matrix4f.data);
							shaderProgram.bind();
						}
					}
					break;
			}
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof VertexAttributeState)) {
			return false;
		}
		final VertexAttributeState vertexAttributeState = (VertexAttributeState) object;
		return Objects.equals(color, vertexAttributeState.color) &&
				Objects.equals(lightmapUV, vertexAttributeState.lightmapUV) &&
				Objects.equals(position, vertexAttributeState.position) &&
				Objects.equals(textureU, vertexAttributeState.textureU) &&
				Objects.equals(textureV, vertexAttributeState.textureV) &&
				Objects.equals(overlayUV, vertexAttributeState.overlayUV) &&
				Objects.equals(normal, vertexAttributeState.normal) &&
				Objects.equals(matrix4f, vertexAttributeState.matrix4f);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, color, textureU, textureV, lightmapUV, normal, overlayUV, matrix4f);
	}
}
