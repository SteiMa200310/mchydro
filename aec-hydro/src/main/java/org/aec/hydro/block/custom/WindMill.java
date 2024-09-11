package org.aec.hydro.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.aec.hydro.block.entity.WindMillBlockEntity;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class WindMill extends BlockWithEntity {
    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.makeWindmillShape();
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,1,0, NORTH_SHAPE);
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(0,2,0, NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,3,0, NORTH_SHAPE);
    public WindMill(Settings settings) {
        super(settings);
    }

    // Block Entity
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WindMillBlockEntity(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    //Voxelshape
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.FACING)) {
            case DOWN, NORTH, UP -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    //Properties
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
    }
}
