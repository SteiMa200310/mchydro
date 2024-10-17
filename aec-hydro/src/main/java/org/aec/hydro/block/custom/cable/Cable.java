package org.aec.hydro.block.custom.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.custom.pipe.Pipe;
import org.aec.hydro.item.ModItemGroups;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.aec.hydro.utils.HudDataManager;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Cable extends Block {
    private static final org.aec.hydro.pipeHandling.core.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
            VoxelGenerator.makeCableLongShape_NORTH_SOUTH(),
            VoxelGenerator.makeCableEdgeShape_NORTH_EAST()
    );

    private static List<Block> PowerProviders = null;

    public Cable(Settings settings) {
        super(settings);

        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(PipeProperties.PIPE_ID, PipeID.F1)
                .with(PipeProperties.PowerLevel, 0)
                .with(PipeProperties.RecieverFace, PowerFlowDirection.NONE)
                .with(PipeProperties.ProviderFace, PowerFlowDirection.NONE)
        );

        PowerProviders = Arrays.asList(_HydroBlocks.WIND_MILL, _HydroBlocks.WATERWHEEL, _HydroBlocks.SOLAR_PANEL);
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
        return Cable.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        EnergyContext info = new EnergyContext(
            ctx.getWorld(),
            ctx.getBlockPos(),
            ContextType.Pipe,
            Cable.PowerProviders,
            _HydroBlocks.CABLECOMBINER,
            _HydroBlocks.CABLE
        );
        info.EvaluateBase(); //is air at start

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir); //needs to be evaluated previously

        return info.GetCorrectedState();
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        System.out.println(pos);

        EnergyContext info = new EnergyContext(
            world,
            pos,
            ContextType.Pipe,
            Cable.PowerProviders,
            _HydroBlocks.CABLECOMBINER,
            _HydroBlocks.CABLE
        );
        info.EvaluateActual();

        world.setBlockState(pos, info.GetCorrectedState());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem().equals(ModItemGroups.VOLTMETER)) {
            if (!state.getBlock().equals(_HydroBlocks.CABLE))
                return ActionResult.PASS;

            int powerLevel = state.get(PipeProperties.PowerLevel);
            PowerFlowDirection providerFace = state.get(PipeProperties.ProviderFace);
            PowerFlowDirection recieverFace = state.get(PipeProperties.RecieverFace);

            if (providerFace == PowerFlowDirection.NONE && recieverFace == PowerFlowDirection.NONE) {
                if (powerLevel == 0) {
                    HudDataManager.setPipeStatus("No Energy Flow");
                }

                if (powerLevel == 30) {
                    HudDataManager.setPipeStatus("Cable Error");
                }
                return ActionResult.SUCCESS;
            }

            if (!world.isClient) {
                HudDataManager.setPipeStatus(String.valueOf(powerLevel));
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            NbtList canPlaceOn = new NbtList();

            canPlaceOn.add(NbtString.of("minecraft:grass_block"));
            canPlaceOn.add(NbtString.of("hydro:cable"));
            canPlaceOn.add(NbtString.of("hydro:cablecombiner"));

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