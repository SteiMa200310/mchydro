package org.aec.hydro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.commands.base.ACommand;
import org.aec.hydro.pipeHandling.utils.PipeProperties;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class GetPowerlevelCommand extends ACommand {
    public GetPowerlevelCommand(String name) {
        super(name);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal(Name)
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("target", BlockPosArgumentType.blockPos())
                .executes(this::Execute)));
    }

    @Override
    public int Execute(CommandContext<ServerCommandSource> ctx) {
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "target");
        ServerWorld world = ctx.getSource().getWorld();
        int powerlevel = world.getBlockState(pos).get(PipeProperties.PowerLevel);

        ctx.getSource().sendFeedback(() -> Text.literal("Powerlevel for %s:%s".formatted(world.getBlockState(pos).getBlock().toString(), powerlevel)), false);
        return 1;
    }
}
