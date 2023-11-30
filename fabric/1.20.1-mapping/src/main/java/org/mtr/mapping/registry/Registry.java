package org.mtr.mapping.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mapping.tool.HolderBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Registry extends DummyClass {

	static Identifier packetsIdentifier;
	static final Map<String, Function<PacketBuffer, ? extends PacketHandler>> PACKETS = new HashMap<>();
	private static final List<Runnable> OBJECTS_TO_REGISTER = new ArrayList<>();

	@MappedMethod
	public static void init() {
		OBJECTS_TO_REGISTER.forEach(Runnable::run);
	}

	@MappedMethod
	public static BlockRegistryObject registerBlock(Identifier identifier, Supplier<Block> supplier) {
		final Block block = supplier.get();
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.BLOCK, identifier.data, block.data));
		return new BlockRegistryObject(block);
	}

	@MappedMethod
	public static BlockRegistryObject registerBlockWithBlockItem(Identifier identifier, Supplier<Block> supplier, CreativeModeTabHolder creativeModeTabHolder) {
		final Block block = supplier.get();
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.BLOCK, identifier.data, block.data));
		final BlockItemExtension blockItemExtension = new BlockItemExtension(block, new ItemSettings());
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.ITEM, identifier.data, blockItemExtension));
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, creativeModeTabHolder.identifier)).register(content -> content.add(blockItemExtension));
		return new BlockRegistryObject(block);
	}

	@MappedMethod
	public static ItemRegistryObject registerItem(Identifier identifier, Function<ItemSettings, Item> function, CreativeModeTabHolder creativeModeTabHolder) {
		final Item item = function.apply(new ItemSettings());
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.ITEM, identifier.data, item.data));
		ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, creativeModeTabHolder.identifier)).register(content -> content.add(item.data));
		return new ItemRegistryObject(item);
	}

	@MappedMethod
	public static <T extends BlockEntityExtension> BlockEntityTypeRegistryObject<T> registerBlockEntityType(Identifier identifier, BiFunction<BlockPos, BlockState, T> function, Supplier<Block>... blockSuppliers) {
		final net.minecraft.block.entity.BlockEntityType<T> blockEntityType = FabricBlockEntityTypeBuilder.create((pos, state) -> function.apply(new BlockPos(pos), new BlockState(state)), HolderBase.convertArray(blockSuppliers, net.minecraft.block.Block[]::new)).build(null);
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.BLOCK_ENTITY_TYPE, identifier.data, blockEntityType));
		return new BlockEntityTypeRegistryObject<>(new BlockEntityType<>(blockEntityType));
	}

	@MappedMethod
	public static <T extends EntityExtension> EntityTypeRegistryObject<T> registerEntityType(Identifier identifier, BiFunction<EntityType<?>, World, T> function, float width, float height) {
		final net.minecraft.entity.EntityType<T> entityType = FabricEntityTypeBuilder.create(SpawnGroup.MISC, getEntityFactory(function)).dimensions(EntityDimensions.fixed(width, height)).build();
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.ENTITY_TYPE, identifier.data, entityType));
		return new EntityTypeRegistryObject<>(new EntityType<>(entityType));
	}

	private static <T extends EntityExtension> net.minecraft.entity.EntityType.EntityFactory<T> getEntityFactory(BiFunction<EntityType<?>, World, T> function) {
		return (entityType, world) -> function.apply(new EntityType<>(entityType), new World(world));
	}

	@MappedMethod
	public static CreativeModeTabHolder createCreativeModeTabHolder(Identifier identifier, Supplier<ItemStack> iconSupplier) {
		final ItemGroup itemGroup = FabricItemGroup.builder().icon(() -> iconSupplier.get().data).displayName(Text.translatable(String.format("itemGroup.%s.%s", identifier.data.getNamespace(), identifier.data.getPath()))).build();
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.ITEM_GROUP, identifier.data, itemGroup));
		return new CreativeModeTabHolder(itemGroup, identifier.data);
	}

	@MappedMethod
	public static SoundEventRegistryObject registerSoundEvent(Identifier identifier) {
		final SoundEvent soundEvent = SoundEvent.of(identifier);
		OBJECTS_TO_REGISTER.add(() -> net.minecraft.registry.Registry.register(Registries.SOUND_EVENT, identifier.data, soundEvent.data));
		return new SoundEventRegistryObject(soundEvent);
	}

	@MappedMethod
	public static void setupPackets(Identifier identifier) {
		packetsIdentifier = identifier;
		ServerPlayNetworking.registerGlobalReceiver(identifier.data, (server, player, handler, buf, responseSender) -> {
			final Function<PacketBuffer, ? extends PacketHandler> getInstance = PACKETS.get(buf.readString());
			if (getInstance != null) {
				final PacketHandler packetHandler = getInstance.apply(new PacketBuffer(buf));
				packetHandler.runServer();
				server.execute(() -> packetHandler.runServerQueued(new MinecraftServer(server), new ServerPlayerEntity(player)));
			}
		});
	}

	@MappedMethod
	public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBuffer, T> getInstance) {
		PACKETS.put(classObject.getName(), getInstance);
	}

	@MappedMethod
	public static <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
		if (packetsIdentifier != null) {
			final PacketByteBuf packetByteBuf = PacketByteBufs.create();
			packetByteBuf.writeString(data.getClass().getName());
			data.write(new PacketBuffer(packetByteBuf));
			ServerPlayNetworking.send(serverPlayerEntity.data, packetsIdentifier.data, packetByteBuf);
		}
	}
}
