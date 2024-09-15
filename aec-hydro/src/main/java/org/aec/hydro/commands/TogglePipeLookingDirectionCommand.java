package org.aec.hydro.commands;


import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.aec.hydro.pipeHandling.core.EnergyContext;

public class TogglePipeLookingDirectionCommand extends ACommand {
    public TogglePipeLookingDirectionCommand(String name) {
        super(name);
    }

    public int Execute(CommandContext<ServerCommandSource> ctx) {
        EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent =
                !EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent;

        return 1;
    }
}
