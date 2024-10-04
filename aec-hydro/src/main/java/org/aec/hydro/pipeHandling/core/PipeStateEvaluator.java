package org.aec.hydro.pipeHandling.core;

import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.aec.hydro.AECHydro;
import org.aec.hydro.pipeHandling.utils.*;

//Nested Function problem -> java does not support implicit predefined delegates nor extension methods nor nested functions nor partial classes ->
//so bysides from always moving to a new file im pretty much stuck with defining them hiracical

//would normaly put those functions either in a partial class or in an extension class -> but both is not possible ->
//but i then ran into the probelm of not beeing able to nest functions or save them into delegates without defining a function interface every time
//that is why its a bit urgly now
public class PipeStateEvaluator { private PipeStateEvaluator() {}
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

        if( neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe && neighbor1.GetPipePowerLevelInfo().IsError() ||
            neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe && neighbor2.GetPipePowerLevelInfo().IsError() ||
            self.GetPipePowerLevelInfo().IsError())
            return PowerLevelInfo.Error(); //HERE ERROR GETS RETURNED

        if (neighbor1 == null && neighbor2 == null)
            return PowerLevelInfo.Default();

        if (neighbor1 != null && neighbor2 == null)
            return PipeStateEvaluator.EvaluateOneNotNull(neighbor1, openFace1, cOpenFace1, cOpenFace2);

        if (neighbor1 == null && neighbor2 != null)
            return PipeStateEvaluator.EvaluateOneNotNull(neighbor2, openFace2, cOpenFace2, cOpenFace1);

        return EvaluateBothNotNull(self, openFace1, openFace2, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
    }
    private static PowerLevelInfo EvaluateOneNotNull(EnergyContext neighbor, Direction openFace, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        if (neighbor.PipeContextType == ContextType.PowerProvider ||
            neighbor.PipeContextType == ContextType.PipeCombiner) {
            if (neighbor.BlockState.get(Properties.FACING) == openFace.getOpposite()) {
                return PowerLevelInfo.Construct(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2);
            } else {
                return PowerLevelInfo.Default();
            }
        }

        if (neighbor.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo neighborPowerLevelInfo = neighbor.GetPipePowerLevelInfo();

            if (!neighborPowerLevelInfo.IsDefault() &&
                    neighborPowerLevelInfo.flowTo() == cOpenFace1.getOpposite())
                return PowerLevelInfo.Construct(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2); //i get power from neighbor
            else {
                return PowerLevelInfo.Default();
            }
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
    private static PowerLevelInfo EvaluateOnePipeAndOnePowerProviderOrPipeCombiner(Direction powerProviderFace1, EnergyContext powerProviderNeighbor1, EnergyContext pipeNeighbor2, PowerFlowDirection cPowerProviderFace1, PowerFlowDirection cPipeFace2) {
        PowerLevelInfo neighborPowerLevelInfo = pipeNeighbor2.GetPipePowerLevelInfo();

        if (neighborPowerLevelInfo.IsDefault()) {
            //just check power provider -> away 0 to me 1
            if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite()) {
                return PowerLevelInfo.Construct(powerProviderNeighbor1.GetPowerLevel(), cPowerProviderFace1, cPipeFace2);
            } else {
                return PowerLevelInfo.Default();
            }
        } else {
            //check both -> if both look to me ERROR -> since pipe is not none
            if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite() &&
                neighborPowerLevelInfo.flowTo() == cPipeFace2.getOpposite()) {
                return PowerLevelInfo.Error();
            }
            //just powerprovider looking to me
            if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace1.getOpposite()) {
                return PowerLevelInfo.Construct(powerProviderNeighbor1.GetPowerLevel(), cPowerProviderFace1, cPipeFace2);
            }
            //just pipe looking to me
            if (neighborPowerLevelInfo.flowTo() == cPipeFace2.getOpposite()) {
                return PowerLevelInfo.Construct(pipeNeighbor2.GetPowerLevel(), cPipeFace2, cPowerProviderFace1);
            }
            //TODO: BIG PROBLEM - i always knew that my PowerLevelInfo is not updated based on soon to come faces now i am getting fucked in case of pipe provider next to pipe that is not already properly facing
        }

        return null;
    }
    private static PowerLevelInfo EvaluateOnePipeCombinerAndOnePowerProvider(Direction powerProviderFace, Direction pipeCombinerFace, EnergyContext powerProviderNeighbor1, EnergyContext pipeCombinerNeighbor2, PowerFlowDirection cPowerProviderFace1, PowerFlowDirection cPipeCombinerFace2) {
        if (powerProviderNeighbor1.BlockState.get(Properties.FACING) == powerProviderFace.getOpposite() &&
            pipeCombinerNeighbor2.BlockState.get(Properties.FACING) == pipeCombinerFace.getOpposite()) {
            return PowerLevelInfo.Error();
        }

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
}