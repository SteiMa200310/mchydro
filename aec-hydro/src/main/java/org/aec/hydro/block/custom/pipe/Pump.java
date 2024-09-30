package org.aec.hydro.block.custom.pipe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class Pump extends Block {
    private static final VoxelShape UP_SHAPE = VoxelGenerator.makePumpShape(); //roberto exported as up thats why i use as default

    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.rotateShape(3,0,0, UP_SHAPE);
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(1,0,0, UP_SHAPE);
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(3,1,0, UP_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(3,3,0, UP_SHAPE);

    private static final VoxelShape DOWN_SHAPE = VoxelGenerator.rotateShape(2,0,0, UP_SHAPE);

    public Pump(Settings settings) {
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
        return this.getDefaultState().with(Properties.FACING, ctx.getSide());
    }
}