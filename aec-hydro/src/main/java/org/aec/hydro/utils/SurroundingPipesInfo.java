package org.aec.hydro.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SurroundingPipesInfo {
    private final World World;
    private final BlockPos Pos;
    private boolean IsEvaluated;

    private Direction LookingDirection;
    private BlockState BlockState;
    private Pair<Direction, Direction> OpenFaces;

    private SurroundingPipesInfo north;
    private SurroundingPipesInfo south;
    private SurroundingPipesInfo east;
    private SurroundingPipesInfo west;
    private SurroundingPipesInfo up;
    private SurroundingPipesInfo down;

    public SurroundingPipesInfo(World world, BlockPos pos) {
        this.World = world;
        this.Pos = pos;
        this.IsEvaluated = false;

        this.LookingDirection = null;
        this.BlockState = null;
        this.OpenFaces = null;

        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.up = null;
        this.down = null;
    }

    public void Evaluate() {
        this.BlockState = World.getBlockState(Pos);
        this.OpenFaces = SurroundingPipesInfo.GetOpenFacesBasedOnPipeId(this.BlockState.get(PipeProperties.PIPE_ID));

        this.north = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.NORTH);
        this.south = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.SOUTH);
        this.east = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.EAST);
        this.west = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.WEST);
        this.up = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.UP);
        this.down = SurroundingPipesInfo.HasSameBlockInDir(World, Pos, Direction.DOWN);

        this.IsEvaluated = true;
    }

    public void EvaluateMatch(Block match) {
        this.BlockState = match.getDefaultState();
        this.OpenFaces = SurroundingPipesInfo.GetOpenFacesBasedOnPipeId(this.BlockState.get(PipeProperties.PIPE_ID));

        this.north = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.NORTH, match);
        this.south = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.SOUTH, match);
        this.east = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.EAST, match);
        this.west = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.WEST, match);
        this.up = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.UP, match);
        this.down = SurroundingPipesInfo.HasMatchBlockInDir(World, Pos, Direction.DOWN, match);

        this.IsEvaluated = true;
    }

    public boolean IsEvaluated() {
        return this.IsEvaluated;
    }

    public int AmountOfNeighbors() {
        if (!IsEvaluated())
            Evaluate();

        int sum = 0;
        sum += north != null ? 1 : 0;
        sum += south != null ? 1 : 0;
        sum += east != null ? 1 : 0;
        sum += west != null ? 1 : 0;
        sum += up != null ? 1 : 0;
        sum += down != null ? 1 : 0;

        return sum;
    }

    public int AmountOfConnectionSeekingOrAlreadyConnectedNeighbors() {
        if (!IsEvaluated())
            Evaluate();

        int sum = 0;
        sum += this.IsInNorthConnectionSeekerOrAlreadyConnected() ? 1 : 0;
        sum += this.IsInSouthConnectionSeekerOrAlreadyConnected() ? 1 : 0;
        sum += this.IsInEastConnectionSeekerOrAlreadyConnected() ? 1 : 0;
        sum += this.IsInWestConnectionSeekerOrAlreadyConnected() ? 1 : 0;
        sum += this.IsInUpConnectionSeekerOrAlreadyConnected() ? 1 : 0;
        sum += this.IsInDownConnectionSeekerOrAlreadyConnected() ? 1 : 0;

        return sum;
    }

    //by default normal evaluate is executed but if explicit evaluate for example based on match is executed previously than ofc this one does also count as evaluated
    public BlockState GetCorrectState() {
        if (!IsEvaluated())
            Evaluate();

        if (this.GetPipeConnectionState() == PipeConnectionState.Full)
            return this.BlockState;

        int amount = this.AmountOfConnectionSeekingOrAlreadyConnectedNeighbors();

        if (amount == 0 && this.LookingDirection != null) {
            return switch (this.LookingDirection.getOpposite()) {
                case NORTH, SOUTH -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
                case EAST, WEST -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
                case UP, DOWN -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            };
        }

        if (amount == 1) {
            if (north != null) {
                if (CheckPipeConnectedTo(north, Direction.NORTH))
                    return this.BlockState;
            }
            if (south != null) {
                if (CheckPipeConnectedTo(south, Direction.SOUTH))
                    return this.BlockState;
            }
            if (east != null) {
                if (CheckPipeConnectedTo(east, Direction.EAST))
                    return this.BlockState;
            }
            if (west != null) {
                if (CheckPipeConnectedTo(west, Direction.WEST))
                    return this.BlockState;
            }
            if (up != null) {
                if (CheckPipeConnectedTo(up, Direction.UP))
                    return this.BlockState;
            }
            if (down != null) {
                if (CheckPipeConnectedTo(down, Direction.DOWN))
                    return this.BlockState;
            }

            if (this.IsInNorthConnectionSeekerOrAlreadyConnected() || this.IsInSouthConnectionSeekerOrAlreadyConnected()) {
                //and i am not connected to north then connect there -> but afterwards when i already used the logic to determine which direction i have looked in already use E piece -> this should not be hit
                //where as when a block is placed to a not correctly facing pipe then it should be hit
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
            }

            if (this.IsInEastConnectionSeekerOrAlreadyConnected() || this.IsInWestConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
            }

            if (this.IsInUpConnectionSeekerOrAlreadyConnected() || this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            }
        }

        if (amount > 1) {
            //Priority Ifs

            //3
            if (this.IsInNorthConnectionSeekerOrAlreadyConnected() && this.IsInSouthConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
            }

            if (this.IsInEastConnectionSeekerOrAlreadyConnected() && this.IsInWestConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
            }

            if (this.IsInUpConnectionSeekerOrAlreadyConnected() && this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            }

            //12
            if (this.IsInNorthConnectionSeekerOrAlreadyConnected() && this.IsInEastConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E1);
            }

            if (this.IsInEastConnectionSeekerOrAlreadyConnected() && this.IsInSouthConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E2);
            }

            if (this.IsInSouthConnectionSeekerOrAlreadyConnected() && this.IsInWestConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E3);
            }

            if (this.IsInWestConnectionSeekerOrAlreadyConnected() && this.IsInNorthConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E4);
            }


            if (this.IsInNorthConnectionSeekerOrAlreadyConnected() && this.IsInUpConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E5);
            }

            if (this.IsInNorthConnectionSeekerOrAlreadyConnected() && this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E6);
            }

            if (this.IsInEastConnectionSeekerOrAlreadyConnected() && this.IsInUpConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E7);
            }

            if (this.IsInEastConnectionSeekerOrAlreadyConnected() && this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E8);
            }


            if (this.IsInSouthConnectionSeekerOrAlreadyConnected() && this.IsInUpConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E9);
            }

            if (this.IsInSouthConnectionSeekerOrAlreadyConnected() && this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E10);
            }

            if (this.IsInWestConnectionSeekerOrAlreadyConnected() && this.IsInUpConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E11);
            }

            if (this.IsInWestConnectionSeekerOrAlreadyConnected() && this.IsInDownConnectionSeekerOrAlreadyConnected()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E12);
            }
        }

        return this.BlockState;
    }

    public void SetLookingDirection(Direction dir) {
        this.LookingDirection = dir;
    }

    public boolean IsInNorthConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(north, Direction.NORTH);
    }
    public boolean IsInSouthConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(south, Direction.SOUTH);
    }
    public boolean IsInEastConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(east, Direction.EAST);
    }
    public boolean IsInWestConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(west, Direction.WEST);
    }
    public boolean IsInUpConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(up, Direction.UP);
    }
    public boolean IsInDownConnectionSeekerOrAlreadyConnected() {
        return this.IsInDirConnectionSeekerOrAlreadyConnected(down, Direction.DOWN);
    }

    //Is In Dir Valid means ->
    //Connection Seeker OR
    //Already Connected OR
    //Looking Direction OR
    public boolean IsInDirConnectionSeekerOrAlreadyConnected(SurroundingPipesInfo info, Direction dir) {
        if (!IsEvaluated())
            Evaluate();

        boolean LookingDir = this.LookingDirection != null && this.LookingDirection.getOpposite() == dir;
        if (LookingDir) return true;

        if (info != null) {
            if (!info.IsEvaluated())
                info.Evaluate();

            PipeConnectionState state = info.GetPipeConnectionState();

            return  state == PipeConnectionState.Not ||
                    state == PipeConnectionState.One ||
                    this.CheckPipeConnectedTo(info, dir);

            //is not enough to check not state.full since it our neighbor could be fully connected to someone else and us which would be perfectly valid to use this shape
            //this would as well be a full case
        }
        return false;
    }

    public PipeConnectionState GetPipeConnectionState() {
        if (!IsEvaluated())
            Evaluate();

        if (AmountOfNeighbors() == 0)
            return PipeConnectionState.Not;

        int connectionsFound = 0;
        connectionsFound += CheckPipeConnectedTo(north, Direction.NORTH) ? 1 : 0;
        connectionsFound += CheckPipeConnectedTo(south, Direction.SOUTH) ? 1 : 0;
        connectionsFound += CheckPipeConnectedTo(east, Direction.EAST) ? 1 : 0;
        connectionsFound += CheckPipeConnectedTo(west, Direction.WEST) ? 1 : 0;
        connectionsFound += CheckPipeConnectedTo(up, Direction.UP) ? 1 : 0;
        connectionsFound += CheckPipeConnectedTo(down, Direction.DOWN) ? 1 : 0;

        if (connectionsFound == 0)
            return PipeConnectionState.Not;

        if (connectionsFound == 1)
            return PipeConnectionState.One;

        if (connectionsFound == 2)
            return  PipeConnectionState.Full;

        System.out.println("connected to more than two");
        return PipeConnectionState.Not;
    }

    public boolean CheckPipeConnectedTo(SurroundingPipesInfo neighborInfo, Direction dirOfNeighbor) {
        //if non of my faces go into the direction of the neighbor -> then comparing the direction with the opposit of the neighbors open faces
        //could result in a wrong outcome -> suppose we have a pipe open towards north and south and another pipe in the direction of east open east and west
        //in that case the condition 2. would result in true even tho my pipe is not even open into east hope that make sense
        //this can be even easier demonstrated with two pipes facing north south and another facing north south
        if (this.OpenFaces.getLeft() != dirOfNeighbor && this.OpenFaces.getRight() != dirOfNeighbor)
            return false;

        if (neighborInfo != null) {
            if (!neighborInfo.IsEvaluated())
                neighborInfo.Evaluate();

            //2.
            if (dirOfNeighbor == neighborInfo.OpenFaces.getLeft().getOpposite() || //opposit obviously only relevant for edge pieces
                dirOfNeighbor == neighborInfo.OpenFaces.getRight().getOpposite())
                return true;
        }

        return false;
    }

    //statics
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

    public static SurroundingPipesInfo HasSameBlockInDir(World world, BlockPos pos, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(block) ? new SurroundingPipesInfo(world, neighborBlockPos) : null;
    }

    public static SurroundingPipesInfo HasMatchBlockInDir(World world, BlockPos pos, Direction direction, Block match) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(match) ? new SurroundingPipesInfo(world, neighborBlockPos) : null;
    }
}