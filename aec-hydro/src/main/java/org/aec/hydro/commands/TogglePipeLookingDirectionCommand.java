package org.aec.hydro.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.aec.hydro.AECHydro;
import org.aec.hydro.commands.base.ACommand;
import org.aec.hydro.pipeHandling.core.EnergyContext;

import static net.minecraft.server.command.CommandManager.literal;

public class TogglePipeLookingDirectionCommand extends ACommand {
    public TogglePipeLookingDirectionCommand(String name) {
        super(name);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal(Name).executes(this::Execute));
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
