package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.*;

import java.util.Arrays;
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

    //TODO:
    //3 faces in different direction where all would connect the 3th not prio one will still change its direction to there - leave for now

    //Logic
    public BlockState GetCorrectedState() {
        if (!this.IsEvaluated)
            this.EvaluateActual();

        if (this.PipeContextType != ContextType.Pipe) {
            System.out.println("GetCorrectedState called on non pipe");
            return this.ActualBlockState;
        }

        System.out.println(this.Pos);

        //prevents pipes that are already connected to still seek for new connections
        PipeConnectionState state = this.GetConnectionState();
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

        if (this.North != null && this.IsConnectedToContext(Direction.NORTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.NORTH, this.GetOpenFaces().getLeft() == Direction.NORTH ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.South != null && this.IsConnectedToContext(Direction.SOUTH)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.SOUTH, this.GetOpenFaces().getLeft() == Direction.SOUTH ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }


        if (this.East != null && this.IsConnectedToContext(Direction.EAST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.EAST, this.GetOpenFaces().getLeft() == Direction.EAST ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.West != null && this.IsConnectedToContext(Direction.WEST)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.WEST, this.GetOpenFaces().getLeft() == Direction.WEST ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.Up != null && this.IsConnectedToContext(Direction.UP)) {
            PowerLevelInfo info = PipeContextExtensions.CSH_PowerLevelInConnectionWillings(this, Direction.UP, this.GetOpenFaces().getLeft() == Direction.UP ? this.GetOpenFaces().getRight() : this.GetOpenFaces().getLeft());
            return PipeContextExtensions.SetPowerInfoOnBlockState(this.ActualBlockState, info);
        }

        if (this.Down != null && this.IsConnectedToContext(Direction.DOWN)) {
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
            .with(PipeProperties.ProviderFace, PowerFlowDirection.NONE)
            .with(PipeProperties.RecieverFace, PowerFlowDirection.NONE);
    }

    //Evaluators
    public void EvaluateActual() {
        this.ActualBlockState = this.World.getBlockState(this.Pos);

        this.EvaluateNeighboirs();
        this.IsEvaluated = true;
    }
    public void EvaluateBase() {
        this.ActualBlockState = this.BasePipeBlock.getDefaultState();

        this.EvaluateNeighboirs();
        this.IsEvaluated = true;
    }
    private void EvaluateNeighboirs() {
        GetContextInDirDelegate getContextInDirDelegate = (World world, BlockPos pos, List<Block> powerProviders, Block block, Direction direction) -> {
            BlockPos neighborBlockPos = pos.offset(direction);
            BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
            Block neighborBlock = neighborBlockState.getBlock();

            if (neighborBlock.equals(block))
                return new PipeContext(world, neighborBlockPos, ContextType.Pipe, powerProviders, block);

            if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
                return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders, block);

            return null;
        };

        Arrays.stream(Direction.values()).forEach((direction) -> {
            this.SetContextBasedOnDirection(direction,
                getContextInDirDelegate.GetContextInDir(
                    this.World,
                    this.Pos,
                    this.PowerProviders,
                    this.BasePipeBlock,
                    direction
                )
            );
        });
    }

    //Context Functionalities
    public PipeConnectionState GetConnectionState() {
        if (!this.IsEvaluated)
            this.EvaluateActual();

        //check all pipe neighbors
        Direction connectedDirection1 = null;
        Direction connectedDirection2 = null;

        int sum = 0;
        for(Direction direction : Direction.values()) {
            if (this.IsConnectedToContext(direction)) {
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
            return PipeConnectionState.GetNot();

        if (sum == 1) {
            return PipeConnectionState.GetOne(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1
            );
        }

        if (sum == 2) {
            return PipeConnectionState.GetTwo(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1,
                this.GetContextBasedOnDirection(connectedDirection2),
                connectedDirection2
            );
        }

        System.out.println("ERROR: connected to more than two pipes or power providers");
        return PipeConnectionState.GetNot();
    }
    public Pair<Direction, Direction> GetOpenFaces() {
        if (this.PipeContextType == ContextType.Pipe)
            return switch (this.ActualBlockState.get(PipeProperties.PIPE_ID)) {
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

        System.out.println("ERROR: GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }
    public Pair<PowerFlowDirection, PowerFlowDirection> GetFlowDirection() {
        if (this.PipeContextType == ContextType.Pipe) {
            PowerFlowDirection dir1 = this.ActualBlockState.get(PipeProperties.RecieverFace);
            PowerFlowDirection dir2 = this.ActualBlockState.get(PipeProperties.ProviderFace);
            return new Pair<>(dir1, dir2);
        }

        System.out.println("ERROR: GetFlowDirection called on non Pipe");
        return new Pair<>(null, null);
    }
    public PowerLevelInfo GetCurrentPowerLevelInfo() {
        if (this.PipeContextType != ContextType.Pipe) {
            System.out.println("ERROR: GetCurrentPowerLevelInfo called on non Pipe");
            return PowerLevelInfo.Error;
        }

        if (this.ActualBlockState.get(PipeProperties.PowerLevel) == 30 &&
            this.ActualBlockState.get(PipeProperties.RecieverFace) == PowerFlowDirection.NONE &&
            this.ActualBlockState.get(PipeProperties.ProviderFace) == PowerFlowDirection.NONE)
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
    public void SetFakeDirection(Direction direction) {
        if (!this.IsEvaluated)
            this.EvaluateActual();

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

    public boolean IsConnectedToContext(Direction direction) {
        if (!this.IsEvaluated)
            this.EvaluateActual();

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
                neighborInfo.EvaluateActual(); //does eveluate by the actual block type which is why i then can request properties on demand

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
}