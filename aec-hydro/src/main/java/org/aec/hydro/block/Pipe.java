package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.utils.*;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

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

public class Pipe extends HorizontalFacingBlock {
    private static final org.aec.hydro.pipeHandling.core.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
        VoxelGenerator.makePipeV2LongShape_NORTH_SOUTH(),
        VoxelGenerator.makePipeV2EdgeShape_NORTH_EAST()
    );

    public Pipe(Settings settings) {
        super(settings);

        this.setDefaultState(
            this.stateManager.getDefaultState()
                .with(PipeProperties.PIPE_ID, PipeID.F1)
                .with(PipeProperties.PowerLevel, 0)
                .with(PipeProperties.RecieverFace, PowerFlowDirection.NONE)
                .with(PipeProperties.ProviderFace, PowerFlowDirection.NONE)
        );
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
        return Pipe.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        EnergyContext info = new EnergyContext(ctx.getWorld(), ctx.getBlockPos(), ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), _HydroBlocks.PIPE);
        info.EvaluateBase(); //is air at start

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir);

        return info.GetCorrectedState();
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        EnergyContext info = new EnergyContext(world, pos, ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), _HydroBlocks.PIPE);
        world.setBlockState(pos, info.GetCorrectedState());
    }
}