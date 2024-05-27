package org.aec.hydro.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.Hopper;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PipeV2 extends HorizontalFacingBlock {
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
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            Direction facing = state.get(FACING);
            BlockPos neighbor1 = pos.offset(facing.rotateYClockwise());
            BlockPos neighbor2 = pos.offset(facing.rotateYCounterclockwise());
            BlockState neighborState1 = world.getBlockState(neighbor1);
            BlockState neighborState2 = world.getBlockState(neighbor2);
            if (neighborState1.getBlock() == this && neighborState2.getBlock() == this) {
                // Replace this block with your desired block when placed between two other PipeV2 blocks
                world.setBlockState(pos, Blocks.STONE.getDefaultState());
            }
        }
    }
}
