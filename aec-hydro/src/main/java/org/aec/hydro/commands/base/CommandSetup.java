package org.aec.hydro.commands.base;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.aec.hydro.commands.*;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandSetup {
    private  CommandSetup() {}
    public static List<ACommand> commandList = List.of(
        new TogglePipeLookingDirectionCommand("togglePipeLookingDirection"),
            new GetPowerlevelCommand("getPowerlevel"),
            new SetPowerlevelCommand("setPowerlevel"),
            new ComparePowerlevelCommand("comparePower"),
            new ResetLevelCommand("reset"),
            new SetblockAndUpdateCommand("updateBlock")
    );

    public static void RegisterCommands() {
        commandList.forEach((command) ->
            CommandRegistrationCallback.EVENT.register(command::register)
        );
    }
}