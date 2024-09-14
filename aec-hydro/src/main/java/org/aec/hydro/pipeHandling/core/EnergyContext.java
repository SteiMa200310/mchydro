package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.delegates.GetContextInDirDelegate;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.*;

import java.util.Arrays;
import java.util.List;

public class EnergyContext {
    public final List<Block> PowerProviders;
    public final Block BasePipeBlock;

    public final World World;
    public final BlockPos Pos;
    public final ContextType PipeContextType;

    public Direction FakeConnectie = null;
    public boolean IsEvaluated = false;

    //not too beautifully since "ActualBlockState" is actually when a new block is placed still the pipe -> even to the actual block would be air
    public BlockState ActualBlockState = null;

    public EnergyContext North = null;
    public EnergyContext South = null;
    public EnergyContext East = null;
    public EnergyContext West = null;
    public EnergyContext Up = null;
    public EnergyContext Down = null;

    //maybe use builder
    public EnergyContext(World world, BlockPos pos, ContextType contextType, List<Block> powerProviders, Block basePipeBlock) {
        PowerProviders = powerProviders;
        BasePipeBlock = basePipeBlock;

        World = world;
        Pos = pos;
        PipeContextType = contextType;
    }

    //TODO:
    //3 faces in different direction where all would connect the 3th not prio one will still change its direction to there - leave for now
    //build cap at 30 -> in merger / trigger neighbor update
    //think about error state currently whole pipe becomes error state

    //Logic
    public BlockState GetCorrectedState() {
        if (!this.IsEvaluated)
            this.EvaluateActual();

        if (this.PipeContextType != ContextType.Pipe) {
            System.out.println("ERROR: GetCorrectedState called on non pipe");
            return this.ActualBlockState;
        }

        System.out.println("TRACE: " + this.Pos);

        //prevents pipes that are already connected to still seek for new connections
        PipeConnectionState state = this.GetConnectionState();
        if (state.IsTwo()) {
            //can also be just a triggered neighbor update where im prefectly fine
            PowerLevelInfo info = PipeStateEvaluator.PowerLevelOfConnectionWilling(this, state.contextDirection1, state.contextDirection2);
            return info.ApplyOn(this.ActualBlockState);
        }

        //priority ifs - (for two neighbors)
        for(PipeID pipeID : PipeID.AllSerialPipePriority) {
            Pair<Direction, Direction> soonToComeOpenFaces = pipeID.GetOpenFacesBasedOnPipeId();

            boolean isNeighborConnectionWilling = PipeStateEvaluator
                .IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight());

            if (isNeighborConnectionWilling)
                return PipeStateEvaluator
                    .PowerLevelOfConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight()) //Power Info
                    .ApplyOn(this.ActualBlockState)
                    .with(PipeProperties.PIPE_ID, pipeID);
        }

        //just one neighbor - (no priority needed)
        for(Direction direction : Direction.values()) {
            EnergyContext dirCtx = this.GetContextBasedOnDirection(direction);
            if (dirCtx != null && this.IsConnectedToContext(direction)) { //only take when already connected -> otherwise beneath base case hit to not flatten out when e.x. corner is made
                Pair<Direction, Direction> openFaces = this.GetOpenFaces();
                Direction oppositeOpenFace =
                    openFaces.getLeft() == direction ?
                    openFaces.getRight() :
                    openFaces.getLeft();

                return PipeStateEvaluator
                    .PowerLevelOfConnectionWilling(this, direction, oppositeOpenFace)
                    .ApplyOn(this.ActualBlockState);
            }
        }

        //if no actuals hit then look for looking direction
        for(PipeID pipeID : PipeID.FullSerialPipePriority) {
            Pair<Direction, Direction> openFaces = pipeID.GetOpenFacesBasedOnPipeId();

            if (this.FakeConnectie == openFaces.getLeft() || this.FakeConnectie == openFaces.getRight())
                return this.ActualBlockState.with(PipeProperties.PIPE_ID, pipeID);
        }

        //here would be the logic with that one error to preserve one edge of the previously existing connection not sure if that would fit into the context logic tho
        for(PipeID pipeID : PipeID.FullSerialPipePriority) {
            Pair<Direction, Direction> soonToComeOpenFaces = pipeID.GetOpenFacesBasedOnPipeId();
            EnergyContext dirCtx1 = this.GetContextBasedOnDirection(soonToComeOpenFaces.getLeft());
            EnergyContext dirCtx2 = this.GetContextBasedOnDirection(soonToComeOpenFaces.getRight());

            //&& dirCtx1.PipeContextType == ContextType.Pipe
            if (dirCtx1 != null && PipeStateEvaluator.IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getLeft()) ||
                dirCtx2 != null && PipeStateEvaluator.IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getRight()))
                return PipeStateEvaluator
                    .PowerLevelOfConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight())
                    .ApplyOn(this.ActualBlockState)
                    .with(PipeProperties.PIPE_ID, pipeID);
        }

        //backup
        System.out.println("TRACE: Default PipeState Returned");

        return PowerLevelInfo //keep Pipe ID on example edge or different direction than North South (East West / Up Down)
            .Default().ApplyOn(this.ActualBlockState);
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
                return new EnergyContext(world, neighborBlockPos, ContextType.Pipe, powerProviders, block);

            if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
                return new EnergyContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders, block);

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
            return this.ActualBlockState
                .get(PipeProperties.PIPE_ID)
                .GetOpenFacesBasedOnPipeId();

        System.out.println("ERROR: GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }
    public PowerLevelInfo GetCurrentPowerLevelInfo() {
        if (this.PipeContextType != ContextType.Pipe) {
            System.out.println("ERROR: GetCurrentPowerLevelInfo called on non Pipe");
            return PowerLevelInfo.Error();
        }

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
            if (PipeStateEvaluator.IsNeighbourConnectionWilling(this, direction)) sum++;
        }
        return sum;
    }
    public EnergyContext GetContextBasedOnDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> this.North;
            case SOUTH -> this.South;
            case EAST -> this.East;
            case WEST -> this.West;
            case UP -> this.Up;
            case DOWN -> this.Down;
        };
    }

    public void SetContextBasedOnDirection(Direction direction, EnergyContext context) {
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

        EnergyContext neighborInfo = this.GetContextBasedOnDirection(direction);
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