package org.aec.hydro.block.custom.geo;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.entity.WaterwheelBlockEntity;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Waterwheel extends BlockWithEntity {
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.makeWaterwheelShape(); //base is south wrong export just like pump

    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.rotateShape(0,2,0, SOUTH_SHAPE);
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,3,0, SOUTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,1,0, SOUTH_SHAPE);

    public Waterwheel(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(PipeProperties.PowerLevel, 1)
        );
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WaterwheelBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(Properties.FACING)) {
            case DOWN, NORTH, UP -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    //Properties
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.FACING);
        builder.add(PipeProperties.PowerLevel);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        assert placer != null;
        int radius = 5;
        Set<Block> powerProvider = new HashSet<>();
        powerProvider.add(_HydroBlocks.WIND_MILL);
        powerProvider.add(_HydroBlocks.WATERWHEEL);
        powerProvider.add(_HydroBlocks.SOLAR_PANEL);

        if(isSameBlockInRadius(world, pos, radius, powerProvider)){
            //NbtList canPlaceOn = new NbtList();

            //canPlaceOn.add(NbtString.of("minecraft:oak_log"));

            // Create an ItemStack of the block (the item form of the block)
            //ItemStack solarItemStack = new ItemStack(this);

            //itemStack.getOrCreateNbt().put("CanPlaceOn", canPlaceOn);

            //placer.setStackInHand(Hand.MAIN_HAND, solarItemStack);
            placer.getMainHandStack().increment(1);
            world.removeBlock(pos, false);
        }
    }

    private boolean isSameBlockInRadius(World world, BlockPos pos, int radius, Set<Block> blocksToCheck) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = pos.add(x, y, z);

                    // Skip the original block position
                    if (checkPos.equals(pos)) {
                        continue;
                    }

                    // Get the block state at the checked position
                    BlockState nearbyBlockState = world.getBlockState(checkPos);

                    // Check if the block is one of the blocks we're looking for
                    if (blocksToCheck.contains(nearbyBlockState.getBlock())) {
                        return true; // Found a matching block
                    }
                }
            }
        }
        return false; // No matching block found in the radius
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:oak_log"));

            // Create an ItemStack of the block (the item form of the block)
            ItemStack solarItemStack = new ItemStack(this);

            solarItemStack.getOrCreateNbt().put("CanPlaceOn", canPlaceOn);

            // Drop the item stack (with NBT data) when the block is broken
            Block.dropStack(world, pos, solarItemStack);
        }

        // Call the super method to handle normal breaking logic
        super.onBreak(world, pos, state, player);
    }
}
