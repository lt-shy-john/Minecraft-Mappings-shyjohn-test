package org.mtr.mapping.registry;

import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.tool.DummyClass;

public abstract class PacketHandler extends DummyClass {

	@MappedMethod
	public abstract void write(PacketBuffer packetBuffer);

	@MappedMethod
	public void runServer() {
	}

	@MappedMethod
	public void runClient() {
	}

	@MappedMethod
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
	}

	@MappedMethod
	public void runClientQueued() {
	}

	@MappedMethod
	public static String readString(PacketBuffer packetBuffer) {
		return packetBuffer.data.readString(32767);
	}

	@MappedMethod
	public static void writeString(PacketBuffer packetBuffer, String text) {
		packetBuffer.data.writeString(text, 32767);
	}
}
