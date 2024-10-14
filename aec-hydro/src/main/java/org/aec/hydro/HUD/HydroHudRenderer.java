package org.aec.hydro.HUD;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.aec.hydro.item.ModItemGroups;

public class HydroHudRenderer implements ClientModInitializer {

    // Example: Let's assume this is the power level of your block (you can change it dynamically)
    private int powerLevel = 100;

    @Override
    public void onInitializeClient() {
        // Register the HUD render callback
        HudRenderCallback.EVENT.register(this::onHudRender);
    }

    private void onHudRender(DrawContext drawContext, float v) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null && client.player != null) {
            // The text to display (you can customize this)
            String displayText = "Power Level: " + powerLevel;

            // Draw the text in the top left corner (with a small margin of 10 pixels)
            drawContext.drawTextWithShadow(client.textRenderer, displayText, 10, 10, 0xFFFFFF);
        }
    }

    // You can add a method to update the power level dynamically, for example:
    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }
}
