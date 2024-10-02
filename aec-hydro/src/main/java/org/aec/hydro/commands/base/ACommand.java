package org.aec.hydro.commands.base;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ACommand {
    public final String Name;

    public ACommand(String name) {
        this.Name = name;
    }

    public abstract void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment);

    public abstract int Execute(CommandContext<ServerCommandSource> ctx);
}
