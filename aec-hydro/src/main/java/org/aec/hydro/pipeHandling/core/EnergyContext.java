package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.pipeHandling.delegates.GetContextInDirDelegate;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.*;

import java.util.Arrays;
import java.util.List;

public class EnergyContext {
    //comments describe the required properties:
    public final List<Block> PowerProviders; //facing
    public final Block BaseCombinerBlock; //facing
    public final Block BasePipeBlock; //all pipe properties
    public int ElectrolyticFaceOffset; //would be 0 for the waterpipe

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
    public EnergyContext(World world, BlockPos pos, ContextType contextType, List<Block> powerProviders, Block baseCombinerBlock, Block basePipeBlock, int electrolyticFaceOffset) {
        PowerProviders = powerProviders;
        BaseCombinerBlock = baseCombinerBlock;
        BasePipeBlock = basePipeBlock;
        ElectrolyticFaceOffset = electrolyticFaceOffset;

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

        //this.IsAnyNeighborPipeCombiner() //only take then one if i where to prio them OR power providers which would be the same thing
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
        if (this.GetPipePowerLevelInfo().IsError())
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
        GetContextInDirDelegate getContextInDir = (World world, BlockPos pos, List<Block> powerProviders, Block basePipeCombiner, Block basePipeBlock, int electrolyticFaceOffset, Direction direction) -> {
            BlockPos neighborBlockPos = pos.offset(direction);
            BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
            Block neighborBlock = neighborBlockState.getBlock();

            if (neighborBlock.equals(basePipeBlock))
                return new EnergyContext(world, neighborBlockPos, ContextType.Pipe, powerProviders, basePipeCombiner, basePipeBlock, electrolyticFaceOffset);

            if (neighborBlock.equals(basePipeCombiner)) //if is null that wont even be true so should be fine
                return new EnergyContext(world, neighborBlockPos, ContextType.PipeCombiner, powerProviders, basePipeCombiner, basePipeBlock, electrolyticFaceOffset);

            if (powerProviders.stream().anyMatch(b -> b.equals(neighborBlock)))
                return new EnergyContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders, basePipeCombiner, basePipeBlock, electrolyticFaceOffset);

            if (neighborBlock.equals(_HydroBlocks.ELEKTROLYZEUR))
                return new EnergyContext(world, neighborBlockPos, ContextType.Electrolytic, powerProviders, basePipeCombiner, basePipeBlock, electrolyticFaceOffset);

            return null;
        };

        Arrays.stream(Direction.values()).forEach((direction) -> {
            this.SetContextBasedOnDirection(direction,
                getContextInDir.GetContextInDir(
                    this.World,
                    this.Pos,
                    this.PowerProviders,
                    this.BaseCombinerBlock,
                    this.BasePipeBlock,
                    this.ElectrolyticFaceOffset,
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

        AECHydro.LOGGER.error("connected to more than two pipes or power providers or combiners");
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
    public PowerLevelInfo GetPipePowerLevelInfo() {
        if (this.PipeContextType != ContextType.Pipe) {
            AECHydro.LOGGER.error("GetPipePowerLevelInfo called on non Pipe");
            return PowerLevelInfo.Error();
        }

        return PowerLevelInfo.Construct(
            this.BlockState.get(PipeProperties.PowerLevel),
            this.BlockState.get(PipeProperties.RecieverFace),
            this.BlockState.get(PipeProperties.ProviderFace)
        );
    }
    public int GetPowerLevel() {
        if (this.PipeContextType == ContextType.Pipe || this.PipeContextType == ContextType.PowerProvider) {
            return this.BlockState.get(PipeProperties.PowerLevel);
        }

        if (this.PipeContextType == ContextType.PipeCombiner) {
            if (!this.IsEvaluated)
                this.EvaluateActual();

            int sum = 0;
            for(Direction direction : Direction.values()) {
                EnergyContext dirCtx = this.GetContextBasedOnDirection(direction);
                if (dirCtx == null)
                    continue;
                if (!dirCtx.IsEvaluated)
                    dirCtx.EvaluateActual();

                if (this.IsConnectedToContext(direction)) {
                    if (dirCtx.PipeContextType == ContextType.PipeCombiner && //since is always connected but only valid when provider face hence facing property to us
                        dirCtx.BlockState.get(Properties.FACING) != direction.getOpposite())
                        continue;

                    if (dirCtx.PipeContextType == ContextType.Pipe) { //when connected flowdirection needs to fit
                        PowerLevelInfo pipePowerLevelInfo = dirCtx.GetPipePowerLevelInfo();
                        if (pipePowerLevelInfo.IsError() ||
                            this.BlockState.get(Properties.FACING) != direction && //if is recieving face on pipe combiner
                            !pipePowerLevelInfo.IsDefault() && pipePowerLevelInfo.flowTo().toDirection() != direction.getOpposite()) //and pipe is not provicing
                            return -1;

                        if (pipePowerLevelInfo.IsDefault()) //no add - even tho properties should be (0 NONE NONE)
                            continue;
                    }

                    int neighborlevel = dirCtx.GetPowerLevel();
                    if (neighborlevel < 0)
                        return neighborlevel; //special state

                    if (this.BlockState.get(Properties.FACING) == direction)
                        continue;

                    sum += neighborlevel;
                }
            }

            return sum; //calculate sum of all
        }

        AECHydro.LOGGER.error("GetPowerLevel called on non supported ContextType");
        return -1;
    } //REQUIRES IMPLEMENTATION
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

    public boolean IsActualLogicPipe() {
        return this.ElectrolyticFaceOffset == 0 || this.ElectrolyticFaceOffset == 1;
    }
    public boolean IsElectrolyticArround() {
        return Arrays
            .stream(Direction.values())
            .anyMatch(direction -> {
                EnergyContext ctx = this.GetContextBasedOnDirection(direction);
                if (ctx == null)
                    return false;

                return ctx.BlockState.getBlock() == _HydroBlocks.ELEKTROLYZEUR;
            });
    }

    public boolean IsConnectedToContext(Direction direction) {
        if (!this.IsEvaluated)
            this.EvaluateActual();

        //early returns for finding out if i am even looking into the direction of the neighbor
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

        //this.PipeContextType == ContextType.PipeCombiner //cannot early check anything for him since he will connect to anyone its just based on the neighbor

        if (this.PipeContextType == ContextType.Electrolytic) {
            if (direction == Direction.UP || direction == Direction.DOWN) //cannot be connected to up or down
                return false;
        }

        //if early returns succeed -> check for neigher and if he matches the requirements
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

            if (neighborInfo.PipeContextType == ContextType.PipeCombiner) {
                return true; //always connected if neighbor is combiner since all faces of a combiner serve as connectors
            }

            if (neighborInfo.PipeContextType == ContextType.Electrolytic) {
                return PipeStateEvaluator
                    .GetElectrolyticOffSet(neighborInfo.BlockState.get(Properties.HORIZONTAL_FACING), this.ElectrolyticFaceOffset)
                    .getOpposite() == direction;
            }
        }

        return false;
    }
}