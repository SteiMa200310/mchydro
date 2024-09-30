package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.AECHydro;
import org.aec.hydro.pipeHandling.delegates.GetContextInDirDelegate;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EnergyContext {
    //comments describe the required properties:
    public final List<Block> PowerProviders; //facing
    public final Block BaseMergerBlock; //facing
    public final Block BasePipeBlock; //all pipe properties

    public final World World;
    public final BlockPos Pos;
    public final ContextType PipeContextType;

    public Direction FakeConnectie = null;
    public boolean IsEvaluated = false;

    //not too beautifully since "BlockState" is actually when a new block is placed still the pipe -> even to the actual block would be air
    public BlockState BlockState = null;

    public EnergyContext North = null;
    public EnergyContext South = null;
    public EnergyContext East = null;
    public EnergyContext West = null;
    public EnergyContext Up = null;
    public EnergyContext Down = null;

    //statics -> does not have state and is gone uppon restart
    public static boolean CareAboutLookingDirectionWhenRealNeighborIsPresent = false;

    //maybe use builder
    public EnergyContext(World world, BlockPos pos, ContextType contextType, List<Block> powerProviders, Block baseMergerBlock, Block basePipeBlock) {
        PowerProviders = powerProviders;
        BaseMergerBlock = baseMergerBlock;
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
            AECHydro.LOGGER.error("GetCorrectedState called on non pipe");
            return this.BlockState;
        }

        AECHydro.LOGGER.trace(this.Pos.toString());

        //prevents pipes that are already connected to still seek for new connections
        PipeConnectionState state = this.GetConnectionState();
        if (state.IsTwo()) {
            //can also be just a triggered neighbor update where im prefectly fine
            PowerLevelInfo info = PipeStateEvaluator.PowerLevelOfConnectionWilling(this, state.contextDirection1, state.contextDirection2);
            return info.ApplyOn(this.BlockState);
        }

        //priority ifs - (for two neighbors - doesnt matter if already connected or not)
        for(PipeID pipeID : PipeID.AllSerialPipePriority) {
            Pair<Direction, Direction> soonToComeOpenFaces = pipeID.GetOpenFacesBasedOnPipeId();

            boolean isNeighborConnectionWilling = PipeStateEvaluator
                .IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight());

            if (isNeighborConnectionWilling)
                return PipeStateEvaluator
                    .PowerLevelOfConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight()) //Power Info
                    .ApplyOn(this.BlockState)
                    .with(PipeProperties.PIPE_ID, pipeID);
        }

        //just one neighbor but connection to him is already here - (no priority needed)
        for(Direction direction : Direction.values()) {
            EnergyContext dirCtx = this.GetContextBasedOnDirection(direction);
            if (dirCtx != null && this.IsConnectedToContext(direction)) {
                Pair<Direction, Direction> openFaces = this.GetOpenFaces();
                Direction oppositeOpenFace =
                    openFaces.getLeft() == direction ?
                    openFaces.getRight() :
                    openFaces.getLeft();

                return PipeStateEvaluator
                    .PowerLevelOfConnectionWilling(this, direction, oppositeOpenFace)
                    .ApplyOn(this.BlockState);
            }
        }

        //check for dual neighbors -> no connection required
        for(PipeID pipeID : PipeID.FullSerialPipePriority) {
            Pair<Direction, Direction> soonToComeOpenFaces = pipeID.GetOpenFacesBasedOnPipeId();
            EnergyContext dirCtx1 = this.GetContextBasedOnDirection(soonToComeOpenFaces.getLeft());
            EnergyContext dirCtx2 = this.GetContextBasedOnDirection(soonToComeOpenFaces.getRight());

            //&& dirCtx1.PipeContextType == ContextType.Pipe
            if (dirCtx1 != null && PipeStateEvaluator.IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getLeft()) ||
                    dirCtx2 != null && PipeStateEvaluator.IsNeighbourConnectionWilling(this, soonToComeOpenFaces.getRight()))
                return PipeStateEvaluator
                        .PowerLevelOfConnectionWilling(this, soonToComeOpenFaces.getLeft(), soonToComeOpenFaces.getRight())
                        .ApplyOn(this.BlockState)
                        .with(PipeProperties.PIPE_ID, pipeID);
        }

        //if no actuals hit then look for looking direction -> no neighbors are arround so no PowerLevelOfConnectionWilling is required
        //this function is called CareAboutLookingDirectionWhenRealNeighborIsPresent even when this is false -> as the name states
        //it does care about looking direction since no real neighbor is present
        for(PipeID pipeID : PipeID.FullSerialPipePriority) {
            Pair<Direction, Direction> openFaces = pipeID.GetOpenFacesBasedOnPipeId();

            if (this.FakeConnectie == openFaces.getLeft() || this.FakeConnectie == openFaces.getRight())
                return this.BlockState.with(PipeProperties.PIPE_ID, pipeID);
        }

        //backup
        AECHydro.LOGGER.trace("Default PipeState Returned");

        //can only work with self values from here on no other case hit
        if (this.GetCurrentPowerLevelInfo().IsError())
            return this.BlockState;

        return PowerLevelInfo //keep Pipe ID on example edge or different direction than North South (East West / Up Down)
            .Default().ApplyOn(this.BlockState);
    }

    //Evaluators
    public void EvaluateActual() {
        this.BlockState = this.World.getBlockState(this.Pos);
        //use what is actually there -> will be used most of the time

        this.EvaluateNeighboirs();
        this.IsEvaluated = true;
    }
    public void EvaluateBase() {
        this.BlockState = this.BasePipeBlock.getDefaultState();
        //use base in case of pipe will only come in the future - on placement state

        this.EvaluateNeighboirs();
        this.IsEvaluated = true;
    }
    private void EvaluateNeighboirs() {
        GetContextInDirDelegate getContextInDir = (World world, BlockPos pos, List<Block> powerProviders, Block basePipeMerger, Block basePipeBlock, Direction direction) -> {
            BlockPos neighborBlockPos = pos.offset(direction);
            BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
            Block neighborBlock = neighborBlockState.getBlock();

            if (neighborBlock.equals(basePipeBlock))
                return new EnergyContext(world, neighborBlockPos, ContextType.Pipe, powerProviders, basePipeMerger, basePipeBlock);

            try {
                if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
                    return new EnergyContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders, basePipeMerger, basePipeBlock);
                return null;
            } catch (Exception ex) {
                throw  ex;
            }
        };

        Arrays.stream(Direction.values()).forEach((direction) -> {
            this.SetContextBasedOnDirection(direction,
                getContextInDir.GetContextInDir(
                    this.World,
                    this.Pos,
                    this.PowerProviders,
                    this.BaseMergerBlock,
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
                    AECHydro.LOGGER.error("both connected directions already set");
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

        AECHydro.LOGGER.error("connected to more than two pipes or power providers");
        return PipeConnectionState.GetNot();
    }
    public Pair<Direction, Direction> GetOpenFaces() {
        if (this.PipeContextType == ContextType.Pipe)
            return this.BlockState
                .get(PipeProperties.PIPE_ID)
                .GetOpenFacesBasedOnPipeId();

        AECHydro.LOGGER.error("GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }
    public PowerLevelInfo GetCurrentPowerLevelInfo() {
        if (this.PipeContextType != ContextType.Pipe) {
            AECHydro.LOGGER.error("GetCurrentPowerLevelInfo called on non Pipe");
            return PowerLevelInfo.Error();
        }

        return new PowerLevelInfo(
            this.BlockState.get(PipeProperties.PowerLevel),
            this.BlockState.get(PipeProperties.RecieverFace),
            this.BlockState.get(PipeProperties.ProviderFace)
        );
    }
    public int GetPowerLevel() {
        if (this.PipeContextType == ContextType.Pipe) {
            return this.BlockState.get(PipeProperties.PowerLevel);
        }

        AECHydro.LOGGER.error("GetPowerLevel called on non Pipe");
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
            Direction openFace = this.BlockState.get(Properties.FACING);
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
                Direction neighborOpenFace = neighborInfo.BlockState.get(Properties.FACING);

                return direction == neighborOpenFace.getOpposite();
            }
        }

        return false;
    }
}