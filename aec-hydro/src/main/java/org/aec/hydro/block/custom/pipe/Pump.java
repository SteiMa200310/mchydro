package org.aec.hydro.block.custom.pipe;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class Pump extends Block implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape UP_SHAPE = VoxelGenerator.makePumpShape(); //roberto exported as up thats why i use as default

    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.rotateShape(3,0,0, UP_SHAPE);
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(1,0,0, UP_SHAPE);
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(3,1,0, UP_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(3,3,0, UP_SHAPE);

    private static final VoxelShape DOWN_SHAPE = VoxelGenerator.rotateShape(2,0,0, UP_SHAPE);

    public Pump(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING, WATERLOGGED);
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
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(Properties.FACING, ctx.getSide());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.get(WATERLOGGED) && !state.isOf(newState.getBlock())) {
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

}