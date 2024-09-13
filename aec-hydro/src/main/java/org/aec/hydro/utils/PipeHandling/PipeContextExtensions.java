package org.aec.hydro.utils.PipeHandling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class PipeContextExtensions {
    private PipeContextExtensions() {}

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
    public static BlockState SetPowerInfoOnBlockState(BlockState state, PowerLevelInfo info) {
        return state
            .with(PipeProperties.PowerLevel, info.powerLevel())
            .with(PipeProperties.ProviderFace, info.flowTo())
            .with(PipeProperties.RecieverFace, info.flowFrom());
    }

    //CSH (Corrected State Helper)
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

        //NOTE: NEED TO CHECK FOR IS NEIGHBOR ALREADY ERROR SINCE THIS DOES NOT CURRENLTY HAPPEN

        if (self.PipeContextType != ContextType.Pipe)
            return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);

        PipeContext neighbor1 = dir1 != null ? self.GetContextBasedOnDirection(dir1) : null;
        PipeContext neighbor2 = dir2 != null ? self.GetContextBasedOnDirection(dir2) : null;
        CustomDirection cdir1 = dir1 != null ? PipeContextExtensions.ConvertDirection(dir1) : null;
        CustomDirection cdir2 = dir2 != null ? PipeContextExtensions.ConvertDirection(dir2) : null;

        if (neighbor1 != null && !neighbor1.IsEvaluated)
            neighbor1.Evaluate();

        if (neighbor2 != null && !neighbor2.IsEvaluated)
            neighbor2.Evaluate();

        if( neighbor1 != null && neighbor1.PipeContextType == ContextType.Pipe && neighbor1.GetCurrentPowerLevelInfo() == PowerLevelInfo.Error ||
            neighbor2 != null && neighbor2.PipeContextType == ContextType.Pipe && neighbor2.GetCurrentPowerLevelInfo() == PowerLevelInfo.Error)
            return PowerLevelInfo.Error;

        if (neighbor1 == null && neighbor2 == null)
            return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);

        if (neighbor1 != null && neighbor2 == null) {
            if (neighbor1.PipeContextType == ContextType.PowerProvider)
                return new PowerLevelInfo(1, cdir1, cdir2);

            if (neighbor1.PipeContextType != ContextType.Pipe) {
                System.out.println("Neighbor was not PowerProvider nor Pipe");
                return PowerLevelInfo.Error;
            }
            Pair<CustomDirection, CustomDirection> neighborFlowDirection = neighbor1.GetFlowDirection();

            if (neighborFlowDirection.getRight() != CustomDirection.NONE && neighborFlowDirection.getLeft() != CustomDirection.NONE &&
                neighborFlowDirection.getRight() == cdir1.getOpposite())
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2); //i get power from neighbor
            else {
                return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);
            }
        }

        if (neighbor1 == null && neighbor2 != null) {
            if (neighbor2.PipeContextType == ContextType.PowerProvider)
                return new PowerLevelInfo(1, cdir2, cdir1);

            if (neighbor2.PipeContextType != ContextType.Pipe) {
                System.out.println("Neighbor was not PowerProvider nor Pipe");
                return PowerLevelInfo.Error;
            }
            Pair<CustomDirection, CustomDirection> neighborFlowDirection = neighbor2.GetFlowDirection();

            if (neighborFlowDirection.getRight() != CustomDirection.NONE && neighborFlowDirection.getLeft() != CustomDirection.NONE &&
                neighborFlowDirection.getRight() == cdir2.getOpposite())
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
            else {
                return new PowerLevelInfo(0, CustomDirection.NONE, CustomDirection.NONE);
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
                return PowerLevelInfo.Error;
            }

            if (neighbor1.PipeContextType == ContextType.Pipe && neighbor2.PipeContextType == ContextType.Pipe) {
                Pair<CustomDirection, CustomDirection> neighborFlowDirection1 = neighbor1.GetFlowDirection();
                Pair<CustomDirection, CustomDirection> neighborFlowDirection2 = neighbor1.GetFlowDirection();

                if (neighborFlowDirection1.getRight() == cdir1.getOpposite() && neighborFlowDirection2.getLeft() == cdir2.getOpposite()) {
                    return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
                }

                if (neighborFlowDirection1.getRight() == cdir2.getOpposite() && neighborFlowDirection2.getLeft() == cdir1.getOpposite()) {
                    return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
                }
            }

            // && self.FakeConnectie != null && self.FakeConnectie == dir2
            if (neighbor1.PipeContextType == ContextType.Pipe) {
                return new PowerLevelInfo(neighbor1.GetPowerLevel(), cdir1, cdir2);
            }
            // && self.FakeConnectie != null && self.FakeConnectie == dir1
            if (neighbor2.PipeContextType == ContextType.Pipe) {
                return new PowerLevelInfo(neighbor2.GetPowerLevel(), cdir2, cdir1);
            }
        }

        return PowerLevelInfo.Error;
    }
}