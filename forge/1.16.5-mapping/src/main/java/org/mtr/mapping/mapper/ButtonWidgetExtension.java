package org.mtr.mapping.mapper;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.ButtonWidgetAbstractMapping;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Text;

public class ButtonWidgetExtension extends ButtonWidgetAbstractMapping {

	@MappedMethod
	public ButtonWidgetExtension(int x, int y, int width, int height, org.mtr.mapping.holder.PressAction onPress) {
		this(x, y, width, height, "", onPress);
	}

	@MappedMethod
	public ButtonWidgetExtension(int x, int y, int width, int height, String message, org.mtr.mapping.holder.PressAction onPress) {
		this(x, y, width, height, TextHelper.literal(message), onPress);
	}

	@MappedMethod
	public ButtonWidgetExtension(int x, int y, int width, int height, MutableText message, org.mtr.mapping.holder.PressAction onPress) {
		super(x, y, width, height, new Text(message.data), onPress);
	}

	@MappedMethod
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		if (graphicsHolder.matrixStack != null) {
			super.render2(graphicsHolder.matrixStack, mouseX, mouseY, delta);
		}
	}

	@Deprecated
	@Override
	public final void render2(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		render(new GraphicsHolder(matrices, null), mouseX, mouseY, delta);
	}

	@MappedMethod
	public final int getX2() {
		return x;
	}

	@MappedMethod
	public final int getY2() {
		return y;
	}

	@MappedMethod
	public final void setX2(int x) {
		this.x = x;
	}

	@MappedMethod
	public final void setY2(int y) {
		this.y = y;
	}

	@MappedMethod
	public static void drawCenteredText(GraphicsHolder graphicsHolder, String text, int centerX, int y, int color) {
		if (graphicsHolder.matrixStack != null) {
			drawCenteredString(graphicsHolder.matrixStack, Minecraft.getInstance().font, text, centerX, y, color);
		}
	}

	@MappedMethod
	public static void drawCenteredText(GraphicsHolder graphicsHolder, MutableText text, int centerX, int y, int color) {
		if (graphicsHolder.matrixStack != null) {
			drawCenteredString(graphicsHolder.matrixStack, Minecraft.getInstance().font, text.data, centerX, y, color);
		}
	}
}
