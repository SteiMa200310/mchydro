package org.aec.hydro.commands;


import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aec.hydro.AECHydro;
import org.aec.hydro.commands.base.ACommand;
import org.aec.hydro.pipeHandling.core.EnergyContext;

public class TogglePipeLookingDirectionCommand extends ACommand {
    public TogglePipeLookingDirectionCommand(String name) {
        super(name);
    }

    public int Execute(CommandContext<ServerCommandSource> ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.player.sendMessage(Text.of("PipeLookingDirection Toggled"), false);
        AECHydro.LOGGER.info("PipeLookingDirection Toggled");

        EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent =
                !EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent;

        return 1;
    }
}
