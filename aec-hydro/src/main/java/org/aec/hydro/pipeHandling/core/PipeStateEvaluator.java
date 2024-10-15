package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.Block;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.custom.cable.Cable;
import org.aec.hydro.block.custom.water.WaterPipe;
import org.aec.hydro.pipeHandling.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Nested Function problem -> java does not support implicit predefined delegates nor extension methods nor nested functions nor partial classes ->
//so bysides from always moving to a new file im pretty much stuck with defining them hiracical

//would normaly put those functions either in a partial class or in an extension class -> but both is not possible ->
//but i then ran into the probelm of not beeing able to nest functions or save them into delegates without defining a function interface every time
//that is why its a bit urgly now
public class PipeStateEvaluator {
    private PipeStateEvaluator() {
    }

    public static boolean IsNeighbourConnectionWilling(EnergyContext self, Direction dir1, Direction dir2) {
        return IsNeighbourConnectionWilling(self, dir1) && IsNeighbourConnectionWilling(self, dir2);
    }

    public static boolean IsNeighbourConnectionWilling(EnergyContext self, Direction dir1) {
        EnergyContext ctx = self.GetContextBasedOnDirection(dir1);

        if (ctx == null) {
            if (EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent && self.FakeConnectie != null && self.FakeConnectie == dir1)
                return true;
            return false;
        }

        if (!ctx.IsEvaluated)
            ctx.EvaluateActual();

        //pipe needs two connected neighbours to be fully connected
        if (ctx.PipeContextType == ContextType.Pipe) {
            return !ctx.GetConnectionState().IsTwo() || self.IsConnectedToContext(dir1);
        }
        //on power provider one face is enough for fully connected state
        if (ctx.PipeContextType == ContextType.PowerProvider) {
            return ctx.BlockState.get(Properties.FACING).getOpposite() == dir1 && !ctx.GetConnectionState().IsOne() || self.IsConnectedToContext(dir1);
        }

        if (ctx.PipeContextType == ContextType.PipeCombiner) {
            return true;
        }

        if (ctx.PipeContextType == ContextType.Electrolytic) {
            return PipeStateEvaluator
                .GetElectrolyticOffSet(ctx.BlockState.get(Properties.HORIZONTAL_FACING), self.ElectrolyticFaceOffset)
                .getOpposite() == dir1;
        }

        return false;
    }

    //-----
    //is only to be used on pipes ofc the neighbors can be non pipes but the self has to be a pipe since this feature is only designed for evaluation on pipes
    public static PowerLevelInfo PowerLevelOfConnectionWilling(EnergyContext self, Direction openFace1, Direction openFace2) {
        //self soon to come open faces are openFace1 and openFace2 because those are the approximatly valid connection willings
        if (self.PipeContextType != ContextType.Pipe)
            return PowerLevelInfo.Construct(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

        EnergyContext neighbor1 = openFace1 != null ? self.GetContextBasedOnDirection(openFace1) : null;
        EnergyContext neighbor2 = openFace2 != null ? self.GetContextBasedOnDirection(openFace2) : null;
        PowerFlowDirection cOpenFace1 = openFace1 != null ? PowerFlowDirection.ConvertDirection(openFace1) : PowerFlowDirection.NONE;
        PowerFlowDirection cOpenFace2 = openFace2 != null ? PowerFlowDirection.ConvertDirection(openFace2) : PowerFlowDirection.NONE;

        if (neighbor1 != null && !neighbor1.IsEvaluated)
            neighbor1.EvaluateActual();

        if (neighbor2 != null && !neighbor2.IsEvaluated)
            neighbor2.EvaluateActual();

        if (neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe && neighbor1.GetPipePowerLevelInfo().IsError() ||
                neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe && neighbor2.GetPipePowerLevelInfo().IsError() ||
                self.GetPipePowerLevelInfo().IsError())
            return PowerLevelInfo.Current();

        if (neighbor1 == null && neighbor2 == null)
            return PowerLevelInfo.Default();

        //now a super disgusting thing can happen here
        //if i a pipe is next to two pipes one is connection willing and one is not
        //and they are on opposite sides then the logic will say okay well the not connection willing pipe well is not connection willing
        //but then the care for the fake looking direction or opposite clicked side comes in - and unfortinatly still uses this side as a soon to come face
        //this than makes the PowerLevelOfConnectionWilling logic think okay well there is a neighbor in this direction he must be a calculaty for the power level - which he is not
        //then i get an error state on the pipe even tho it should be the power level of the willing to connect pipe
        //so i again have to check here if the neighbors are connection willing

        boolean ActualAndElektro1 = (self.IsActualLogicPipe() && neighbor1 != null && neighbor1.BlockState.getBlock() == _HydroBlocks.ELEKTROLYZEUR);
        boolean ActualAndElektro2 = (self.IsActualLogicPipe() && neighbor2 != null && neighbor2.BlockState.getBlock() == _HydroBlocks.ELEKTROLYZEUR);

        boolean NotActualAndElektro1AndPipe2 = (!self.IsActualLogicPipe() && neighbor1 != null && neighbor1.BlockState.getBlock() == _HydroBlocks.ELEKTROLYZEUR && neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe);
        boolean NotActualAndElektro2AndPipe1 = (!self.IsActualLogicPipe() && neighbor2 != null && neighbor2.BlockState.getBlock() == _HydroBlocks.ELEKTROLYZEUR && neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe);

        boolean NullOrNotWilling1 = (neighbor1 == null || !PipeStateEvaluator.IsNeighbourConnectionWilling(self, openFace1));
        boolean NullOrNotWilling2 = (neighbor2 == null || !PipeStateEvaluator.IsNeighbourConnectionWilling(self, openFace2));

        if (neighbor1 != null && (NullOrNotWilling2 || ActualAndElektro2 || NotActualAndElektro1AndPipe2)) {
            return PipeStateEvaluator.EvaluateOneNotNull(self, neighbor1, openFace1, cOpenFace1, cOpenFace2);
        }

        if (neighbor2 != null && (NullOrNotWilling1 || ActualAndElektro1 || NotActualAndElektro2AndPipe1)) {
            return PipeStateEvaluator.EvaluateOneNotNull(self, neighbor2, openFace2, cOpenFace2, cOpenFace1);
        }

        return EvaluateBothNotNull(self, openFace1, openFace2, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
    }

    private static PowerLevelInfo EvaluateOneNotNull(EnergyContext self, EnergyContext neighbor, Direction openFace, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        if (neighbor.PipeContextType == ContextType.PowerProvider ||
            neighbor.PipeContextType == ContextType.PipeCombiner) {
            if (neighbor.BlockState.get(Properties.FACING) == openFace.getOpposite()) {
                return PowerLevelInfo.Construct(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2);
            }

            if (neighbor.PipeContextType == ContextType.PipeCombiner && neighbor.GetPowerLevel() < 0)
                return PowerLevelInfo.Error();

            return PowerLevelInfo.Default();
        }

        if (neighbor.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo neighborPowerLevelInfo = neighbor.GetPipePowerLevelInfo();

            if (!neighborPowerLevelInfo.IsDefault() && neighborPowerLevelInfo.flowTo() == cOpenFace1.getOpposite()) //not sure since values are invalid anyways
//                return (!self.IsActualLogicPipe() && self.IsElectrolyticArround()) ?
//                    PowerLevelInfo.Construct(1, cOpenFace2, cOpenFace1) : //cannot get power from neighbor since can only be from electrolytic
//                    PowerLevelInfo.Construct(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2); //i get power from neighbor

                return PowerLevelInfo.Construct(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2);
            else {
                return PowerLevelInfo.Default();
            }
        }

        if (neighbor.PipeContextType == ContextType.Electrolytic) {
            if (self.IsActualLogicPipe())
                return PowerLevelInfo.Default();

            Direction neighborFacing = neighbor.BlockState.get(Properties.HORIZONTAL_FACING);
            Direction possibleWaterPipeFacing = PipeStateEvaluator.GetElectrolyticOffSet(neighborFacing, 0);
            Direction possibleCableFacing = PipeStateEvaluator.GetElectrolyticOffSet(neighborFacing, 1);
            //Big Fucking problem - water pipe is not recognized as a pipe by the cable or oxygen one this is by design
            //But no i need different informations so either i init a context or i - well IDK

            BlockPos waterPos = neighbor.Pos.offset(possibleWaterPipeFacing);
            BlockPos cablePos = neighbor.Pos.offset(possibleCableFacing);

            Block waterBlock = neighbor.World.getBlockState(waterPos).getBlock();
            Block cableBlock = neighbor.World.getBlockState(cablePos).getBlock();

            EnergyContext waterCtx = null;
            EnergyContext cableCtx = null;

            if (waterBlock == _HydroBlocks.WATERPIPE) {
                waterCtx = WaterPipe.MakeContext(neighbor.World, waterPos, ContextType.Pipe);
            }
            if (waterBlock == _HydroBlocks.WATERPIPECOMBINER) {
                waterCtx = WaterPipe.MakeContext(neighbor.World, waterPos, ContextType.PipeCombiner);
            }

            if (cableBlock == _HydroBlocks.CABLE) {
                cableCtx = Cable.MakeContext(neighbor.World, cablePos, ContextType.Pipe);
            }
            if (cableBlock == _HydroBlocks.CABLECOMBINER) {
                cableCtx = Cable.MakeContext(neighbor.World, cablePos, ContextType.PipeCombiner);
            }

            if (waterCtx == null || cableCtx == null)
                return PowerLevelInfo.Default();

            waterCtx.EvaluateActual();
            cableCtx.EvaluateActual();

            boolean waterConnected = waterCtx.IsConnectedToContext(possibleWaterPipeFacing.getOpposite());
            if (!waterConnected)
                return PowerLevelInfo.Default();
            int waterPowerLevel = waterCtx.GetPowerLevel();

            boolean cableConnected = cableCtx.IsConnectedToContext(possibleCableFacing.getOpposite());
            if (!cableConnected)
                return PowerLevelInfo.Default();
            int cablePowerLevel = cableCtx.GetPowerLevel();

            if (waterPowerLevel >= 5 && cablePowerLevel >= 20)
                return PowerLevelInfo.Construct(1, cOpenFace1, cOpenFace2);
            else
                return PowerLevelInfo.Default();
        }

        AECHydro.LOGGER.error("Neighbor was not implemented");
        return PowerLevelInfo.Error();
    }

    //---
    private static PowerLevelInfo EvaluateBothNotNull(EnergyContext self, Direction openFace1, Direction openFace2, EnergyContext neighbor1, EnergyContext neighbor2, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PowerProvider ||
                neighbor1.PipeContextType == ContextType.PipeCombiner && neighbor2.PipeContextType == ContextType.PipeCombiner) {
            if (neighbor1.BlockState.get(Properties.FACING) == openFace1.getOpposite() &&
                    neighbor2.BlockState.get(Properties.FACING) == openFace2.getOpposite())
                return PowerLevelInfo.Error();

            if (neighbor1.BlockState.get(Properties.FACING) == openFace1.getOpposite())
                return PowerLevelInfo.Construct(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);

            if (neighbor2.BlockState.get(Properties.FACING) == openFace2.getOpposite())
                return PowerLevelInfo.Construct(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);

            return PowerLevelInfo.Error();
        }

        if (neighbor1.PipeContextType == ContextType.Pipe && neighbor2.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo neighborPowerLevelInfo1 = neighbor1.GetPipePowerLevelInfo();
            PowerLevelInfo neighborPowerLevelInfo2 = neighbor2.GetPipePowerLevelInfo();

            if (neighborPowerLevelInfo1.IsDefault() && neighborPowerLevelInfo2.IsDefault())
                return PowerLevelInfo.Default();

            if (neighborPowerLevelInfo1.IsDefault()) {
                if (neighborPowerLevelInfo2.flowTo() == cOpenFace2.getOpposite()) {
                    return PowerLevelInfo.Construct(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
                } else {
                    return PowerLevelInfo.Default();
                }
            }

            if (neighborPowerLevelInfo2.IsDefault()) {
                if (neighborPowerLevelInfo1.flowTo() == cOpenFace1.getOpposite()) {
                    return PowerLevelInfo.Construct(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
                } else {
                    return PowerLevelInfo.Default();
                }
            }

            //both are not none at this point

            //flow from neigh 1 to neigh 2
            if (neighborPowerLevelInfo1.flowTo() == cOpenFace1.getOpposite() && neighborPowerLevelInfo2.flowFrom() == cOpenFace2.getOpposite()) {
                return PowerLevelInfo.Construct(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
            }
            //flow from neigh 2 to neigh 1
            if (neighborPowerLevelInfo2.flowTo() == cOpenFace2.getOpposite() && neighborPowerLevelInfo1.flowFrom() == cOpenFace1.getOpposite()) {
                return PowerLevelInfo.Construct(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
            }
        }

        if ((neighbor1.PipeContextType == ContextType.PowerProvider ||
                neighbor1.PipeContextType == ContextType.PipeCombiner) &&
                neighbor2.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo onePipeOnePowerProviderResult = PipeStateEvaluator.EvaluateOnePipeAndOnePowerProviderOrPipeCombiner(openFace1, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
            if (onePipeOnePowerProviderResult != null) return onePipeOnePowerProviderResult;
        }

        if ((neighbor2.PipeContextType == ContextType.PowerProvider ||
                neighbor2.PipeContextType == ContextType.PipeCombiner) &&
                neighbor1.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo onePipeOnePowerProviderResult = PipeStateEvaluator.EvaluateOnePipeAndOnePowerProviderOrPipeCombiner(openFace2, neighbor2, neighbor1, cOpenFace2, cOpenFace1);
            if (onePipeOnePowerProviderResult != null) return onePipeOnePowerProviderResult;
        }

        if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PipeCombiner) {
            PowerLevelInfo onePowerProviderOnePipeCombinerResult = PipeStateEvaluator.EvaluateOnePipeCombinerAndOnePowerProvider(openFace1, openFace2, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
            if (onePowerProviderOnePipeCombinerResult != null) return onePowerProviderOnePipeCombinerResult;
        }

        if (neighbor2.PipeContextType == ContextType.PowerProvider && neighbor1.PipeContextType == ContextType.PipeCombiner) {
            PowerLevelInfo onePowerProviderOnePipeCombinerResult = PipeStateEvaluator.EvaluateOnePipeCombinerAndOnePowerProvider(openFace2, openFace1, neighbor2, neighbor1, cOpenFace2, cOpenFace1);
            if (onePowerProviderOnePipeCombinerResult != null) return onePowerProviderOnePipeCombinerResult;
        }

        if (EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent && neighbor1.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == openFace2) {
            return PowerLevelInfo.Construct(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
        }

        if (EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent && neighbor2.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == openFace1) {
            return PowerLevelInfo.Construct(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
        }

        return PowerLevelInfo.Error();
    }

    private static PowerLevelInfo EvaluateOnePipeAndOnePowerProviderOrPipeCombiner(Direction powerProviderFace1, EnergyContext powerProviderOrPipeCombinerNeighbor1, EnergyContext pipeNeighbor2, PowerFlowDirection cPowerProviderFace1, PowerFlowDirection cPipeFace2) {
        PowerLevelInfo neighborPowerLevelInfo = pipeNeighbor2.GetPipePowerLevelInfo();

        if (neighborPowerLevelInfo.IsError())
            return PowerLevelInfo.Current();

        if (neighborPowerLevelInfo.IsDefault()) {
            //just check power provider -> away 0 to me 1
            if (powerProviderOrPipeCombinerNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite()) {
                return PowerLevelInfo.Construct(powerProviderOrPipeCombinerNeighbor1.GetPowerLevel(), cPowerProviderFace1, cPipeFace2);
            }

            return PowerLevelInfo.Default();
        }

        //check both -> if both look to me ERROR -> since pipe is not none
        if (/*powerProviderOrPipeCombinerNeighbor1.PipeContextType == ContextType.PipeCombiner &&*/
                powerProviderOrPipeCombinerNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite() &&
                        neighborPowerLevelInfo.flowTo() == cPipeFace2.getOpposite()) {
            return PowerLevelInfo.Error();
        }
        //just powerprovider looking to me
        if (powerProviderOrPipeCombinerNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite()) {
            return PowerLevelInfo.Construct(powerProviderOrPipeCombinerNeighbor1.GetPowerLevel(), cPowerProviderFace1, cPipeFace2);
        }

        if (powerProviderOrPipeCombinerNeighbor1.GetPowerLevel() < 0)
            return PowerLevelInfo.Current();

        //just pipe looking to me
//        if (neighborPowerLevelInfo.flowTo() == cPipeFace2.getOpposite()) {
//            return PowerLevelInfo.Construct(pipeNeighbor2.GetPowerLevel(), cPipeFace2, cPowerProviderFace1);
//        }
        return PowerLevelInfo.Construct(pipeNeighbor2.GetPowerLevel(), cPipeFace2, cPowerProviderFace1);
        //TODO: BIG PROBLEM - i always knew that my PowerLevelInfo is not updated based on soon to come faces now i am getting fucked in case of pipe provider next to pipe that is not already properly facing
        //TODO: I got it working like this since the promise of this becoming a power provider is enough for me currenlty but sooner or later i will run into some major iussed with this implementation

        //TODO: The thing i kinda forgot when i wrote the above - what still is valid is the IsDefault check - and by doing it i can evaluate which one had already energy on and which one not and let it flow accordingly
        //TODO: And if both were not default then i could set it to error

        //TODO: Keep a pretty stupid bug in mind -> by setting to "Current" when the blockstate changes its pipe id but not the power info so it can happen that the power info stays on a wrong value until the pipe is fixed
        //TODO: But honestly that doesnt really matter since i only use those directions when i need clarification on energy flow which i do not need in case of error state pipe
        //TODO: Could even be fixed by making an overload of the Current function and providing it with the updated faces just like i would do it when using Construct

//        return null;
    }

    private static PowerLevelInfo EvaluateOnePipeCombinerAndOnePowerProvider(Direction powerProviderFace, Direction pipeCombinerFace, EnergyContext powerProviderNeighbor1, EnergyContext pipeCombinerNeighbor2, PowerFlowDirection cPowerProviderFace1, PowerFlowDirection cPipeCombinerFace2) {
        if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace.getOpposite() &&
                pipeCombinerNeighbor2.BlockState.get(Properties.FACING) == pipeCombinerFace.getOpposite()) {
            return PowerLevelInfo.Error();
        }

        if (powerProviderNeighbor1.GetPowerLevel() < 0 || pipeCombinerNeighbor2.GetPowerLevel() < 0)
            return PowerLevelInfo.Current();

        if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace.getOpposite()) {
            return PowerLevelInfo.Construct(powerProviderNeighbor1.GetPowerLevel(), cPowerProviderFace1, cPipeCombinerFace2);
        }

        if (pipeCombinerNeighbor2.BlockState.get(Properties.FACING) == pipeCombinerFace.getOpposite()) {
            return PowerLevelInfo.Construct(pipeCombinerNeighbor2.GetPowerLevel(), cPipeCombinerFace2, cPowerProviderFace1);
        }

        return null;
    }
    //---
    //-----

    //    private static Map<Integer, Direction> correctDirs = Map.of(
//        0, Direction.NORTH,
//        1, Direction.EAST,
//        2, Direction.SOUTH,
//        3, Direction.WEST
//    );
    private static List<Pair<Integer, Direction>> correctDirs = List.of(
            new Pair<>(0, Direction.NORTH),
            new Pair<>(1, Direction.EAST),
            new Pair<>(2, Direction.SOUTH),
            new Pair<>(3, Direction.WEST)
    );

    /**
     * @param facing          facing direction of the electrolytic
     * @param yRotationOffset offSet of current pipe -> to get the face to which the current pipe should connect
     *                        for example if the facing is North and you are a water pipe then you offset would be 0
     *                        since you are willing to connect to the north face since this is the one the water face is looking to
     *                        but if you are a cable you wanna connect to the cable slot which would be offset by on to the right viewing from + to - on the y axis
     *                        and this 1 is exactly the parameter to provide this function with
     *                        hope that makes sense
     * @return
     */
    public static Direction GetElectrolyticOffSet(Direction facing, int yRotationOffset) { //offSet is defined by the PipeType
        if (facing == Direction.UP || facing == Direction.DOWN) {
            AECHydro.LOGGER.error("GetElectrolyticOffSet provided with non HORIZONTAL_FACING value");
            return Direction.NORTH;
        }

        //can only be placed Horizontal Facing

        //NORTH -> Water
        //EAST -> Electro
        //SOUTH -> Oxygen
        //WEST -> Hydrogen

        int idOfFacing = correctDirs
                .stream()
                .filter(x -> x.getRight() == facing)
                .findFirst()
                .get().getLeft();

        int idOfRequested = (idOfFacing + yRotationOffset) % 4;

        return correctDirs
                .stream()
                .filter(x -> x.getLeft() == idOfRequested)
                .findFirst()
                .get().getRight();
    }
}