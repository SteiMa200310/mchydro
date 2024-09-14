package org.aec.hydro.pipeHandling.core;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.PowerLevelInfo;

public class PipeContextExtensions { private PipeContextExtensions() {}
    public static BlockState SetPowerInfoOnBlockState(BlockState state, PowerLevelInfo info) {
        return state
            .with(PipeProperties.PowerLevel, info.powerLevel())
            .with(PipeProperties.ProviderFace, info.flowTo())
            .with(PipeProperties.RecieverFace, info.flowFrom());
    }

    //CSH (Corrected State Helper)
    //IsNeighbourConnectionWilling
    public static boolean CSH_INCW(EnergyContext self, Direction dir1, Direction dir2) {
        return CSH_INCW(self, dir1) && CSH_INCW(self, dir2);
    }
    public static boolean CSH_INCW(EnergyContext self, Direction dir1) {
        EnergyContext ctx = self.GetContextBasedOnDirection(dir1);

        if (ctx == null) {
            if (self.FakeConnectie != null && self.FakeConnectie == dir1)
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
            return ctx.ActualBlockState.get(Properties.FACING).getOpposite() == dir1 && !ctx.GetConnectionState().IsOne() || self.IsConnectedToContext(dir1);
        }

        return false;
    }


    //think about error state -> maybe remove it only set one pipe to error state that goes away when corrected because
    //currently the whole pipe stays in error state
    public static PowerLevelInfo CSH_PowerLevelInConnectionWillings(EnergyContext self, Direction dir1, Direction dir2) {
        //self soon to come open faces are dir1 and dir2 because those are the approximatly valid connection willings

        if (self.PipeContextType != ContextType.Pipe)
            return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

        EnergyContext neighbor1 = dir1 != null ? self.GetContextBasedOnDirection(dir1) : null;
        EnergyContext neighbor2 = dir2 != null ? self.GetContextBasedOnDirection(dir2) : null;
        PowerFlowDirection cdir1 = dir1 != null ? PowerFlowDirection.ConvertDirection(dir1) : PowerFlowDirection.NONE;
        PowerFlowDirection cdir2 = dir2 != null ? PowerFlowDirection.ConvertDirection(dir2) : PowerFlowDirection.NONE;

        if (neighbor1 != null && !neighbor1.IsEvaluated)
            neighbor1.EvaluateActual();

        if (neighbor2 != null && !neighbor2.IsEvaluated)
            neighbor2.EvaluateActual();

        if( neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe && neighbor1.GetCurrentPowerLevelInfo() == PowerLevelInfo.Error ||
            neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe && neighbor2.GetCurrentPowerLevelInfo() == PowerLevelInfo.Error ||
            self.GetCurrentPowerLevelInfo() == PowerLevelInfo.Error)
            return PowerLevelInfo.Error;

        if (neighbor1 == null && neighbor2 == null)
            return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

        if (neighbor1 != null && neighbor2 == null) {
            if (neighbor1.PipeContextType == ContextType.PowerProvider) {
                if (neighbor1.ActualBlockState.get(Properties.FACING) == dir1.getOpposite()) {
                    return new PowerLevelInfo(1, cdir1, cdir2);
                } else {
                    return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                }
            }

            if (neighbor1.PipeContextType != ContextType.Pipe) {
                System.out.println("Neighbor was not PowerProvider nor Pipe");
                return PowerLevelInfo.Error;
            }
            Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection = neighbor1.GetFlowDirection();

            if (neighborFlowDirection.getRight() != PowerFlowDirection.NONE && neighborFlowDirection.getLeft() != PowerFlowDirection.NONE &&
                neighborFlowDirection.getRight() == cdir1.getOpposite())
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2); //i get power from neighbor
            else {
                return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
            }
        }

        if (neighbor1 == null && neighbor2 != null) {
            if (neighbor2.PipeContextType == ContextType.PowerProvider) {
                if (neighbor2.ActualBlockState.get(Properties.FACING) == dir2.getOpposite()) {
                    return new PowerLevelInfo(1, cdir2, cdir1);
                } else {
                    return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                }
            }

            if (neighbor2.PipeContextType != ContextType.Pipe) {
                System.out.println("Neighbor was not PowerProvider nor Pipe");
                return PowerLevelInfo.Error;
            }
            Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection = neighbor2.GetFlowDirection();

            if (neighborFlowDirection.getRight() != PowerFlowDirection.NONE && neighborFlowDirection.getLeft() != PowerFlowDirection.NONE &&
                neighborFlowDirection.getRight() == cdir2.getOpposite())
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
            else {
                return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
            }
        }

        if (neighbor1 != null && neighbor2 != null) {
            if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.PowerProvider) {
                if (neighbor1.ActualBlockState.get(Properties.FACING) == dir1.getOpposite() &&
                    neighbor2.ActualBlockState.get(Properties.FACING) == dir2.getOpposite())
                    return PowerLevelInfo.Error;

                return new PowerLevelInfo(1, cdir1, cdir2);
            }

            if (neighbor1.PipeContextType == ContextType.PowerProvider && neighbor2.PipeContextType == ContextType.Pipe) {
                Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection = neighbor2.GetFlowDirection();

                if (neighborFlowDirection.getRight() == PowerFlowDirection.NONE && neighborFlowDirection.getLeft() == PowerFlowDirection.NONE) {
                    //just check power provider -> away 0 to me 1
                    if (neighbor1.ActualBlockState.get(Properties.FACING) == dir1.getOpposite()) {
                        return new PowerLevelInfo(1, cdir1, cdir2);
                    } else {
                        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                    }
                } else {
                    //check both -> if both look to me ERROR -> since pipe is not none
                    if (neighbor1.ActualBlockState.get(Properties.FACING) == dir1.getOpposite() && neighborFlowDirection.getRight() == cdir2.getOpposite()) {
                        return PowerLevelInfo.Error;
                    } else {
                        //just powerprovider looking to me
                        if (neighbor1.ActualBlockState.get(Properties.FACING) == dir1.getOpposite()) {
                            return new PowerLevelInfo(1, cdir1, cdir2);
                        }
                        //just pipe looking to me
                        if (neighborFlowDirection.getRight() == cdir2.getOpposite()) {
                            return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
                        }
                    }
                }
            }

            if (neighbor2.PipeContextType == ContextType.PowerProvider && neighbor1.PipeContextType == ContextType.Pipe) {
                Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection = neighbor1.GetFlowDirection();

                if (neighborFlowDirection.getRight() == PowerFlowDirection.NONE && neighborFlowDirection.getLeft() == PowerFlowDirection.NONE) {
                    //just check power provider -> away 0 to me 1
                    if (neighbor2.ActualBlockState.get(Properties.FACING) == dir2.getOpposite()) {
                        return new PowerLevelInfo(1, cdir2, cdir1);
                    } else {
                        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                    }
                } else {
                    //check both -> if both look to me ERROR -> since pipe is not none
                    if (neighbor2.ActualBlockState.get(Properties.FACING) == dir2.getOpposite() && neighborFlowDirection.getRight() == cdir1.getOpposite()) {
                        return PowerLevelInfo.Error;
                    } else {
                        //just powerprovider looking to me
                        if (neighbor2.ActualBlockState.get(Properties.FACING) == dir2.getOpposite()) {
                            return new PowerLevelInfo(1, cdir2, cdir1);
                        }
                        //just pipe looking to me
                        if (neighborFlowDirection.getRight() == cdir1.getOpposite()) {
                            return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
                        }
                    }
                }
            }

            if (neighbor1.PipeContextType == ContextType.Pipe && neighbor2.PipeContextType == ContextType.Pipe) {
                Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection1 = neighbor1.GetFlowDirection();
                Pair<PowerFlowDirection, PowerFlowDirection> neighborFlowDirection2 = neighbor2.GetFlowDirection();

                //causesing the 30 30 in both dirs
                if (neighborFlowDirection1.getRight() == PowerFlowDirection.NONE && neighborFlowDirection1.getLeft() == PowerFlowDirection.NONE &&
                    neighborFlowDirection2.getRight() == PowerFlowDirection.NONE && neighborFlowDirection2.getLeft() == PowerFlowDirection.NONE)
                    return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

                if (neighborFlowDirection1.getRight() == PowerFlowDirection.NONE && neighborFlowDirection1.getLeft() == PowerFlowDirection.NONE) {
                    if (neighborFlowDirection2.getRight() == cdir2.getOpposite()) {
                        return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
                    } else {
                        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                    }
                }

                if (neighborFlowDirection2.getRight() == PowerFlowDirection.NONE && neighborFlowDirection2.getLeft() == PowerFlowDirection.NONE) {
                    if (neighborFlowDirection1.getRight() == cdir1.getOpposite()) {
                        return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
                    } else {
                        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
                    }
                }

                //both are not none at this point

                //flow from neigh 1 to neigh 2
                if (neighborFlowDirection1.getRight() == cdir1.getOpposite() && neighborFlowDirection2.getLeft() == cdir2.getOpposite()) {
                    return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
                }
                //flow from neigh 2 to neigh 1
                if (neighborFlowDirection2.getRight() == cdir2.getOpposite() && neighborFlowDirection1.getLeft() == cdir1.getOpposite()) {
                    return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
                }
            }

            if (neighbor1.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir2) {
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
            }

            if (neighbor2.PipeContextType == ContextType.Pipe && self.FakeConnectie != null && self.FakeConnectie == dir1) {
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
            }
        }

        return PowerLevelInfo.Error;
    }
}