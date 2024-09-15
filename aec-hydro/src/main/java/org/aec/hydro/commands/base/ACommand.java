package org.aec.hydro.commands.base;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ACommand {
    public final String Name;

    public ACommand(String name) {
        this.Name = name;
    }

    public abstract int Execute(CommandContext<ServerCommandSource> ctx);
}
