package org.aec.hydro.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.aec.hydro.AECHydro;

public class _HydroSounds {
    public static final SoundEvent WINDMILL_LOOP = registerSoundEvent("windmill_loop");
    public static final SoundEvent SOLAR_LOOP = registerSoundEvent("solar_panel");

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(AECHydro.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void registerSounds(){
        AECHydro.LOGGER.info("Registering Sounds for " + AECHydro.MOD_ID);
    }
}
