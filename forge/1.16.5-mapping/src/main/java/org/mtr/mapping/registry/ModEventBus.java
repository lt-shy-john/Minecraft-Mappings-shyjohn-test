package org.mtr.mapping.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.holder.SoundEvent;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ModEventBus {

	static final List<Supplier<Block>> BLOCKS = new ArrayList<>();
	static final List<Supplier<BlockItemExtension>> BLOCK_ITEMS = new ArrayList<>();
	static final List<Supplier<Item>> ITEMS = new ArrayList<>();
	static final List<Supplier<TileEntityType<? extends BlockEntityExtension>>> BLOCK_ENTITY_TYPES = new ArrayList<>();
	static final List<Supplier<EntityType<? extends EntityExtension>>> ENTITY_TYPES = new ArrayList<>();
	static final List<Supplier<SoundEvent>> SOUND_EVENTS = new ArrayList<>();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<net.minecraft.block.Block> event) {
		BLOCKS.forEach(supplier -> event.getRegistry().register(supplier.get().data));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<net.minecraft.item.Item> event) {
		BLOCK_ITEMS.forEach(supplier -> event.getRegistry().register(supplier.get()));
		ITEMS.forEach(supplier -> event.getRegistry().register(supplier.get().data));
	}

	@SubscribeEvent
	public static void registerBlockEntityTypes(RegistryEvent.Register<TileEntityType<?>> event) {
		BLOCK_ENTITY_TYPES.forEach(supplier -> event.getRegistry().register(supplier.get()));
	}

	@SubscribeEvent
	public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
		ENTITY_TYPES.forEach(supplier -> event.getRegistry().register(supplier.get()));
	}

	@SubscribeEvent
	public static void registerSoundEvents(RegistryEvent.Register<net.minecraft.util.SoundEvent> event) {
		SOUND_EVENTS.forEach(supplier -> event.getRegistry().register(supplier.get().data));
	}
}
