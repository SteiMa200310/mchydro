package org.aec.hydro.pipeHandling.core;

import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
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

        return false;
    }

    //-----
    public static PowerLevelInfo PowerLevelOfConnectionWilling(EnergyContext self, Direction openFace1, Direction openFace2) {
        //self soon to come open faces are openFace1 and openFace2 because those are the approximatly valid connection willings
        if (self.PipeContextType != ContextType.Pipe)
            return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

        EnergyContext neighbor1 = openFace1 != null ? self.GetContextBasedOnDirection(openFace1) : null;
        EnergyContext neighbor2 = openFace2 != null ? self.GetContextBasedOnDirection(openFace2) : null;
        PowerFlowDirection cOpenFace1 = openFace1 != null ? PowerFlowDirection.ConvertDirection(openFace1) : PowerFlowDirection.NONE;
        PowerFlowDirection cOpenFace2 = openFace2 != null ? PowerFlowDirection.ConvertDirection(openFace2) : PowerFlowDirection.NONE;

        if (neighbor1 != null && !neighbor1.IsEvaluated)
            neighbor1.EvaluateActual();

        if (neighbor2 != null && !neighbor2.IsEvaluated)
            neighbor2.EvaluateActual();

        if( neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe && neighbor1.GetCurrentPowerLevelInfo().IsError() ||
            neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe && neighbor2.GetCurrentPowerLevelInfo().IsError() ||
            self.GetCurrentPowerLevelInfo().IsError())
            return PowerLevelInfo.Error();

        if (neighbor1 == null && neighbor2 == null)
            return PowerLevelInfo.Default();

        if (neighbor1 != null && neighbor2 == null)
            return PipeStateEvaluator.EvaluateOneNotNull(neighbor1, openFace1, cOpenFace1, cOpenFace2);

        if (neighbor1 == null && neighbor2 != null)
            return PipeStateEvaluator.EvaluateOneNotNull(neighbor2, openFace2, cOpenFace2, cOpenFace1);

        return EvaluateBothNotNull(self, openFace1, openFace2, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
    }
    private static PowerLevelInfo EvaluateOneNotNull(EnergyContext neighbor, Direction openFace, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        if (neighbor.PipeContextType == ContextType.PowerProvider) {
            if (neighbor.BlockState.get(Properties.FACING) == openFace.getOpposite()) {
                return new PowerLevelInfo(1, cOpenFace1, cOpenFace2);
            } else {
                return PowerLevelInfo.Default();
            }
        }

        if (neighbor.PipeContextType != ContextType.Pipe) {
            System.out.println("Neighbor was not PowerProvider nor Pipe");
            return PowerLevelInfo.Error();
        }
        PowerLevelInfo neighborPowerLevelInfo = neighbor.GetCurrentPowerLevelInfo();

        if (!neighborPowerLevelInfo.IsDefault() &&
            neighborPowerLevelInfo.flowTo() == cOpenFace1.getOpposite())
            return new PowerLevelInfo(neighbor.GetPowerLevel(), cOpenFace1, cOpenFace2); //i get power from neighbor
        else {
            return PowerLevelInfo.Default();
        }
    }

    //---
    private static PowerLevelInfo EvaluateBothNotNull(EnergyContext self, Direction openFace1, Direction openFace2, EnergyContext neighbor1, EnergyContext neighbor2, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PowerProvider) {
            if (neighbor1.BlockState.get(Properties.FACING) == openFace1.getOpposite() &&
                    neighbor2.BlockState.get(Properties.FACING) == openFace2.getOpposite())
                return PowerLevelInfo.Error();

            return new PowerLevelInfo(1, cOpenFace1, cOpenFace2);
        }

        if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo onePipeOnePowerProviderResult = PipeStateEvaluator.EvaluateOnePipeAndOnePowerProvider(openFace1, neighbor1, neighbor2, cOpenFace1, cOpenFace2);
            if (onePipeOnePowerProviderResult != null) return onePipeOnePowerProviderResult;
        }

        if (neighbor2.PipeContextType == ContextType.PowerProvider && neighbor1.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo onePipeOnePowerProviderResult = PipeStateEvaluator.EvaluateOnePipeAndOnePowerProvider(openFace2, neighbor2, neighbor1, cOpenFace2, cOpenFace1);
            if (onePipeOnePowerProviderResult != null) return onePipeOnePowerProviderResult;
        }

        if (neighbor1.PipeContextType == ContextType.Pipe && neighbor2.PipeContextType == ContextType.Pipe) {
            PowerLevelInfo neighborPowerLevelInfo1 = neighbor1.GetCurrentPowerLevelInfo();
            PowerLevelInfo neighborPowerLevelInfo2 = neighbor2.GetCurrentPowerLevelInfo();

            if (neighborPowerLevelInfo1.IsDefault() && neighborPowerLevelInfo2.IsDefault())
                return PowerLevelInfo.Default();

            if (neighborPowerLevelInfo1.IsDefault()) {
                if (neighborPowerLevelInfo2.flowTo() == cOpenFace2.getOpposite()) {
                    return new PowerLevelInfo(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
                } else {
                    return PowerLevelInfo.Default();
                }
            }

            if (neighborPowerLevelInfo2.IsDefault()) {
                if (neighborPowerLevelInfo1.flowTo() == cOpenFace1.getOpposite()) {
                    return new PowerLevelInfo(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
                } else {
                    return PowerLevelInfo.Default();
                }
            }

            //both are not none at this point

            //flow from neigh 1 to neigh 2
            if (neighborPowerLevelInfo1.flowTo() == cOpenFace1.getOpposite() && neighborPowerLevelInfo2.flowFrom() == cOpenFace2.getOpposite()) {
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
            }
            //flow from neigh 2 to neigh 1
            if (neighborPowerLevelInfo2.flowTo() == cOpenFace2.getOpposite() && neighborPowerLevelInfo1.flowFrom() == cOpenFace1.getOpposite()) {
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
            }
        }

        if (EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent && neighbor1.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == openFace2) {
            return new PowerLevelInfo(neighbor1.GetPowerLevel(), cOpenFace1, cOpenFace2);
        }

        if (EnergyContext.CareAboutLookingDirectionWhenRealNeighborIsPresent && neighbor2.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == openFace1) {
            return new PowerLevelInfo(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
        }

        return PowerLevelInfo.Error();
    }
    private static PowerLevelInfo EvaluateOnePipeAndOnePowerProvider(Direction openFace, EnergyContext neighbor1, EnergyContext neighbor2, PowerFlowDirection cOpenFace1, PowerFlowDirection cOpenFace2) {
        PowerLevelInfo neighborPowerLevelInfo = neighbor2.GetCurrentPowerLevelInfo();

        if (neighborPowerLevelInfo.IsDefault()) {
            //just check power provider -> away 0 to me 1
            if (neighbor1.BlockState.get(Properties.FACING) == openFace.getOpposite()) {
                return new PowerLevelInfo(1, cOpenFace1, cOpenFace2);
            } else {
                return PowerLevelInfo.Default();
            }
        } else {
            //check both -> if both look to me ERROR -> since pipe is not none
            if (neighbor1.BlockState.get(Properties.FACING) == openFace.getOpposite() && neighborPowerLevelInfo.flowTo() == cOpenFace2.getOpposite()) {
                return PowerLevelInfo.Error();
            } else {
                //just powerprovider looking to me
                if (neighbor1.BlockState.get(Properties.FACING) == openFace.getOpposite()) {
                    return new PowerLevelInfo(1, cOpenFace1, cOpenFace2);
                }
                //just pipe looking to me
                if (neighborPowerLevelInfo.flowTo() == cOpenFace2.getOpposite()) {
                    return new PowerLevelInfo(neighbor2.GetPowerLevel(), cOpenFace2, cOpenFace1);
                }
            }
        }

        return null;
    }
    //---
    //-----
}