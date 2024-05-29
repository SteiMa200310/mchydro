package org.aec.hydro.mixin;

import net.minecraft.server.MinecraftServer;
import org.aec.hydro.AECHydro;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class DefaultMixin {
	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadWorld()
		AECHydro.LOGGER.info("MinecrafServer.loadWorld");
	}
}