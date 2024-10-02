package org.aec.hydro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.commands.base.ACommand;
import org.aec.hydro.pipeHandling.utils.PipeProperties;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ComparePowerlevelCommand extends ACommand {
    public ComparePowerlevelCommand(String name) {
        super(name);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal(Name)
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("target", BlockPosArgumentType.blockPos())
                .then(argument("compare level", IntegerArgumentType.integer(0))
                .then(argument("score", )
                .executes(this::Execute)))));
    }

    @Override
    public int Execute(CommandContext<ServerCommandSource> ctx) {
        ScoreboardCriterion score = ;
        int compareLevel = IntegerArgumentType.getInteger(ctx, "compare level");
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "target");
        ServerWorld world = ctx.getSource().getWorld();
        int powerlevel = world.getBlockState(pos).get(PipeProperties.PowerLevel);

        score
        // powerlevel == compareLevel ?  : -1;

        return 1;
    }
}
