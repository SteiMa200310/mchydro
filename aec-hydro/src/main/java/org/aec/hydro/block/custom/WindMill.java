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
import org.aec.hydro.utils.PipeHandling.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class WindMill extends BlockWithEntity {
    public WindMill(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.PowerLevel, 1));
        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.IsProvider, true));
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
        return VoxelGenerator.makeWindmillShape();
    }

    //Properties
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = this.getDefaultState()
                .with(Properties.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
        System.out.println(ctx.getHorizontalPlayerFacing().getOpposite().toString());

        return state;
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
        builder.add(PipeProperties.PowerLevel);
        builder.add(PipeProperties.IsProvider);
    }
}
