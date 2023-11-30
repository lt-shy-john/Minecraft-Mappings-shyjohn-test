package org.mtr.mapping.mapper;

import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.*;

import java.util.function.Consumer;

public abstract class AbstractSoundInstanceExtension extends AbstractSoundInstanceAbstractMapping {

	@MappedMethod
	protected void setIsRelativeMapped(boolean isRelative) {
		relative = isRelative;
	}

	@MappedMethod
	protected void setIsRepeatableMapped(boolean isRepeatable) {
		looping = isRepeatable;
	}

	@MappedMethod
	@Override
	public boolean isRelative2() {
		return super.isRelative2();
	}

	@MappedMethod
	public boolean isRepeatable2() {
		return super.isLooping2();
	}

	@MappedMethod
	protected AbstractSoundInstanceExtension(SoundEvent sound, SoundCategory category) {
		super(sound, category, Random.create());
	}

	@MappedMethod
	protected AbstractSoundInstanceExtension(Identifier soundId, SoundCategory category) {
		super(soundId, category, Random.create());
	}

	@MappedMethod
	public static SoundEvent createSoundEvent(Identifier identifier) {
		return SoundEvent.createVariableRangeEvent(identifier);
	}

	@MappedMethod
	public static void iterateSoundIds(Consumer<Identifier> consumer) {
		MinecraftClient.getInstance().getSoundManager().getAvailableSounds().forEach(identifier -> consumer.accept(new Identifier(identifier)));
	}
}
