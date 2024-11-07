package org.aec.hydro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.aec.hydro.commands.base.ACommand;

import java.util.function.Supplier;

import static net.minecraft.server.command.CommandManager.argument;

public class ResetLevelCommand extends ACommand {
    public ResetLevelCommand(String name) {
        super(name);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal(Name)
                .requires(source -> source.hasPermissionLevel(1))
                .executes(this::Execute));
    }

    @Override
    public int Execute(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();

        source.getServer().getCommandManager().executeWithPrefix(source,"/function debug:reset_game");

        source.sendFeedback(()-> Text.literal("Game reset"), true);
        return 1;
    }
}
