package org.aec.hydro.block.custom.hydrogen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
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
import org.aec.hydro.block.custom.water.WaterPipe;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class HydrogenPipe extends Block {
    private static final org.aec.hydro.pipeHandling.core.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
            VoxelGenerator.makePipeLongShape_NORTH_SOUTH(),
            VoxelGenerator.makePipeEdgeShape_NORTH_EAST()
    );

    private static List<Block> PowerProviders = null;

    public HydrogenPipe(Settings settings) {
        super(settings);

        this.setDefaultState(
                this.stateManager.getDefaultState()
                        .with(PipeProperties.PIPE_ID, PipeID.F1)
                        .with(PipeProperties.PowerLevel, 0)
                        .with(PipeProperties.RecieverFace, PowerFlowDirection.NONE)
                        .with(PipeProperties.ProviderFace, PowerFlowDirection.NONE)
        );

        PowerProviders = Arrays.asList();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PipeProperties.PIPE_ID);
        builder.add(PipeProperties.PowerLevel);
        builder.add(PipeProperties.RecieverFace);
        builder.add(PipeProperties.ProviderFace);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return HydrogenPipe.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        EnergyContext info = MakeContext(ctx.getWorld(), ctx.getBlockPos(), ContextType.Pipe);
        info.EvaluateBase(); //is air at start

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir); //needs to be evaluated previously

        return info.GetCorrectedState();
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        EnergyContext info = MakeContext(world, pos, ContextType.Pipe);
        info.EvaluateActual();

        world.setBlockState(pos, info.GetCorrectedState());
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:grass_block"));
            canPlaceOn.add(NbtString.of("hydro:elektrolyseur"));
            canPlaceOn.add(NbtString.of("hydro:pipeh"));

            // Create an ItemStack of the block (the item form of the block)
            ItemStack itemStack = new ItemStack(this);

            itemStack.getOrCreateNbt().put("CanPlaceOn", canPlaceOn);

            // Drop the item stack (with NBT data) when the block is broken
            Block.dropStack(world, pos, itemStack);
        }

        // Call the super method to handle normal breaking logic
        super.onBreak(world, pos, state, player);
    }

    public static EnergyContext MakeContext(World world, BlockPos pos, ContextType contextType) {
        return new EnergyContext(
            world,
            pos,
            contextType,
            HydrogenPipe.PowerProviders,
            null,
            _HydroBlocks.HYDROGENPIPE,
            3
        );
    }
}