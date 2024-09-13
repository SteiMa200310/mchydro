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
    public final World World;
    public final BlockPos Pos;
    public final List<Block> PowerProviders;
    public final ContextType PipeContextType;
    public Direction FakeConnectie = null;
    public boolean IsEvaluated = false;

    //not too beautifully since "ActualBlockState" is actually when a new block is placed still the pipe -> even to the actual block would be air
    public BlockState ActualBlockState = null;
    public Block BasePipeBlock = null;

    public PipeContext North = null;
    public PipeContext South = null;
    public PipeContext East = null;
    public PipeContext West = null;
    public PipeContext Up = null;
    public PipeContext Down = null;

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
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, state.contextDirection1, state.contextDirection2);
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        //BEGIN Priority Ifs -------------------
        //3
        if (PipeContextExtensions.CSH_INCW(this, Direction.NORTH, Direction.SOUTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, Direction.SOUTH);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                .with(PipeProperties.PIPE_ID, PipeID.F1);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.EAST, Direction.WEST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, Direction.WEST);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.F2);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.UP, Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.UP, Direction.DOWN);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.F3);
        }

        //12
        if (PipeContextExtensions.CSH_INCW(this, Direction.NORTH, Direction.EAST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, Direction.EAST);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E1);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.EAST, Direction.SOUTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, Direction.SOUTH);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E2);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.SOUTH, Direction.WEST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.SOUTH, Direction.WEST);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E3);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.WEST, Direction.NORTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.WEST, Direction.NORTH);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E4);
        }


        if (PipeContextExtensions.CSH_INCW(this, Direction.NORTH, Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, Direction.UP);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E5);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.NORTH, Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, Direction.DOWN);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E6);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.EAST, Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, Direction.UP);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E7);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.EAST, Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, Direction.DOWN);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E8);
        }


        if (PipeContextExtensions.CSH_INCW(this, Direction.SOUTH, Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.SOUTH, Direction.UP);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E9);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.SOUTH, Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.SOUTH, Direction.DOWN);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E10);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.WEST, Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.WEST, Direction.UP);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E11);
        }

        if (PipeContextExtensions.CSH_INCW(this, Direction.WEST, Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.WEST, Direction.DOWN);

            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info)
                    .with(PipeProperties.PIPE_ID, PipeID.E12);
        }
        //END Priority Ifs -------------------

        if (this.North != null && this.ConnectedToContext(Direction.NORTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, this.GetOpenFaces().getLeft() == Direction.NORTH ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.South != null && this.ConnectedToContext(Direction.SOUTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.SOUTH, this.GetOpenFaces().getLeft() == Direction.SOUTH ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }


        if (this.East != null && this.ConnectedToContext(Direction.EAST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, this.GetOpenFaces().getLeft() == Direction.EAST ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.West != null && this.ConnectedToContext(Direction.WEST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.WEST, this.GetOpenFaces().getLeft() == Direction.WEST ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.Up != null && this.ConnectedToContext(Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.UP, this.GetOpenFaces().getLeft() == Direction.UP ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.Down != null && this.ConnectedToContext(Direction.DOWN)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.DOWN, this.GetOpenFaces().getLeft() == Direction.DOWN ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

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
        if (this.North != null && this.North.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.NORTH) || this.South != null && this.South.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.SOUTH))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);

        if (this.East != null && this.East.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.EAST) || this.West != null && this.West.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.WEST))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);

        if (this.Up != null && this.Up.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.UP) || this.Down != null && this.Down.PipeContextType == ContextType.Pipe && PipeContextExtensions.CSH_INCW(this, Direction.DOWN))
            return this.ActualBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);

        //backup
        return this.ActualBlockState
            .with(PipeProperties.PowerLevel, 0)
            .with(PipeProperties.ProviderFace, CustomDirection.NONE)
            .with(PipeProperties.RecieverFace, CustomDirection.NONE);
    }

    //Evaluators
    public void Evaluate() {
        this.ActualBlockState = this.GetActualBlockState(); //based on actual block at pos in world

        this.North = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH, this.BasePipeBlock);
        this.South = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH, this.BasePipeBlock);
        this.East = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST, this.BasePipeBlock);
        this.West = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST, this.BasePipeBlock);
        this.Up = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP, this.BasePipeBlock);
        this.Down = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN, this.BasePipeBlock);

        this.IsEvaluated = true;
    }
    public void EvaluateJustPlaced() {
        this.ActualBlockState = this.BasePipeBlock.getDefaultState(); //based on dummy provided block

        this.North = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH, this.BasePipeBlock);
        this.South = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH, this.BasePipeBlock);
        this.East = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST, this.BasePipeBlock);
        this.West = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST, this.BasePipeBlock);
        this.Up = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP, this.BasePipeBlock);
        this.Down = PipeContextExtensions.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN, this.BasePipeBlock);

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
            return PipeContextExtensions.GetOpenFacesBasedOnPipeId(this.ActualBlockState.get(PipeProperties.PIPE_ID));

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
    public PowerLevelInfo GetCurrentPowerLevelInfo() {
        if (this.PipeContextType != PipeContextType.Pipe) {
            System.out.println("ERROR: GetCurrentPowerLevelInfo called on non Pipe");
            return PowerLevelInfo.Error;
        }

        if (this.ActualBlockState.get(PipeProperties.PowerLevel) == 30 &&
            this.ActualBlockState.get(PipeProperties.RecieverFace) == CustomDirection.NONE &&
            this.ActualBlockState.get(PipeProperties.ProviderFace) == CustomDirection.NONE)
            return PowerLevelInfo.Error;

        return new PowerLevelInfo(
            this.ActualBlockState.get(PipeProperties.PowerLevel),
            this.ActualBlockState.get(PipeProperties.RecieverFace),
            this.ActualBlockState.get(PipeProperties.ProviderFace)
        );
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
            if (PipeContextExtensions.CSH_INCW(this, direction)) sum++;
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
}