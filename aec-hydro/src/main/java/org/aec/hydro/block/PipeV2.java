package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.utils.*;
import org.jetbrains.annotations.Nullable;

//public static final DirectionProperty FACING = Properties.FACING; //horizontal Facing cannot save vertical
//is requiered for rotate and mirror / but i could use the base impl
//will leave this though just for clarification

//public static final EnumProperty<PipeID> PIPE_ID = EnumProperty.of("pipe_id", PipeID.class);
//public static final EnumProperty<PipeConnectionState> PIPE_CONNECTION_STATE = EnumProperty.of("pipe_connection_state", PipeConnectionState.class);
//is not a property since its basicly calculated based on neighbors

public class PipeV2 extends HorizontalFacingBlock {
    private static final VoxelShape SHAPE_UP_DOWN = VoxelGenerator.makePipeV2Shape_UP_DOWN();
    private static final VoxelShape SHAPE_EAST_WEST = VoxelGenerator.makePipeV2Shape_EAST_WEST();
    private static final VoxelShape SHAPE_NORTH_SOUTH = VoxelGenerator.makePipeV2Shape_NORTH_SOUTH();

    public PipeV2(Settings settings) {
        super(settings);
    }

    //System.out.println(ctx.getPlayerLookDirection().toString()); //getHorizontalPlayerFacing()

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState defaultState = this.getDefaultState();

        SurroundingPipesInfo info = new SurroundingPipesInfo(ctx.getWorld(), ctx.getBlockPos());
        info.EvaluateMatch(defaultState.getBlock()); //evaluate by match since the current block would be air

        Direction dir = ctx.getPlayerLookDirection();
        if(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite())).getBlock() == this.getDefaultState().getBlock()) {
            dir = ctx.getSide().getOpposite();
        }

        int amount = info.AmountOfConnectionSeekingOrAlreadyConnectedNeighbors();
        System.out.println(amount);

        if (amount == 1 || amount == 0) {
            //if i always set it could get priority over a real block which is not what i want
            info.SetLookingDirection(dir);
        }

        //is connected to one or two
        //block update immediately on place
        //voxel shape

        return info.GetCorrectState();
        //need to get based on Match since -> by the time placing the state is still what is was before -> most likly air
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
        builder.add(FACING, PipeProperties.PIPE_ID);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return switch (state.get(PipeProperties.PIPE_ID)) {
            case F1 -> PipeV2.SHAPE_NORTH_SOUTH;
            case F2 -> PipeV2.SHAPE_EAST_WEST;
            case F3 -> PipeV2.SHAPE_UP_DOWN;
            default ->
                //System.out.println("shape did not match any case");
                PipeV2.SHAPE_NORTH_SOUTH;
        };
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        SurroundingPipesInfo info = new SurroundingPipesInfo(world, pos);

        System.out.println(info.GetPipeConnectionState().toString() + " " + pos.toString());
        world.setBlockState(pos, info.GetCorrectState());
    }
}