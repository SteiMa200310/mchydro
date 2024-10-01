package org.aec.hydro.block.custom.pipe;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PipeCombiner extends Block {
    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.makePipeCombinerShape();
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(0,2,0,NORTH_SHAPE);
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,1,0,NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,3,0,NORTH_SHAPE);
    private static final VoxelShape UP_SHAPE = VoxelGenerator.rotateShape(1,0,0,NORTH_SHAPE);
    private static final VoxelShape DOWN_SHAPE = VoxelGenerator.rotateShape(3,0,0,NORTH_SHAPE);

    public PipeCombiner(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }
}
