package org.aec.hydro.utils;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class Command {

    public Command(String cmdName) {
        // register new command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(cmdName)
                .executes(this::cmdFunction)));
    }

    protected int cmdFunction(CommandContext<ServerCommandSource> ctx) {

        // code to run
        System.out.println("Called command!");

        return 1;
    }
}
