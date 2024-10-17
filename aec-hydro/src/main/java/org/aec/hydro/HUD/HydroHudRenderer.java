package org.aec.hydro.HUD;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.item.ModItemGroups;
import org.aec.hydro.utils.HudDataManager;

public class HydroHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta){
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if the client and player exist
        if (client != null && client.player != null) {
            if(client.player.getMainHandStack().getItem() != ModItemGroups.VOLTMETER){
                drawContext.drawTextWithShadow(client.textRenderer, "No Voltmeter equiped", 10, 10, 0xFFFFFF);
                return;
            }
            // Perform raycast to detect the block the player is looking at
            HitResult hitResult = client.crosshairTarget;

            // Check if the player is looking at a block
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = client.world.getBlockState(blockPos);

                // Display block information on the HUD
                String blockInfo = getBlockInfo(blockState);
                drawContext.drawTextWithShadow(client.textRenderer, blockInfo, 10, 10, 0xFFFFFF);
            }
        }
    }

    // Method to get block information (customize this based on what you want to show)
    private String getBlockInfo(BlockState blockState) {
        return "Block: " + blockState.getBlock().getTranslationKey();
    }
}
