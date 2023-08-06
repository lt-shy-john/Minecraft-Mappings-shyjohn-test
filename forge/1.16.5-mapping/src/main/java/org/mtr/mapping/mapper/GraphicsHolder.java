package org.mtr.mapping.mapper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.ColorHelper;
import org.mtr.mapping.tool.DummyClass;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public final class GraphicsHolder extends DummyClass {

	private int matrixPushes;

	final MatrixStack matrixStack;
	final IRenderTypeBuffer vertexConsumerProvider;
	private final IRenderTypeBuffer.Impl immediate;

	public static final int DEFAULT_LIGHT = 0xF000F0;

	public GraphicsHolder(@Nullable MatrixStack matrixStack, @Nullable IRenderTypeBuffer vertexConsumerProvider) {
		this.matrixStack = matrixStack;
		this.vertexConsumerProvider = vertexConsumerProvider;
		immediate = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
		push();
	}

	@MappedMethod
	public void push() {
		if (matrixStack != null) {
			matrixStack.pushPose();
			matrixPushes++;
		}
	}

	@MappedMethod
	public void pop() {
		if (matrixStack != null && matrixPushes > 0) {
			matrixStack.popPose();
			matrixPushes--;
		}
	}

	@MappedMethod
	public void popAll() {
		while (matrixPushes > 0) {
			pop();
		}
	}

	@MappedMethod
	public void translate(double x, double y, double z) {
		if (matrixStack != null) {
			matrixStack.translate(x, y, z);
		}
	}

	@MappedMethod
	public void scale(float x, float y, float z) {
		if (matrixStack != null) {
			matrixStack.scale(x, y, z);
		}
	}

	@MappedMethod
	public void rotateXRadians(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.XP.rotation(angle));
		}
	}

	@MappedMethod
	public void rotateYRadians(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.YP.rotation(angle));
		}
	}

	@MappedMethod
	public void rotateZRadians(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.ZP.rotation(angle));
		}
	}

	@MappedMethod
	public void rotateXDegrees(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.XP.rotationDegrees(angle));
		}
	}

	@MappedMethod
	public void rotateYDegrees(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
		}
	}

	@MappedMethod
	public void rotateZDegrees(float angle) {
		if (matrixStack != null) {
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
		}
	}

	@MappedMethod
	public void drawText(MutableText mutableText, int x, int y, int color, boolean shadow, int light) {
		if (matrixStack != null && immediate != null) {
			getInstance().font.drawInBatch(mutableText.data, x, y, color, shadow, matrixStack.last().pose(), immediate, false, 0, light);
		}
	}

	@MappedMethod
	public void drawText(OrderedText orderedText, int x, int y, int color, boolean shadow, int light) {
		if (matrixStack != null && immediate != null) {
			getInstance().font.drawInBatch(orderedText.data, x, y, color, shadow, matrixStack.last().pose(), immediate, false, 0, light);
		}
	}

	@MappedMethod
	public void drawText(String text, int x, int y, int color, boolean shadow, int light) {
		if (matrixStack != null && immediate != null) {
			getInstance().font.drawInBatch(text, x, y, color, shadow, matrixStack.last().pose(), immediate, false, 0, light);
		}
	}

	@MappedMethod
	public static int getTextWidth(MutableText mutableText) {
		return getInstance().font.width(mutableText.data);
	}

	@MappedMethod
	public static int getTextWidth(OrderedText orderedText) {
		return getInstance().font.width(orderedText.data);
	}

	@MappedMethod
	public static int getTextWidth(String text) {
		return getInstance().font.width(text);
	}

	@MappedMethod
	public static List<OrderedText> wrapLines(MutableText mutableText, int width) {
		return getInstance().font.split(mutableText.data, width).stream().map(OrderedText::new).collect(Collectors.toList());
	}

	private static Minecraft getInstance() {
		return Minecraft.getInstance();
	}

	@MappedMethod
	public void drawImmediate() {
		if (immediate != null) {
			immediate.endBatch();
		}
	}

	@MappedMethod
	public void drawLine(float x1, float y1, float z1, float x2, float y2, float z2, int color) {
		if (matrixStack != null) {
			ColorHelper.unpackColor(color, (a, r, g, b) -> {
				final IVertexBuilder vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.LINES);

				final MatrixStack.Entry entry = matrixStack.last();
				final Matrix4f matrix4f = entry.pose();
				final Matrix3f matrix3f = entry.normal();

				vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).normal(matrix3f, 0, 1, 0).endVertex();
				vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).normal(matrix3f, 0, 1, 0).endVertex();
			});
		}
	}

	@MappedMethod
	public static void drawRectangle(BufferBuilder bufferBuilder, double x1, double y1, double x2, double y2, int color) {
		ColorHelper.unpackColor(color, (a, r, g, b) -> {
			bufferBuilder.data.vertex(x1, y1, 0).color(r, g, b, a).endVertex();
			bufferBuilder.data.vertex(x1, y2, 0).color(r, g, b, a).endVertex();
			bufferBuilder.data.vertex(x2, y2, 0).color(r, g, b, a).endVertex();
			bufferBuilder.data.vertex(x2, y1, 0).color(r, g, b, a).endVertex();
		});
	}

	@MappedMethod
	public void drawTexture(RenderLayer renderLayer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2, Direction facing, int color, int light) {
		if (matrixStack != null) {
			ColorHelper.unpackColor(color, (a, r, g, b) -> {
				final IVertexBuilder vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer.data);

				final Vector3i vector3i = facing.getVector();
				final int x = vector3i.getX();
				final int y = vector3i.getY();
				final int z = vector3i.getZ();

				final MatrixStack.Entry entry = matrixStack.last();
				final Matrix4f matrix4f = entry.pose();
				final Matrix3f matrix3f = entry.normal();

				vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).uv(u1, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, x, y, z).endVertex();
				vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, x, y, z).endVertex();
				vertexConsumer.vertex(matrix4f, x3, y3, z3).color(r, g, b, a).uv(u2, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, x, y, z).endVertex();
				vertexConsumer.vertex(matrix4f, x4, y4, z4).color(r, g, b, a).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, x, y, z).endVertex();
			});
		}
	}
}
