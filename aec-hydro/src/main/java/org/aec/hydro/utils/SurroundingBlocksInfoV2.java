package org.aec.hydro.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block.PipeV2;

public class SurroundingBlocksInfoV2 {
    private World world;
    private BlockPos pos;
    private boolean IsEvaluated;

    private BlockState blockState;

    private SurroundingBlocksInfoV2 north;
    private SurroundingBlocksInfoV2 south;
    private SurroundingBlocksInfoV2 east;
    private SurroundingBlocksInfoV2 west;
    private SurroundingBlocksInfoV2 up;
    private SurroundingBlocksInfoV2 down;

    public SurroundingBlocksInfoV2(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
        this.IsEvaluated = false;

        this.blockState = null;

        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.up = null;
        this.down = null;
    }

    public void Evaluate() {
        this.blockState = world.getBlockState(pos);

        this.north = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.NORTH);
        this.south = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.SOUTH);
        this.east = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.EAST);
        this.west = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.WEST);
        this.up = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.UP);
        this.down = SurroundingBlocksInfoV2.HasSameBlockInDir(world, pos, Direction.DOWN);
        this.IsEvaluated = true;
    }

    public boolean IsEvaluated() {
        return this.IsEvaluated;
    }

    public int AmountOfNeighbors() {
        int sum = 0;
        sum += north != null ? 1 : 0;
        sum += south != null ? 1 : 0;
        sum += east != null ? 1 : 0;
        sum += west != null ? 1 : 0;
        sum += up != null ? 1 : 0;
        sum += down != null ? 1 : 0;

        return sum;
    }

    public BlockState CorrectedState() {
        return null;
    }

    public PipeConnectionState GetPipeConnectionState() {
        this.Evaluate();

        if (AmountOfNeighbors() == 0)
            return PipeConnectionState.Not;

        int connectionsFound = 0;
        PipeID myId = this.blockState.get(PipeV2.PIPE_ID);
        Pair<Direction, Direction> myOpenFaces = OpenFace(myId);

        if (myOpenFaces.getLeft() == Direction.NORTH || myOpenFaces.getRight() == Direction.NORTH)
            connectionsFound += CheckPipeConnectedTo(north, myOpenFaces) ? 1 : 0;

        if (myOpenFaces.getLeft() == Direction.SOUTH || myOpenFaces.getRight() == Direction.SOUTH)
            connectionsFound += CheckPipeConnectedTo(south, myOpenFaces) ? 1 : 0;

        if (myOpenFaces.getLeft() == Direction.EAST || myOpenFaces.getRight() == Direction.EAST)
            connectionsFound += CheckPipeConnectedTo(east, myOpenFaces) ? 1 : 0;

        if (myOpenFaces.getLeft() == Direction.WEST || myOpenFaces.getRight() == Direction.WEST)
            connectionsFound += CheckPipeConnectedTo(west, myOpenFaces) ? 1 : 0;

        if (myOpenFaces.getLeft() == Direction.UP || myOpenFaces.getRight() == Direction.UP)
            connectionsFound += CheckPipeConnectedTo(up, myOpenFaces) ? 1 : 0;

        if (myOpenFaces.getLeft() == Direction.DOWN || myOpenFaces.getRight() == Direction.DOWN)
            connectionsFound += CheckPipeConnectedTo(down, myOpenFaces) ? 1 : 0;


        if (connectionsFound == 0)
            return PipeConnectionState.Not;

        if (connectionsFound == 1)
            return PipeConnectionState.One;

        if (connectionsFound == 2)
            return  PipeConnectionState.Full;

        System.out.println("connected to more than two");
        return PipeConnectionState.Not;
    }

    public boolean CheckPipeConnectedTo(SurroundingBlocksInfoV2 info, Pair<Direction, Direction> myOpenFaces) {
        if (info != null) {
            info.Evaluate();
            PipeID neighborId = info.blockState.get(PipeV2.PIPE_ID);
            Pair<Direction, Direction> neighborOpenFaces = OpenFace(neighborId);

            if (neighborOpenFaces.getLeft() == myOpenFaces.getLeft() ||
                neighborOpenFaces.getRight() == myOpenFaces.getLeft() ||
                neighborOpenFaces.getLeft() == myOpenFaces.getRight() ||
                neighborOpenFaces.getRight() == myOpenFaces.getRight())
                return true;
        }

        return false;
    }

    public Pair<Direction, Direction> OpenFace(PipeID pipeId) {
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

    //statics
    public static SurroundingBlocksInfoV2 HasSameBlockInDir(World world, BlockPos pos, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(block) ? new SurroundingBlocksInfoV2(world, neighborBlockPos) : null;
    }
}