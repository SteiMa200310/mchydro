package org.aec.hydro.block.custom.geo;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.ModelPart;
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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.entity.BaggerBlockEntity;
import org.aec.hydro.block.entity.SolarPanelBlockEntity;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Bagger extends BlockWithEntity {
    private static final VoxelShape NORTH_SHAPE = VoxelGenerator.makeBaggerShape();
    private static final VoxelShape EAST_SHAPE = VoxelGenerator.rotateShape(0,1,0, NORTH_SHAPE);
    private static final VoxelShape SOUTH_SHAPE = VoxelGenerator.rotateShape(0,2,0, NORTH_SHAPE);
    private static final VoxelShape WEST_SHAPE = VoxelGenerator.rotateShape(0,3,0, NORTH_SHAPE);

    public Bagger(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BaggerBlockEntity(pos, state);
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
    }
}
