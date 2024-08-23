package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.utils.*;
import org.jetbrains.annotations.Nullable;

public class PipeV2 extends HorizontalFacingBlock {
    private static final VoxelShape SHAPE_UP_DOWN = VoxelGenerator.makePipeV2Shape_UP_DOWN();
    private static final VoxelShape SHAPE_EAST_WEST = VoxelGenerator.makePipeV2Shape_EAST_WEST();
    private static final VoxelShape SHAPE_NORTH_SOUTH = VoxelGenerator.makePipeV2Shape_NORTH_SOUTH();

    public static final DirectionProperty FACING = Properties.FACING; //horizontal Facing cannot save vertical
    //is requiered for rotate and mirror / but i could use the base impl
    //will leave this though just for clarification

    public static final EnumProperty<PipeID> PIPE_ID = EnumProperty.of("pipe_id", PipeID.class);
    //public static final EnumProperty<PipeConnectionState> PIPE_CONNECTION_STATE = EnumProperty.of("pipe_connection_state", PipeConnectionState.class);
    //is not a property since its basicly calculated based on neighbors

    public PipeV2(Settings settings) {
        super(settings);
    }

    //System.out.println(ctx.getPlayerLookDirection().toString()); //getHorizontalPlayerFacing()

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState defaultState = this.getDefaultState();

        Direction opposite = ctx.getPlayerLookDirection().getOpposite();
        return switch (opposite) {
            case NORTH, SOUTH -> defaultState.with(PIPE_ID, PipeID.F1);
            case EAST, WEST -> defaultState.with(PIPE_ID, PipeID.F2);
            case UP, DOWN -> defaultState.with(PIPE_ID, PipeID.F3);
        };

//        SurroundingEqualsInfo info = new SurroundingEqualsInfo(ctx.getWorld(), ctx.getBlockPos());
//        info.EvaluateMatch(defaultState.getBlock());
//        //need to get based on Match since -> by the time placing the state is still what is was before -> most likly air
//
//        int amount = info.AmountOfConnectableNeighbors();
//        System.out.println(amount);
//
//        Direction opposite = ctx.getPlayerLookDirection().getOpposite();
//
//        if (amount == 0) {
//            defaultState = switch (opposite) {
//                case NORTH, SOUTH -> defaultState.with(PIPE_ID, PipeID.F1);
//                case EAST, WEST -> defaultState.with(PIPE_ID, PipeID.F2);
//                case UP, DOWN -> defaultState.with(PIPE_ID, PipeID.F3);
//            };
//        }
//
//        if (amount == 1) {
//            info.SetLookingDirection(opposite);
//            return GetStateBasedOnSurroundings(defaultState, info);
//        }
//
//        if (amount > 1) {
//            return GetStateBasedOnSurroundings(defaultState, info);
//        }

        //is connected to one or two
        //block update immediately on place
        //voxel shape

//        return defaultState;
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
        SurroundingEqualsInfo info = new SurroundingEqualsInfo(world, pos);
        info.EvaluateSame();

        System.out.println(new SurroundingBlocksInfoV2(world, pos).GetPipeConnectionState().toString());
//        world.setBlockState(pos, );
    }

    //customs
    private BlockState GetStateBasedOnSurroundings(BlockState current, SurroundingEqualsInfo info) {
        //System.out.println("n:" + info.north + " s:" + info.south + " e:" + info.east + " w:" + info.west + " up:" + info.up + " down:" + info.down);
        BlockState state = this.getDefaultState();

        //3
        if (info.ConnectableInNorth() && info.ConnectableInSouth()) {
            return state.with(PIPE_ID, PipeID.F1);
        }

        if (info.ConnectableInEast() && info.ConnectableInWest()) {
            return state.with(PIPE_ID, PipeID.F2);
        }

        if (info.ConnectableInUp() && info.ConnectableInDown()) {
            return state.with(PIPE_ID, PipeID.F3);
        }

        //12
        if (info.ConnectableInNorth() && info.ConnectableInEast()) {
            return state.with(PIPE_ID, PipeID.E1);
        }

        if (info.ConnectableInEast() && info.ConnectableInSouth()) {
            return state.with(PIPE_ID, PipeID.E2);
        }

        if (info.ConnectableInSouth() && info.ConnectableInWest()) {
            return state.with(PIPE_ID, PipeID.E3);
        }

        if (info.ConnectableInWest() && info.ConnectableInNorth()) {
            return state.with(PIPE_ID, PipeID.E4);
        }


        if (info.ConnectableInNorth() && info.ConnectableInUp()) {
            return state.with(PIPE_ID, PipeID.E5);
        }

        if (info.ConnectableInNorth() && info.ConnectableInDown()) {
            return state.with(PIPE_ID, PipeID.E6);
        }

        if (info.ConnectableInEast() && info.ConnectableInUp()) {
            return state.with(PIPE_ID, PipeID.E7);
        }

        if (info.ConnectableInEast() && info.ConnectableInDown()) {
            return state.with(PIPE_ID, PipeID.E8);
        }


        if (info.ConnectableInSouth() && info.ConnectableInUp()) {
            return state.with(PIPE_ID, PipeID.E9);
        }

        if (info.ConnectableInSouth() && info.ConnectableInDown()) {
            return state.with(PIPE_ID, PipeID.E10);
        }

        if (info.ConnectableInWest() && info.ConnectableInUp()) {
            return state.with(PIPE_ID, PipeID.E11);
        }

        if (info.ConnectableInWest() && info.ConnectableInDown()) {
            return state.with(PIPE_ID, PipeID.E12);
        }

        return  current;
    }
}