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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PipeV2 extends HorizontalFacingBlock {
    private static final PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
        VoxelGenerator.makePipeV2LongShape_NORTH_SOUTH(),
        VoxelGenerator.makePipeV2EdgeShape_NORTH_EAST()
    );

    //cannot do that since static init can happen here prior to in HydroBlocks
//    private static final List<Block> PowerProvider = Arrays.asList(
//        _HydroBlocks.WIND_MILL
//    );

    public PipeV2(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        SurroundingPipesInfo info = new SurroundingPipesInfo(ctx.getWorld(), ctx.getBlockPos(), Arrays.asList(_HydroBlocks.WIND_MILL));
        info.EvaluateMatch(_HydroBlocks.PIPEV2);
        //need to get based on Match since -> by the time placing the state is still what is was before -> most likly air

        Direction dir = ctx.getPlayerLookDirection();

        //use this as looking direction when edge behaviour is wanted - otherwise comment it out
//        if(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite())).getBlock() == this.getDefaultState().getBlock()) {
//            dir = ctx.getSide().getOpposite();
//        }

        int amount = info.AmountOfCALPs();
        System.out.println(amount);

        if (amount == 1 || amount == 0) {
            //if i always set it could get priority over a real block which is not what i want
            info.SetLookingDirection(dir);
        }

        return info.GetCorrectState();
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING))); //not sure since i basicly ignore facing
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING))); //not sure since i basicly ignore facing
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PipeProperties.PIPE_ID, PipeProperties.PowerLevel);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return PipeV2.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        SurroundingPipesInfo info = new SurroundingPipesInfo(world, pos, Arrays.asList(_HydroBlocks.WIND_MILL));

        System.out.println(info.GetPipeConnectionState().toString() + " " + pos.toString());
        world.setBlockState(pos, info.GetCorrectState());
    }
}