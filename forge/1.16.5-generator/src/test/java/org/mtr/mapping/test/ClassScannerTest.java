package org.mtr.mapping.test;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.*;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import org.junit.jupiter.api.Test;

import java.util.Random;

public final class ClassScannerTest {

	@Test
	public void scan() {
		final ClassScannerBase scanner = ClassScannerBase.getInstance();
		scanner.put("ActionResult", ActionResultType.class);
		scanner.put("Axis", Direction.Axis.class);
		scanner.put("BlockEntityRendererArgument", TileEntityRendererDispatcher.class);
		scanner.put("BlockEntityType", TileEntityType.class);
		scanner.put("BlockHitResult", BlockRayTraceResult.class);
		scanner.put("BlockPos", BlockPos.class);
		scanner.put("BlockRenderView", IBlockDisplayReader.class);
		scanner.put("BlockSettings", AbstractBlock.Properties.class);
		scanner.put("BlockState", BlockState.class);
		scanner.put("BlockView", IBlockReader.class);
		scanner.put("BooleanProperty", BooleanProperty.class);
		scanner.put("Box", AxisAlignedBB.class);
		scanner.put("BufferBuilder", BufferBuilder.class);
		scanner.put("ChunkManager", AbstractChunkProvider.class);
		scanner.put("ClientPlayerEntity", ClientPlayerEntity.class);
		scanner.put("ClientWorld", ClientWorld.class);
		scanner.put("CompoundTag", CompoundNBT.class);
		scanner.put("Direction", Direction.class);
		scanner.put("DirectionProperty", DirectionProperty.class);
		scanner.put("EntityType", EntityType.class);
		scanner.put("EnumProperty", EnumProperty.class);
		scanner.put("Explosion", Explosion.class);
		scanner.put("FluidState", FluidState.class);
		scanner.put("Hand", Hand.class);
		scanner.put("HeightMapType", Heightmap.Type.class);
		scanner.put("Identifier", ResourceLocation.class);
		scanner.put("IntegerProperty", IntegerProperty.class);
		scanner.put("ItemConvertible", IItemProvider.class);
		scanner.put("ItemPlacementContext", BlockItemUseContext.class);
		scanner.put("ItemSettings", Item.Properties.class);
		scanner.put("ItemStack", ItemStack.class);
		scanner.put("ItemUsageContext", ItemUseContext.class);
		scanner.put("KeyBinding", KeyBinding.class);
		scanner.put("LivingEntity", LivingEntity.class);
		scanner.put("MathHelper", MathHelper.class);
		scanner.put("MinecraftClient", Minecraft.class, "ask", "askEither");
		scanner.put("MinecraftServer", MinecraftServer.class, "ask", "askEither");
		scanner.put("Mirror", Mirror.class);
		scanner.put("MutableText", IFormattableTextComponent.class);
		scanner.put("OrderedText", IReorderingProcessor.class);
		scanner.put("PacketBuffer", PacketBuffer.class);
		scanner.put("PlayerEntity", PlayerEntity.class);
		scanner.put("Position", IPosition.class);
		scanner.put("Property", Property.class);
		scanner.put("Random", Random.class);
		scanner.put("RenderLayer", RenderType.class);
		scanner.put("Rotation", Rotation.class);
		scanner.put("Scoreboard", Scoreboard.class);
		scanner.put("ScoreboardCriterion", ScoreCriteria.class);
		scanner.put("ScoreboardCriterionRenderType", ScoreCriteria.RenderType.class);
		scanner.put("ScoreboardObjective", ScoreObjective.class);
		scanner.put("ScoreboardPlayerScore", Score.class);
		scanner.put("ServerPlayerEntity", ServerPlayerEntity.class);
		scanner.put("ServerWorld", ServerWorld.class);
		scanner.put("ServerWorldAccess", IServerWorld.class);
		scanner.put("ShapeContext", ISelectionContext.class);
		scanner.put("SoundCategory", SoundCategory.class);
		scanner.put("SoundEvent", SoundEvent.class);
		scanner.put("SoundInstance", ISound.class);
		scanner.put("SoundManager", SoundHandler.class);
		scanner.put("Team", ScorePlayerTeam.class);
		scanner.put("Tessellator", Tessellator.class);
		scanner.put("Text", ITextComponent.class);
		scanner.put("TextFormatting", TextFormatting.class);
		scanner.put("TextRenderer", FontRenderer.class);
		scanner.put("TooltipContext", ITooltipFlag.class);
		scanner.put("Vector3d", Vector3d.class);
		scanner.put("Vector3f", Vector3f.class);
		scanner.put("Vector3i", Vector3i.class);
		scanner.put("VoxelShape", VoxelShape.class);
		scanner.put("VoxelShapes", VoxelShapes.class);
		scanner.put("World", World.class);
		scanner.put("WorldAccess", IWorld.class);
		scanner.put("WorldChunk", Chunk.class);
		scanner.putAbstract("AbstractSoundInstance", LocatableSound.class);
		scanner.putAbstract("Block", Block.class);
		scanner.putAbstract("BlockEntity", TileEntity.class);
		scanner.putAbstract("BlockItem", BlockItem.class);
		scanner.putAbstract("ButtonWidget", Button.class);
		scanner.putAbstract("CheckboxWidget", CheckboxButton.class);
		scanner.putAbstract("ClickableWidget", AbstractButton.class);
		scanner.putAbstract("Entity", Entity.class);
		scanner.putAbstract("Item", Item.class);
		scanner.putAbstract("MovingSoundInstance", TickableSound.class);
		scanner.putAbstract("PlaceableOnWaterItem", LilyPadItem.class);
		scanner.putAbstract("PressableWidget", Widget.class);
		scanner.putAbstract("Screen", Screen.class);
		scanner.putAbstract("SlabBlock", SlabBlock.class);
		scanner.putAbstract("SliderWidget", AbstractSlider.class);
		scanner.putAbstract("TextFieldWidget", TextFieldWidget.class);
		scanner.putAbstract("TexturedButtonWidget", ImageButton.class);
		scanner.putAbstract("ToggleButtonWidget", ToggleWidget.class);
		scanner.putInterface("BlockColorProvider", IBlockColor.class);
		scanner.putInterface("ItemColorProvider", IItemColor.class);
		scanner.putInterface("PressAction", Button.IPressable.class);
		scanner.putInterface("StringIdentifiable", IStringSerializable.class);
		scanner.putInterface("TickableSoundInstance", ITickableSound.class);
		scanner.generate();
	}
}
