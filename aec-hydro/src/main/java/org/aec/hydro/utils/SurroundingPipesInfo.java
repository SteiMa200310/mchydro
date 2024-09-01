package org.aec.hydro.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block.custom.WindMill;

import java.util.List;

public class SurroundingPipesInfo {
    private final World World;
    private final BlockPos Pos;
    private final List<Block> PowerProviders;
    private boolean IsEvaluated;

    private Direction LookingDirection;
    private Direction CorrectlyFacingPowerProviderDirection;

    private BlockState BlockState;
    private Pair<Direction, Direction> OpenFaces;

    private SurroundingPipesInfo north;
    private SurroundingPipesInfo south;
    private SurroundingPipesInfo east;
    private SurroundingPipesInfo west;
    private SurroundingPipesInfo up;
    private SurroundingPipesInfo down;

    public SurroundingPipesInfo(World world, BlockPos pos, List<Block> powerProviders) {
        this.World = world;
        this.Pos = pos;
        this.PowerProviders = powerProviders;
        this.IsEvaluated = false;

        this.LookingDirection = null;
        this.CorrectlyFacingPowerProviderDirection = null;

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
        this.CorrectlyFacingPowerProviderDirection = GetDirectionOfPossiblePowerProducerOrNull();

        this.north = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.NORTH);
        this.south = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.SOUTH);
        this.east = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.EAST);
        this.west = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.WEST);
        this.up = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.UP);
        this.down = SurroundingPipesInfo.HasSameInDir(World, Pos, this.PowerProviders, Direction.DOWN);

        this.IsEvaluated = true;
    }

    public void EvaluateMatch(Block pipe) {
        this.BlockState = pipe.getDefaultState();
        this.OpenFaces = SurroundingPipesInfo.GetOpenFacesBasedOnPipeId(this.BlockState.get(PipeProperties.PIPE_ID));
        this.CorrectlyFacingPowerProviderDirection = GetDirectionOfPossiblePowerProducerOrNull();

        this.north = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.NORTH, pipe);
        this.south = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.SOUTH, pipe);
        this.east = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.EAST, pipe);
        this.west = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.WEST, pipe);
        this.up = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.UP, pipe);
        this.down = SurroundingPipesInfo.HasPipeInDir(World, Pos, this.PowerProviders, Direction.DOWN, pipe);

        this.IsEvaluated = true;
    }

    public boolean IsEvaluated() {
        return this.IsEvaluated;
    }

    //does only take those into account that actually look into me
    public Direction GetDirectionOfPossiblePowerProducerOrNull() {
        for(Block powerProvider : this.PowerProviders) {
            BlockState bsNorth = SurroundingPipesInfo.GetCertainBlockStateInDirectionOrNull(World, Pos, Direction.NORTH, powerProvider);
            if (bsNorth != null && bsNorth.get(WindMill.FACING) == Direction.SOUTH) { //implement facing property for power sources like i did with pipe properties
                return Direction.NORTH;
            }

            BlockState bsSouth = SurroundingPipesInfo.GetCertainBlockStateInDirectionOrNull(World, Pos, Direction.SOUTH, powerProvider);
            if (bsSouth != null && bsSouth.get(WindMill.FACING) == Direction.NORTH) {
                return Direction.SOUTH;
            }

            BlockState bsEast = SurroundingPipesInfo.GetCertainBlockStateInDirectionOrNull(World, Pos, Direction.EAST, powerProvider);
            if (bsEast != null && bsEast.get(WindMill.FACING) == Direction.WEST) {
                return Direction.EAST;
            }

            BlockState bsWest = SurroundingPipesInfo.GetCertainBlockStateInDirectionOrNull(World, Pos, Direction.WEST, powerProvider);
            if (bsWest != null && bsWest.get(WindMill.FACING) == Direction.EAST) {
                return Direction.WEST;
            }
        }

        return null;
    }

    public int AmountOfPipeNeighbors() {
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

    public int AmountOfCALPs() {
        if (!IsEvaluated())
            Evaluate();

        int sum = 0;
        sum += this.IsInNorthCALP() ? 1 : 0;
        sum += this.IsInSouthCALP() ? 1 : 0;
        sum += this.IsInEastCALP() ? 1 : 0;
        sum += this.IsInWestCALP() ? 1 : 0;
        sum += this.IsInUpCALP() ? 1 : 0;
        sum += this.IsInDownCALP() ? 1 : 0;

        return sum;
    }

    //by default normal evaluate is executed but if explicit evaluate for example based on match is executed previously than ofc this one does also count as evaluated
    public BlockState GetCorrectState() {
        if (!IsEvaluated())
            Evaluate();

        if (this.GetPipeConnectionState() == PipeConnectionState.Full)
            return this.BlockState;

        int amount = this.AmountOfCALPs();

        if (amount == 0 && this.LookingDirection != null) {
            return switch (this.LookingDirection.getOpposite()) {
                case NORTH, SOUTH -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
                case EAST, WEST -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
                case UP, DOWN -> this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            };
        }

        if (amount == 1) {
            //when breaking pipe on the edge then the edge triggers a neighbor update
            //therefore i check in here weather or not that pipe is still connected to the other pipe if yes one of those six cases below hits
            if (north != null && CheckPipeConnectedTo(north, Direction.NORTH)) {
                return this.BlockState;
            }
            if (south != null && CheckPipeConnectedTo(south, Direction.SOUTH)) {
                return this.BlockState;
            }
            if (east != null && CheckPipeConnectedTo(east, Direction.EAST)) {
                return this.BlockState;
            }
            if (west != null && CheckPipeConnectedTo(west, Direction.WEST)) {
                return this.BlockState;
            }
            if (up != null && CheckPipeConnectedTo(up, Direction.UP)) {
                return this.BlockState;
            }
            if (down != null && CheckPipeConnectedTo(down, Direction.DOWN)) {
                return this.BlockState;
            }

            //on the other hand when placing a new block and i have a neighbor that is not connected to anything i align the new block in the neighbor direction and into my looking direction which is the code with > 1 amout of neighbors
            //and i also align the neighbor into the newly place pipe in a straigh maner since he only has one neighbor -> this is what happens in the below 3 cases
            if (this.IsInNorthCALP() || this.IsInSouthCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
            }

            if (this.IsInEastCALP() || this.IsInWestCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
            }

            if (this.IsInUpCALP() || this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            }
        }

        if (amount > 1) {
            //Priority Ifs

            //3
            if (this.IsInNorthCALP() && this.IsInSouthCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
            }

            if (this.IsInEastCALP() && this.IsInWestCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
            }

            if (this.IsInUpCALP() && this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            }

            //12
            if (this.IsInNorthCALP() && this.IsInEastCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E1);
            }

            if (this.IsInEastCALP() && this.IsInSouthCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E2);
            }

            if (this.IsInSouthCALP() && this.IsInWestCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E3);
            }

            if (this.IsInWestCALP() && this.IsInNorthCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E4);
            }


            if (this.IsInNorthCALP() && this.IsInUpCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E5);
            }

            if (this.IsInNorthCALP() && this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E6);
            }

            if (this.IsInEastCALP() && this.IsInUpCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E7);
            }

            if (this.IsInEastCALP() && this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E8);
            }


            if (this.IsInSouthCALP() && this.IsInUpCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E9);
            }

            if (this.IsInSouthCALP() && this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E10);
            }

            if (this.IsInWestCALP() && this.IsInUpCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E11);
            }

            if (this.IsInWestCALP() && this.IsInDownCALP()) {
                return this.BlockState.with(PipeProperties.PIPE_ID, PipeID.E12);
            }
        }

        return this.BlockState;
    }

    public void SetLookingDirection(Direction dir) {
        this.LookingDirection = dir;
    }

    public boolean IsInNorthCALP() {
        return this.IsInDirCALP(north, Direction.NORTH);
    }
    public boolean IsInSouthCALP() {
        return this.IsInDirCALP(south, Direction.SOUTH);
    }
    public boolean IsInEastCALP() {
        return this.IsInDirCALP(east, Direction.EAST);
    }
    public boolean IsInWestCALP() {
        return this.IsInDirCALP(west, Direction.WEST);
    }
    public boolean IsInUpCALP() {
        return this.IsInDirCALP(up, Direction.UP);
    }
    public boolean IsInDownCALP() {
        return this.IsInDirCALP(down, Direction.DOWN);
    }

    //Is In Dir Valid means // CALP MEANS (starting chars) ->
    //Connection Seeker OR
    //Already Connected (btw means already connected to this / yourself) OR
    //Looking Direction (could also be named fake neighbor - since im faking the beeing of someone whos seeking connection - which is something I DONT DO in the CheckPipeConnectedTo so keep that in mind) OR
    //Power Producer Direction
    public boolean IsInDirCALP(SurroundingPipesInfo info, Direction dir) {
        if (!IsEvaluated())
            Evaluate();

        //check for looking dir
        if (this.LookingDirection != null && this.LookingDirection.getOpposite() == dir) //faking dir
            return true;

        //check for power provider
        if (this.CorrectlyFacingPowerProviderDirection != null && this.CorrectlyFacingPowerProviderDirection == dir)
            return true;

        //check on neighbor
        if (info != null) {
            if (!info.IsEvaluated())
                info.Evaluate();

            PipeConnectionState state = info.GetPipeConnectionState();

            //check for connection seeker or already connected
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

        if (AmountOfPipeNeighbors() == 0)
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

        if (this.CorrectlyFacingPowerProviderDirection != null && this.CorrectlyFacingPowerProviderDirection == dirOfNeighbor)
            return true;

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

    public static SurroundingPipesInfo HasSameInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(block) ? new SurroundingPipesInfo(world, neighborBlockPos, powerProviders) : null;
    }

    public static SurroundingPipesInfo HasPipeInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction, Block pipe) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        return neighborBlock.equals(pipe) ? new SurroundingPipesInfo(world, neighborBlockPos, powerProviders) : null;
    }

    public static BlockState GetCertainBlockStateInDirectionOrNull(World world, BlockPos pos, Direction direction, Block block) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        return neighborBlockState.getBlock().equals(block) ? neighborBlockState : null;
    }
}