package org.aec.hydro.block.custom.pipe;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
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
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.custom.cable.Cable;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.*;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

//cannot do that since static init can happen here prior to in HydroBlocks
//        private static final List<Block> PowerProvider = Arrays.asList(
//            _HydroBlocks.WIND_MILL
//        );

//use this as looking direction when edge behaviour is wanted - otherwise comment it out
//        if(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite())).getBlock() == this.getDefaultState().getBlock()) {
//            dir = ctx.getSide().getOpposite();
//        }

//In onUse override
//        player.sendMessage(Text.of("Power Level: " + world.getBlockState(pos).get(PipeProperties.PowerLevel) + " || " + world.getBlockState(pos).get(PipeProperties.PIPE_ID)), true);

public class Pipe extends Block {
    private static final org.aec.hydro.pipeHandling.core.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
        VoxelGenerator.makePipeLongShape_NORTH_SOUTH(),
        VoxelGenerator.makePipeEdgeShape_NORTH_EAST()
    );

    private static List<Block> PowerProviders = null;

    public Pipe(Settings settings) {
        super(settings);

        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(PipeProperties.PIPE_ID, PipeID.F1)
                .with(PipeProperties.PowerLevel, 0)
                .with(PipeProperties.RecieverFace, PowerFlowDirection.NONE)
                .with(PipeProperties.ProviderFace, PowerFlowDirection.NONE)
                .with(Properties.WATERLOGGED, false)
        );

        PowerProviders = Arrays.asList(_HydroBlocks.PUMP);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.WATERLOGGED);
        builder.add(PipeProperties.PIPE_ID);
        builder.add(PipeProperties.PowerLevel);
        builder.add(PipeProperties.RecieverFace);
        builder.add(PipeProperties.ProviderFace);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Pipe.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

        EnergyContext info = new EnergyContext(
            ctx.getWorld(),
            ctx.getBlockPos(),
            ContextType.Pipe,
            Pipe.PowerProviders,
            _HydroBlocks.PIPECOMBINER,
            _HydroBlocks.PIPE
        );
        info.EvaluateBase(); //is air at start

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir); //needs to be evaluated previously

        return info.GetCorrectedState()
            .with(Properties.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        EnergyContext info = new EnergyContext(
            world,
            pos,
            ContextType.Pipe,
            Pipe.PowerProviders,
            _HydroBlocks.PIPECOMBINER,
            _HydroBlocks.PIPE
        );
        info.EvaluateActual();

        world.setBlockState(pos, info.GetCorrectedState());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.get(Properties.WATERLOGGED) && !state.isOf(newState.getBlock())) {
            world.setBlockState(pos, Blocks.WATER.getDefaultState(), 3);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:grass_block"));
            canPlaceOn.add(NbtString.of("hydro:pipe"));
            canPlaceOn.add(NbtString.of("hydro:pipecombiner"));

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