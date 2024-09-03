package org.aec.hydro.utils.PipeHandling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class PipeContext {
    private final World World;
    private final BlockPos Pos;
    private final List<Block> PowerProviders;

    private boolean IsEvaluated = false;
    private ContextType contextType = null;
    private BlockState blockState = null;

    private PipeContext north = null;
    private PipeContext south = null;
    private PipeContext east = null;
    private PipeContext west = null;
    private PipeContext up = null;
    private PipeContext down = null;

    public PipeContext(World world, BlockPos pos, List<Block> powerProviders) {
        World = world;
        Pos = pos;
        PowerProviders = powerProviders;
    }



    //Statics
    public static Pair<Direction, Direction> GetOpenFacesBasedOnPipeId(PipeID pipeId) {
        return switch (pipeId) {
            case F1 -> new Pair<>(Direction.NORTH, Direction.SOUTH);
            case F2 -> new Pair<>(Direction.EAST, Direction.WEST);
            case F3 -> new Pair<>(Direction.UP, Direction.DOWN);

            case E1 -> new Pair<>(Direction.NORTH, Direction.EAST);
            case E2 -> new Pair<>(Direction.EAST, Direction.SOUTH);
            case E3 -> new Pair<>(Direction.SOUTH, Direction.WEST);
            case E4 -> new Pair<>(Direction.WEST, Direction.NORTH);

            case E5 -> new Pair<>(Direction.NORTH, Direction.UP);
            case E6 -> new Pair<>(Direction.NORTH, Direction.DOWN);
            case E7 -> new Pair<>(Direction.EAST, Direction.UP);
            case E8 -> new Pair<>(Direction.EAST, Direction.DOWN);

            case E9 -> new Pair<>(Direction.SOUTH, Direction.UP);
            case E10 -> new Pair<>(Direction.SOUTH, Direction.DOWN);
            case E11 -> new Pair<>(Direction.WEST, Direction.UP);
            case E12 -> new Pair<>(Direction.WEST, Direction.DOWN);
        };
    }

    public static PipeContext HasSameInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(block) ? new SurroundingPipesInfo(world, neighborBlockPos, powerProviders) : null;
    }

    public static PipeContext HasPipeInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction, Block pipe) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(pipe) ? new SurroundingPipesInfo(world, neighborBlockPos, powerProviders) : null;
    }
}
