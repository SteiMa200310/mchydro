package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block.custom.WindMill;
import org.aec.hydro.block.entity.PipeBlockEntity;
import org.aec.hydro.block.entity._HydroBlockEntities;
import org.aec.hydro.utils.*;
import org.aec.hydro.utils.PipeHandling.*;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.jetbrains.annotations.Nullable;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.HashSet;

public class Pipe extends BlockWithEntity  {
    private static final org.aec.hydro.utils.PipeHandling.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
        VoxelGenerator.makePipeV2LongShape_NORTH_SOUTH(),
        VoxelGenerator.makePipeV2EdgeShape_NORTH_EAST()
    );

    //cannot do that since static init can happen here prior to in HydroBlocks
//    private static final List<Block> PowerProvider = Arrays.asList(
//        _HydroBlocks.WIND_MILL
//    );

    public Pipe(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.PIPE_ID, PipeID.F1));
        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.PowerLevel, 0));
        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.RecieverFace, CustomDirection.NONE));
        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.ProviderFace, CustomDirection.NONE));
        this.setDefaultState(this.stateManager.getDefaultState().with(PipeProperties.IsProvider, false));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        PipeContext info = new PipeContext(ctx.getWorld(), ctx.getBlockPos(), ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL));
        info.EvaluateMatch(_HydroBlocks.PIPE);
        //need to get based on Match since -> by the time placing the state is still what is was before -> most likly air

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir); //if there are more than or equal to two then ignored anyways -> built what i had below on intuiten in the new one very nice

        //use this as looking direction when edge behaviour is wanted - otherwise comment it out
//        if(ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite())).getBlock() == this.getDefaultState().getBlock()) {
//            dir = ctx.getSide().getOpposite();
//        }

        return info.GetCorrectedState();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PipeProperties.PIPE_ID);
        builder.add(PipeProperties.PowerLevel);
        builder.add(PipeProperties.RecieverFace);
        builder.add(PipeProperties.ProviderFace);
        builder.add(PipeProperties.IsProvider);
        builder.add(Properties.FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Pipe.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        PipeContext info = new PipeContext(world, pos, ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL));
        world.setBlockState(pos, info.GetCorrectedState());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBlockEntity(pos,state);
    }

    // Called when the block is added to the world
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        if (!world.isClient) {
            PipeBlockEntity pipeEntity = (PipeBlockEntity) world.getBlockEntity(pos);
            if (pipeEntity != null) {
                // Perform the connection logic when the block is added
                pipeEntity.checkAndPropagatePower(world, pos);
            }
        }
    }

    // Called when the block is broken by a player or an event
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);

        if (!world.isClient) {
            PipeBlockEntity pipeEntity = (PipeBlockEntity) world.getBlockEntity(pos);
            if (pipeEntity != null) {
                // Remove power and propagate disconnection to any other connected pipes
                pipeEntity.removePowerFromDisconnectedPipes(world, pos, new HashSet<>());
            }
        }
    }

    // Called when the block state is replaced (including destruction)
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PipeBlockEntity) {
                PipeBlockEntity pipeEntity = (PipeBlockEntity) blockEntity;

                // Check if this pipe's removal disconnects any pipes from power
                pipeEntity.removePowerFromDisconnectedPipes(world, pos, new HashSet<>());
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    // Handle player interaction with the block (optional)
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            PipeBlockEntity pipeEntity = (PipeBlockEntity) world.getBlockEntity(pos);
            if (pipeEntity != null) {
                // Example: Display the current power level to the player
                player.sendMessage(Text.literal("Power Level: " + pipeEntity.getPowerLevel() + " || " + "Is Connected To Power " + pipeEntity.isConnectedToPowerSource(world, pos, new HashSet<>())), true);
            }
        }
        return ActionResult.SUCCESS;
    }
}