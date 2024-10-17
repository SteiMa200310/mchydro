package org.aec.hydro.utils;

public class HudDataManager {
    // Example variable for HUD data, like power level
    private static String pipeStatus = "No Energy Flow";

    // Getter for retrieving the current power level
    public static String getPipeStatus() {
        return pipeStatus;
    }

    // Setter for updating the power level, called from the block or block entity
    public static void setPipeStatus(String newPowerStatus) {
        pipeStatus = newPowerStatus;
    }
}
