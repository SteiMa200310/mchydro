package org.aec.hydro.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block.PipeV2;

public class SurroundingEqualsInfo {
    private World world;
    private BlockPos pos;

    private boolean IsEvaluated;

    private SurroundingEqualsInfo north;
    private SurroundingEqualsInfo south;
    private SurroundingEqualsInfo east;
    private SurroundingEqualsInfo west;
    private SurroundingEqualsInfo up;
    private SurroundingEqualsInfo down;

    private Direction LookingDirection;

    public SurroundingEqualsInfo(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;

        this.IsEvaluated = false;

        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.up = null;
        this.down = null;

        this.LookingDirection = null;
    }

    //Evaluate always neesd to be triggered manually in code to not trigger unnecessary recursion
    public void EvaluateSame() {
        this.north = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.NORTH);
        this.south = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.SOUTH);
        this.east = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.EAST);
        this.west = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.WEST);
        this.up = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.UP);
        this.down = SurroundingEqualsInfo.HasSameBlockInDir(world, pos, Direction.DOWN);
        this.IsEvaluated = true;
    }

    public void EvaluateMatch(Block match) {
        this.north = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.NORTH, match);
        this.south = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.SOUTH, match);
        this.east = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.EAST, match);
        this.west = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.WEST, match);
        this.up = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.UP, match);
        this.down = SurroundingEqualsInfo.HasMatchBlockInDir(world, pos, Direction.DOWN, match);
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

    public int AmountOfConnectableNeighbors() {
        int sum = 0;

        if (north != null) {
            north.EvaluateSame();
            sum += north.NotFullyConnected() ? 1 : 0;
        }
        if (south != null) {
            south.EvaluateSame();
            sum += south.NotFullyConnected() ? 1 : 0;
        }
        if (east != null) {
            east.EvaluateSame();
            sum += east.NotFullyConnected() ? 1 : 0;
        }
        if (west != null) {
            west.EvaluateSame();
            sum += west.NotFullyConnected() ? 1 : 0;
        }
        if (up != null) {
            up.EvaluateSame();
            sum += up.NotFullyConnected() ? 1 : 0;
        }
        if (down != null) {
            down.EvaluateSame();
            sum += down.NotFullyConnected() ? 1 : 0;
        }

        return sum;
    }


    public boolean NotFullyConnected() {
        if (AmountOfNeighbors() <= 1)
            return true;

        if (north != null) {
            north.EvaluateSame();
            if (north.NotFullyConnected()) return false;
        }
        if (south != null) {
            south.EvaluateSame();
            if (south.NotFullyConnected()) return false;
        }
        if (east != null) {
            east.EvaluateSame();
            if (east.NotFullyConnected()) return false;
        }
        if (west != null) {
            west.EvaluateSame();
            if (west.NotFullyConnected()) return false;
        }
        if (up != null) {
            up.EvaluateSame();
            if (up.NotFullyConnected()) return false;
        }
        if (down != null) {
            down.EvaluateSame();
            if (down.NotFullyConnected()) return false;
        }

        return true;
    }

    //Fake for looking Direction
    public void SetLookingDirection(Direction dir) {
        this.LookingDirection = dir;
    }

    public boolean ConnectableInNorth() {
        return this.ConnectableIn(north, Direction.NORTH);
    }
    public boolean ConnectableInSouth() {
        return this.ConnectableIn(south, Direction.SOUTH);
    }
    public boolean ConnectableInEast() {
        return this.ConnectableIn(east, Direction.EAST);
    }
    public boolean ConnectableInWest() {
        return this.ConnectableIn(west, Direction.WEST);
    }
    public boolean ConnectableInUp() {
        return this.ConnectableIn(up, Direction.UP);
    }
    public boolean ConnectableInDown() {
        return this.ConnectableIn(down, Direction.DOWN);
    }

    private boolean ConnectableIn(SurroundingEqualsInfo info, Direction dir) {
        boolean LookingDir = this.LookingDirection != null && this.LookingDirection == dir;
        if (LookingDir) return true;

        if (info != null) {
            info.EvaluateSame();
            return info.NotFullyConnected();
        }
        return false;
    }

    //Get Actuals
    public SurroundingEqualsInfo GetNorth() {
        return north;
    }
    public SurroundingEqualsInfo GetSouth() {
        return south;
    }
    public SurroundingEqualsInfo GetEast() {
        return east;
    }
    public SurroundingEqualsInfo GetWest() {
        return west;
    }
    public SurroundingEqualsInfo GetUp() {
        return up;
    }
    public SurroundingEqualsInfo GetDown() {
        return down;
    }

    public static SurroundingEqualsInfo HasSameBlockInDir(World world, BlockPos pos, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(block) ? new SurroundingEqualsInfo(world, neighborBlockPos) : null;
    }

    public static SurroundingEqualsInfo HasMatchBlockInDir(World world, BlockPos pos, Direction direction, Block match) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(match) ? new SurroundingEqualsInfo(world, neighborBlockPos) : null;
    }
}