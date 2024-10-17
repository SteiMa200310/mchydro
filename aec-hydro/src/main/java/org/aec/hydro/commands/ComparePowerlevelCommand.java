package org.aec.hydro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.*;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
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
                .then(argument("score", ScoreboardObjectiveArgumentType.scoreboardObjective())
                .executes(this::Execute)))));
    }

    @Override
    public int Execute(CommandContext<ServerCommandSource> ctx) {
        System.out.println("ComparePowerlevelCommand Entry");
        ScoreboardObjective score = null;
        ScoreboardPlayerScore playerScore = null;

        try {
            score = ScoreboardObjectiveArgumentType.getObjective(ctx, "score");
            Scoreboard sb = score.getScoreboard();
            playerScore = sb.getPlayerScore("0", score);
        } catch (CommandSyntaxException e) {
            System.out.println("Invalid scoreboard objective");
            ctx.getSource().sendError(Text.literal("Error with score Argument"));
            return -1;
        }

        int compareLevel = IntegerArgumentType.getInteger(ctx, "compare level");
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "target");
        ServerWorld world = ctx.getSource().getWorld();
        int powerlevel = world.getBlockState(pos).get(PipeProperties.PowerLevel);


        if(compareLevel <= powerlevel) playerScore.setScore(1);
        else playerScore.setScore(-1);

        return 1;
    }
}
