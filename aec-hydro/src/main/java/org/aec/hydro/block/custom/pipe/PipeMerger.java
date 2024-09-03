package org.aec.hydro.block.custom.pipe;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.aec.hydro.block.entity.PipeMergerBlockEntity;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class PipeMerger extends BlockWithEntity {
     //public static final EnumProperty<PipeType> PIPETYPE = EnumProperty.of("type", PipeType.class);
    public PipeMerger(Settings settings) {
        super(settings);
        //this.setDefaultState(this.getStateManager().getDefaultState().with(PIPETYPE, PipeType.WATER));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeMergerBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    //TODO Voxlshape Merger
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelGenerator.makeCableShape();
    }
}