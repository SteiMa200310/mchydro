package org.aec.hydro;

import net.fabricmc.api.ClientModInitializer;

public class AECHydroClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AECHydro.LOGGER.info("Client initialization");
    }
}
