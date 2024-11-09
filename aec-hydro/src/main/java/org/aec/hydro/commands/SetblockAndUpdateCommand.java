package org.aec.hydro.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import org.aec.hydro.commands.base.ACommand;

import static net.minecraft.server.command.CommandManager.argument;

public class SetblockAndUpdateCommand extends ACommand {
    public SetblockAndUpdateCommand(String name) {
        super(name);
    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal(Name)
                .requires(source -> source.hasPermissionLevel(2))
                .then(argument("target", BlockPosArgumentType.blockPos())
                        .then(argument("block", BlockStateArgumentType.blockState(registryAccess))
                                .executes(this::Execute))));
    }

    @Override
    public int Execute(CommandContext<ServerCommandSource> ctx) {
        BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "target");
        BlockState block = BlockStateArgumentType.getBlockState(ctx, "block").getBlockState();

        ctx.getSource().getWorld().setBlockState(pos, block);

        block.neighborUpdate(ctx.getSource().getWorld(), pos, block.getBlock(), pos, false);
        return 0;
    }
}
