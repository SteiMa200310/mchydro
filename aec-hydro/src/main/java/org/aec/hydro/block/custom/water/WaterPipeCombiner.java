package org.aec.hydro.block.custom.water;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.AECHydro;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class WaterPipeCombiner extends Block {
    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.makePipeCombinerShape();
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(0,2,0,NORTH_SHAPE);
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,1,0,NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,3,0,NORTH_SHAPE);
    private static final VoxelShape UP_SHAPE = VoxelGenerator.rotateShape(1,0,0,NORTH_SHAPE);
    private static final VoxelShape DOWN_SHAPE = VoxelGenerator.rotateShape(3,0,0,NORTH_SHAPE);

    public WaterPipeCombiner(Settings settings) {
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
        return this.getDefaultState().with(Properties.FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        //trigger neighbor update on all neighbor blocks even if self did not change
        try {
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.offset(dir);
                BlockState neighborState = world.getBlockState(neighborPos);
                //            if (neighborState.getBlock().equals(_HydroBlocks.WATERPIPE)) {
                //                neighborState.neighborUpdate(world, neighborPos, state.getBlock(), pos, notify);
                //            }

                if (neighborPos.getX() == fromPos.getX() && neighborPos.getZ() == fromPos.getZ() && neighborPos.getY() == fromPos.getY())
                    return;

                if (neighborState.getBlock() != _HydroBlocks.WATERPIPE && neighborState.getBlock() != _HydroBlocks.WATERPIPECOMBINER)
                    continue;

                neighborState.neighborUpdate(world, neighborPos, state.getBlock(), pos, notify);
            }
        } catch (StackOverflowError e) {
            AECHydro.LOGGER.error("StackOverflowError: " + e.getMessage());
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:grass_block"));
            canPlaceOn.add(NbtString.of("hydro:pipew"));
            canPlaceOn.add(NbtString.of("hydro:pipewcombiner"));

            // Create an ItemStack of the block (the item form of the block)
            ItemStack itemStack = new ItemStack(this);

            itemStack.getOrCreateNbt().put("CanPlaceOn", canPlaceOn);

            // Drop the item stack (with NBT data) when the block is broken
            Block.dropStack(world, pos, itemStack);
        }

        // Call the super method to handle normal breaking logic
        super.onBreak(world, pos, state, player);
    }
}