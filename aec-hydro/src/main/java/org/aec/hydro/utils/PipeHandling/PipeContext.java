package org.aec.hydro.utils.PipeHandling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class PipeContext {
    private final World World;
    private final BlockPos Pos;
    private final List<Block> PowerProviders;
    private final ContextType PipeContextType;
    private Direction FakeConnectie = null;
    private boolean IsEvaluated = false;

    //not too beautifully since "ActualBlockState" is actually when a new block is placed still the pipe -> even to the actual block would be air
    private BlockState ActualBlockState = null;
    private Block BasePipeBlock = null;

    private PipeContext North = null;
    private PipeContext South = null;
    private PipeContext East = null;
    private PipeContext West = null;
    private PipeContext Up = null;
    private PipeContext Down = null;

    public PipeContext(World world, BlockPos pos, ContextType contextType, List<Block> powerProviders, Block basePipeBlock) {
        World = world;
        Pos = pos;
        PowerProviders = powerProviders;
        PipeContextType = contextType;
        BasePipeBlock = basePipeBlock;
    }

    //fake connectie not working in specific case when real but fully connected is there -> fixed
    //power provider can only take on to connect -> fixed

    //3 faces in different direction where all would connect the 3th not prio one will still change its direction to there
    // -> leave this for now since its kind of an mc lifecycle problem

    //Logic
    public BlockState GetCorrectedState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        if (this.PipeContextType != ContextType.Pipe) {
            System.out.println("GetCorrectedState called on non pipe");
            return this.ActualBlockState;
        }


        System.out.println(this.Pos);

        //prevents pipes that are already connected to still seek for new connections
        ContextConnectionState state = this.GetConnectionState();
        if (state.IsTwo()) {
            //can also be just a triggered neighbor update where im prefectly fine
            return this.ActualBlockState;
        }

        //BEGIN Priority Ifs -------------------
        //3
        if (PipeContext.CSH_INCW(this, Direction.NORTH, Direction.SOUTH)) {
            PowerLevelInfo info = CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, Direction.SOUTH);

            return this.ActualBlockState
                .with(PipeProperties.PIPE_ID, PipeID.F1)
                .with(PipeProperties.PowerLevel, info.powerLevel())
                .with(PipeProperties.ProviderFace, info.flowTo())
                .with(PipeProperties.RecieverFace, info.flowFrom());
        }

        if (PipeContext.CSH_INCW(this, Direction.EAST, Direction.WEST)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.F2);
        }

        if (PipeContext.CSH_INCW(this, Direction.UP, Direction.DOWN)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.F3);
        }

        //12
        if (PipeContext.CSH_INCW(this, Direction.NORTH, Direction.EAST)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E1);
        }

        if (PipeContext.CSH_INCW(this, Direction.EAST, Direction.SOUTH)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E2);
        }

        if (PipeContext.CSH_INCW(this, Direction.SOUTH, Direction.WEST)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E3);
        }

        if (PipeContext.CSH_INCW(this, Direction.WEST, Direction.NORTH)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E4);
        }


        if (PipeContext.CSH_INCW(this, Direction.NORTH, Direction.UP)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E5);
        }

        if (PipeContext.CSH_INCW(this, Direction.NORTH, Direction.DOWN)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E6);
        }

        if (PipeContext.CSH_INCW(this, Direction.EAST, Direction.UP)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E7);
        }

        if (PipeContext.CSH_INCW(this, Direction.EAST, Direction.DOWN)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E8);
        }


        if (PipeContext.CSH_INCW(this, Direction.SOUTH, Direction.UP)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E9);
        }

        if (PipeContext.CSH_INCW(this, Direction.SOUTH, Direction.DOWN)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E10);
        }

        if (PipeContext.CSH_INCW(this, Direction.WEST, Direction.UP)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E11);
        }

        if (PipeContext.CSH_INCW(this, Direction.WEST, Direction.DOWN)) {
            return this.ActualBlockState
                    .with(PipeProperties.PIPE_ID, PipeID.E12);
        }
        //END Priority Ifs -------------------

        if (this.North != null && this.ConnectedToContext(Direction.NORTH))
            return this.ActualBlockState;

        if (this.South != null && this.ConnectedToContext(Direction.SOUTH))
            return this.ActualBlockState;

        if (this.East != null && this.ConnectedToContext(Direction.EAST))
            return this.ActualBlockState;

        if (this.West != null && this.ConnectedToContext(Direction.WEST))
            return this.ActualBlockState;

        if (this.Up != null && this.ConnectedToContext(Direction.UP))
            return this.ActualBlockState;

        if (this.Down != null && this.ConnectedToContext(Direction.DOWN))
            return this.ActualBlockState;

        //if no actuals hit then look for looking direction
        if (this.FakeConnectie != null) {
            if (this.FakeConnectie == Direction.NORTH || this.FakeConnectie == Direction.SOUTH) {
                return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
            }

            if (this.FakeConnectie == Direction.EAST || this.FakeConnectie == Direction.WEST) {
                return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
            }

            if (this.FakeConnectie == Direction.UP || this.FakeConnectie == Direction.DOWN) {
                return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
            }
        }

        //here would be the logic with that one error to preserve one edge of the previously existing connection not sure if that would fit into the context logic tho
        if (this.North != null && this.North.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.NORTH) || this.South != null && this.South.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.SOUTH))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);

        if (this.East != null && this.East.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.EAST) || this.West != null && this.West.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.WEST))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);

        if (this.Up != null && this.Up.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.UP) || this.Down != null && this.Down.PipeContextType == ContextType.Pipe && PipeContext.CSH_INCW(this, Direction.DOWN))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);

        //backup
        return this.ActualBlockState;
    }

    //Evaluators
    public void Evaluate() {
        this.ActualBlockState = this.GetActualBlockState(); //based on actual block at pos in world

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH, this.BasePipeBlock);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH, this.BasePipeBlock);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST, this.BasePipeBlock);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST, this.BasePipeBlock);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP, this.BasePipeBlock);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN, this.BasePipeBlock);

        this.IsEvaluated = true;
    }
    public void EvaluateJustPlaced() {
        this.ActualBlockState = this.BasePipeBlock.getDefaultState(); //based on dummy provided block

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH, this.BasePipeBlock);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH, this.BasePipeBlock);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST, this.BasePipeBlock);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST, this.BasePipeBlock);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP, this.BasePipeBlock);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN, this.BasePipeBlock);

        this.IsEvaluated = true;
    }

    //Helper
    public ContextConnectionState GetConnectionState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        //check all pipe neighbors
        Direction connectedDirection1 = null;
        Direction connectedDirection2 = null;

        int sum = 0;
        for(Direction direction : Direction.values()) {
            if (this.ConnectedToContext(direction)) {
                sum++;

                if (connectedDirection1 == null)
                    connectedDirection1 = direction;
                else if (connectedDirection2 == null)
                    connectedDirection2 = direction;
                else
                    System.out.println("ERROR: both connected directions already set");
            }
        }

        if (sum == 0)
            return ContextConnectionState.GetNot();

        if (sum == 1) {
            return ContextConnectionState.GetOne(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1
            );
        }

        if (sum == 2) {
            return ContextConnectionState.GetTwo(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1,
                this.GetContextBasedOnDirection(connectedDirection2),
                connectedDirection2
            );
        }

        System.out.println("ERROR: connected to more than two pipes or power providers");
        return ContextConnectionState.GetNot();
    }
    public BlockState GetActualBlockState() {
        return this.World.getBlockState(this.Pos);
    }
    public Pair<Direction, Direction> GetOpenFaces() {
        if (this.PipeContextType == ContextType.Pipe)
            return PipeContext.GetOpenFacesBasedOnPipeId(this.ActualBlockState.get(PipeProperties.PIPE_ID));

        System.out.println("ERROR: GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }
    public Pair<CustomDirection, CustomDirection> GetFlowDirection() {
        if (this.PipeContextType == ContextType.Pipe) {
            CustomDirection dir1 = this.ActualBlockState.get(PipeProperties.RecieverFace);
            CustomDirection dir2 = this.ActualBlockState.get(PipeProperties.ProviderFace);
            return new Pair<>(dir1, dir2);
        }

        System.out.println("ERROR: GetFlowDirection called on non Pipe");
        return new Pair<>(null, null);
    }
    public int GetPowerLevel() {
        if (this.PipeContextType == ContextType.Pipe) {
            return this.ActualBlockState.get(PipeProperties.PowerLevel);
        }

        System.out.println("ERROR: GetPowerLevel called on non Pipe");
        return -1;
    }
    public int GetAmoutOfContextNeighbors() {
        int sum = 0;
        sum += this.North != null ? 1 : 0;
        sum += this.South != null ? 1 : 0;
        sum += this.East != null ? 1 : 0;
        sum += this.West != null ? 1 : 0;
        sum += this.Up != null ? 1 : 0;
        sum += this.Down != null ? 1 : 0;

        return sum;
    }
    public int GetAmoutOfWillingNeighbors() {
        int sum = 0;
        for (Direction direction : Direction.values()) {
            if (PipeContext.CSH_INCW(this, direction)) sum++;
        }
        return sum;
    }
    public void SetFakeDirection(Direction direction) {
        if (!this.IsEvaluated)
            this.Evaluate();

        if (this.GetAmoutOfWillingNeighbors() >= 2)
            return;

//        PipeContext ctx = this.GetContextBasedOnDirection(direction);
//        if (ctx == null)
//            this.SetContextBasedOnDirection(
//                    direction,
//                    new PipeContext(World, this.Pos.offset(direction), ContextType.FakeConnectie, PowerProviders)
//            );

        //leave AmoutOfWillingNeighbors as check for now -> since on more than 2 fakeconnectie should be ignored anyways
        this.FakeConnectie = direction;

        //note that this function has lower priority than if an actual neighbor is detected so its only taken into account if there is not already a neighbor there
        //and if there are less than two neighbors
    }
    public boolean ConnectedToContext(Direction direction) {
        if (!this.IsEvaluated)
            this.Evaluate();

        if (this.PipeContextType == ContextType.Pipe) {
            Pair<Direction, Direction> openFaces = this.GetOpenFaces();
            if (openFaces.getLeft() != direction && openFaces.getRight() != direction)
                return false;
        }
        if (this.PipeContextType == ContextType.PowerProvider) {
            Direction openFace = this.ActualBlockState.get(Properties.FACING);
            if (openFace != direction)
                return false;
        }

        PipeContext neighborInfo = this.GetContextBasedOnDirection(direction);
        if (neighborInfo != null) { //cannot be connected to FakeConnectie -> would have needed to check if context Type could still be fake connectie
            if (!neighborInfo.IsEvaluated)
                neighborInfo.Evaluate(); //does eveluate by the actual block type which is why i then can request properties on demand

            if (neighborInfo.PipeContextType == ContextType.Pipe) {
                Pair<Direction, Direction> neighborOpenFaces = neighborInfo.GetOpenFaces();

                return  direction == neighborOpenFaces.getLeft().getOpposite() || //opposit obviously only relevant for edge pieces
                        direction == neighborOpenFaces.getRight().getOpposite();
            }

            if (neighborInfo.PipeContextType == ContextType.PowerProvider) {
                Direction neighborOpenFace = neighborInfo.ActualBlockState.get(Properties.FACING);

                return direction == neighborOpenFace.getOpposite();
            }
        }

        return false;
    }

    public PipeContext GetContextBasedOnDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> this.North;
            case SOUTH -> this.South;
            case EAST -> this.East;
            case WEST -> this.West;
            case UP -> this.Up;
            case DOWN -> this.Down;
        };
    }
    public void SetContextBasedOnDirection(Direction direction, PipeContext context) {
        switch (direction) {
            case NORTH -> this.North = context;
            case SOUTH -> this.South = context;
            case EAST -> this.East = context;
            case WEST -> this.West = context;
            case UP -> this.Up = context;
            case DOWN -> this.Down = context;
        };
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

    public static PipeContext GetContextInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction, Block block) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        if (neighborBlock.equals(block))
            return new PipeContext(world, neighborBlockPos, ContextType.Pipe, powerProviders, block);

        if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders, block);

        return null;

        //    public static PipeContext GetContextInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction) {
//        BlockPos neighborBlockPos = pos.offset(direction);
//
//        BlockState blockState = world.getBlockState(pos);
//        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
//
//        Block block = blockState.getBlock();
//        Block neighborBlock = neighborBlockState.getBlock();
//
//        if (neighborBlock.equals(block))
//            return new PipeContext(world, neighborBlockPos, ContextType.Pipe, powerProviders);
//
//        if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
//            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders);
//
//        return null;
//    }
    }

    //Statics - CSH (Corrected State Helper)
    //IsNeighbourConnectionWilling
    public static boolean CSH_INCW(PipeContext self, Direction dir1, Direction dir2) {
        return CSH_INCW(self, dir1) && CSH_INCW(self, dir2);
    }
    public static boolean CSH_INCW(PipeContext self, Direction dir1) {
        PipeContext ctx = self.GetContextBasedOnDirection(dir1);

        if (ctx == null) {
            if (self.FakeConnectie != null && self.FakeConnectie == dir1)
                return true;
            return false;
        }

        if (!ctx.IsEvaluated)
            ctx.Evaluate();

        //pipe needs two connected neighbours to be fully connected
        if (ctx.PipeContextType == ContextType.Pipe) {
            return !ctx.GetConnectionState().IsTwo() || self.ConnectedToContext(dir1);
        }
        //on power provider one face is enough for fully connected state
        if (ctx.PipeContextType == ContextType.PowerProvider) {
            return ctx.ActualBlockState.get(Properties.FACING).getOpposite() == dir1 && !ctx.GetConnectionState().IsOne() || self.ConnectedToContext(dir1);
        }

        return false;
    }

    public static PowerLevelInfo CSH_PowerLevelInConnectionWillings(PipeContext self, Direction dir1, Direction dir2) {
        //self soon to come open faces are dir1 and dir2 because those are the approximatly valid connection willings

        if (self.PipeContextType != ContextType.Pipe)
            return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);

        PipeContext neighbor1 = self.GetContextBasedOnDirection(dir1);
        PipeContext neighbor2 = self.GetContextBasedOnDirection(dir2);
        CustomDirection cdir1 = PipeContext.ConvertDirection(dir1);
        CustomDirection cdir2 = PipeContext.ConvertDirection(dir2);

        if (neighbor1 != null && !neighbor1.IsEvaluated)
            neighbor1.Evaluate();

        if (neighbor2 != null && !neighbor2.IsEvaluated)
            neighbor2.Evaluate();

        if (neighbor1 == null && neighbor2 == null)
            return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);

        if (neighbor1 != null && neighbor2 == null) {
            if (neighbor1.PipeContextType == ContextType.PowerProvider)
                return new PowerLevelInfo(1, cdir1, cdir2);

            if (neighbor1.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir2)
                return new PowerLevelInfo(1, cdir1, cdir2);

            Pair<CustomDirection, CustomDirection> flowDirection = neighbor1.GetFlowDirection();
            if (flowDirection.getRight() == CustomDirection.NONE || flowDirection.getLeft() == CustomDirection.NONE) {
                if (flowDirection.getLeft() == self.GetFlowDirection().getRight())
                    return new PowerLevelInfo(1, cdir1, cdir2);
                else {
                    return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);
                }
            }
        }

        if (neighbor1 == null && neighbor2 != null) {
            if (neighbor2.PipeContextType == ContextType.PowerProvider)
                return new PowerLevelInfo(1, cdir2, cdir1);

            if (neighbor2.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir1)
                return new PowerLevelInfo(1, cdir2, cdir1);

            Pair<CustomDirection, CustomDirection> flowDirection = neighbor2.GetFlowDirection();
            if (flowDirection.getRight() == CustomDirection.NONE || flowDirection.getLeft() == CustomDirection.NONE) {
                if (flowDirection.getLeft() == self.GetFlowDirection().getRight())
                    return new PowerLevelInfo(1, cdir2, cdir1);
                else {
                    return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);
                }
            }
        }

        //check for looking direction -> or maybe only do things in with neighbor updates
        if (neighbor1 != null && neighbor2 != null) {
            if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType != ContextType.PowerProvider) {
                return new PowerLevelInfo(1, cdir1, cdir2);
            }
            if (neighbor1.PipeContextType != ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PowerProvider) {
                return new PowerLevelInfo(1, cdir2, cdir1);
            }

            if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PowerProvider) {
                return new PowerLevelInfo(30, CustomDirection.NONE, CustomDirection.NONE);
            }

            if (neighbor1.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir2) {
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
            }
            if (neighbor2.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir2) {
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir1, cdir2);
            }
        }

        return new PowerLevelInfo(30, CustomDirection.NONE, CustomDirection.NONE);
    }

    public static CustomDirection ConvertDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> CustomDirection.NORTH;
            case SOUTH -> CustomDirection.SOUTH;
            case EAST -> CustomDirection.EAST;
            case WEST -> CustomDirection.WEST;
            case UP -> CustomDirection.UP;
            case DOWN -> CustomDirection.DOWN;
        };
    }
}