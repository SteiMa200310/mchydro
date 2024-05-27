package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.Hopper;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

public class PipeV2 extends HorizontalFacingBlock {
    private static final VoxelShape SHAPE_UP_DOWN = VoxelGenerator.makePipeV2Shape_UP_DOWN();
    private static final VoxelShape SHAPE_EAST_WEST = VoxelGenerator.makePipeV2Shape_EAST_WEST();
    private static final VoxelShape SHAPE_NORTH_SOUTH = VoxelGenerator.makePipeV2Shape_NORTH_SOUTH();

    public static final DirectionProperty FACING = Properties.FACING; //horizontal Facing cannot save vertical

    public PipeV2(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        System.out.println(ctx.getPlayerLookDirection().toString()); //getHorizontalPlayerFacing()
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch ((Direction)state.get(FACING)) {
            case UP:
            case DOWN:
                return PipeV2.SHAPE_UP_DOWN;
            case NORTH:
            case SOUTH:
                return PipeV2.SHAPE_NORTH_SOUTH;
            case EAST:
            case WEST:
                return PipeV2.SHAPE_EAST_WEST;
            default :
                System.out.println("shape did not match any case");
                throw new InvalidParameterException("shape did not match any case");
        }
    }
}
