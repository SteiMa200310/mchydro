package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.Hopper;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.utils.PipeID;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;

public class PipeV2 extends HorizontalFacingBlock {
    private static final VoxelShape SHAPE_UP_DOWN = VoxelGenerator.makePipeV2Shape_UP_DOWN();
    private static final VoxelShape SHAPE_EAST_WEST = VoxelGenerator.makePipeV2Shape_EAST_WEST();
    private static final VoxelShape SHAPE_NORTH_SOUTH = VoxelGenerator.makePipeV2Shape_NORTH_SOUTH();

    public static final DirectionProperty FACING = Properties.FACING; //horizontal Facing cannot save vertical
    //is requiered for rotate and mirror / but i could use the base impl
    //will leave this though just for clarification

    public static final EnumProperty<PipeID> PIPE_ID = EnumProperty.of("pipe_id", PipeID.class);

    public PipeV2(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        //System.out.println(ctx.getPlayerLookDirection().toString()); //getHorizontalPlayerFacing()
        BlockState state = this.getDefaultState();

        switch (ctx.getPlayerLookDirection().getOpposite()) {
            case NORTH:
            case SOUTH:
                state = state.with(PIPE_ID, PipeID.F1);
                break;
            case EAST:
            case WEST:
                state = state.with(PIPE_ID, PipeID.F2);
                break;
            case UP:
            case DOWN:
                state = state.with(PIPE_ID, PipeID.F3);
                break;
            default:
                //System.out.println("look direction did not match any case");
                break;
        }

        return state;
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
        builder.add(FACING, PIPE_ID);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        switch (state.get(PIPE_ID)) {
            case F1:
                return PipeV2.SHAPE_NORTH_SOUTH;
            case F2:
                return PipeV2.SHAPE_EAST_WEST;
            case F3:
                return PipeV2.SHAPE_UP_DOWN;
            default:
                //System.out.println("shape did not match any case");
                return PipeV2.SHAPE_NORTH_SOUTH;
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        BlockState updatedState = getUpdatedState(state, world, pos);
        world.setBlockState(pos, updatedState);
    }

    //customs
    private BlockState getUpdatedState(BlockState state, World world, BlockPos pos) {
        boolean north = hasSameBlock(world, pos, Direction.NORTH);
        boolean south = hasSameBlock(world, pos, Direction.SOUTH);
        boolean east = hasSameBlock(world, pos, Direction.EAST);
        boolean west = hasSameBlock(world, pos, Direction.WEST);
        boolean up = hasSameBlock(world, pos, Direction.UP);
        boolean down = hasSameBlock(world, pos, Direction.DOWN);

        System.out.println("n:" + north + " s:" + south + " e:" + east + " w:" + west + " up:" + up + " down:" + down);

        //3
        if (north && south) {
            System.out.println("ns");
            return state.with(PIPE_ID, PipeID.F1);
        }

        if (east && west) {
            System.out.println("ew");
            return state.with(PIPE_ID, PipeID.F2);
        }

        if (up && down) {
            System.out.println("ud");
            return state.with(PIPE_ID, PipeID.F3);
        }

        //12
        if (north && east) {
            System.out.println("ne");
            return state.with(PIPE_ID, PipeID.E1);
        }

        if (east && south) {
            return state.with(PIPE_ID, PipeID.E2);
        }

        if (south && west) {
            return state.with(PIPE_ID, PipeID.E3);
        }

        if (west && north) {
            return state.with(PIPE_ID, PipeID.E4);
        }

        if (north && up) {
            return state.with(PIPE_ID, PipeID.E5);
        }

        if (north && down) {
            return state.with(PIPE_ID, PipeID.E6);
        }

        if (east && up) {
            return state.with(PIPE_ID, PipeID.E7);
        }

        if (east && down) {
            return state.with(PIPE_ID, PipeID.E8);
        }

        if (south && up) {
            return state.with(PIPE_ID, PipeID.E9);
        }

        if (south && down) {
            return state.with(PIPE_ID, PipeID.E10);
        }

        if (west && up) {
            return state.with(PIPE_ID, PipeID.E11);
        }

        if (west && down) {
            return state.with(PIPE_ID, PipeID.E12);
        }

        return state;
    }

    private boolean hasSameBlock(World world, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.offset(direction);
        BlockState neighborState = world.getBlockState(neighborPos);
        BlockState currentState = world.getBlockState(pos);
        return neighborState.getBlock().equals(currentState.getBlock());
    }
}