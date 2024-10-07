package org.aec.hydro.block.custom.geo;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block.entity.SolarPanelBlockEntity;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

public class SolarPanel extends BlockWithEntity {
    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.makeSolarPanelShape();
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,1,0, NORTH_SHAPE);
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(0,2,0, NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,3,0, NORTH_SHAPE);

    public SolarPanel(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(PipeProperties.PowerLevel, 1)
        );
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    //Voxelshape
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
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:grass_block"));

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
