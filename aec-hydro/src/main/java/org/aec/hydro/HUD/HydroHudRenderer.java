package org.aec.hydro.HUD;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.item.ModItemGroups;
import org.aec.hydro.pipeHandling.utils.PipeProperties;

public class HydroHudRenderer implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta){
        MinecraftClient client = MinecraftClient.getInstance();

        // Check if the client and player exist
        if (client != null && client.player != null) {
            // Perform raycast to detect the block the player is looking at
            HitResult hitResult = client.crosshairTarget;

            // Check if the player is looking at a block
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                assert client.world != null;
                BlockState blockState = client.world.getBlockState(blockPos);
                Block targetBlock = blockState.getBlock();


                if(client.player.getMainHandStack().getItem() != ModItemGroups.VOLTMETER && client.player.getMainHandStack().getItem() != ModItemGroups.BAROMETER) return;

                if(targetBlock == _HydroBlocks.CABLE && client.player.getMainHandStack().getItem() == ModItemGroups.VOLTMETER)
                    drawContext.drawTextWithShadow(client.textRenderer, Text.translatable("hud.hydro.volt", blockState.get(PipeProperties.PowerLevel)), 10, 10, 0xFFFFFF);

                if (targetBlock == _HydroBlocks.WATERPIPE && client.player.getMainHandStack().getItem() == ModItemGroups.BAROMETER)
                    drawContext.drawTextWithShadow(client.textRenderer, Text.translatable("hud.hydro.pressure", blockState.get(PipeProperties.PowerLevel)), 10, 10, 0xFFFFFF);

                if((targetBlock == _HydroBlocks.HYDROGENPIPE || targetBlock == _HydroBlocks.OXYGENPIPE) && client.player.getMainHandStack().getItem() == ModItemGroups.BAROMETER)
                    drawContext.drawTextWithShadow(client.textRenderer, Text.translatable("hud.hydro.gas", blockState.get(PipeProperties.PowerLevel)), 10, 10, 0xFFFFFF);
            }
        }
    }
}
