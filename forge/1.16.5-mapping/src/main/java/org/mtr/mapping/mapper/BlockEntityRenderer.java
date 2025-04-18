package org.mtr.mapping.mapper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.mtr.mapping.annotation.MappedMethod;

public abstract class BlockEntityRenderer<T extends BlockEntityExtension> extends TileEntityRenderer<T> {

	@MappedMethod
	public BlockEntityRenderer(Argument argument) {
		super(argument.data);
	}

	@Deprecated
	@Override
	public final void render(T entity, float tickDelta, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, int overlay) {
		GraphicsHolder.createInstanceSafe(matrices, vertexConsumers, graphicsHolder -> render(entity, tickDelta, graphicsHolder, light, overlay));
	}

	@MappedMethod
	public abstract void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay);

	@MappedMethod
	public boolean rendersOutsideBoundingBox2(T blockEntity) {
		return super.shouldRenderOffScreen(blockEntity);
	}

	@Deprecated
	@Override
	public final boolean shouldRenderOffScreen(T blockEntity) {
		return rendersOutsideBoundingBox2(blockEntity);
	}

	@Deprecated
	public static final class Argument {

		private final TileEntityRendererDispatcher data;

		public Argument(TileEntityRendererDispatcher data) {
			this.data = data;
		}
	}
}
