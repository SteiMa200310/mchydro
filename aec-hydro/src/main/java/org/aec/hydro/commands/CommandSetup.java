package org.aec.hydro.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSetup { private  CommandSetup() {}
    public static List<ACommand> commandList = List.of(
            new ReConfigureCommand("reconfigure"),
		    new TogglePipeLookingDirectionCommand("togglePipeLookingDirection")
    );

    public static void RegisterCommands() {
        commandList.forEach((command) ->
            CommandRegistrationCallback.EVENT
                .register(
                        (dispatcher, registryAccess, environment) -> dispatcher
                            .register(literal(command.Name)
                            .executes(command::Execute)
                        )
                )
        );
    }
}