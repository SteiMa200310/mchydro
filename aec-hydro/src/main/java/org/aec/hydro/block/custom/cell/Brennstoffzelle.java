package org.aec.hydro.block.custom.cell;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.custom.pipe.PipeCorner;
import org.aec.hydro.pipeHandling.core.EnergyContext;
import org.aec.hydro.pipeHandling.core.PipeShapeWrapper;
import org.aec.hydro.pipeHandling.utils.ContextType;
import org.aec.hydro.pipeHandling.utils.PipeID;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.pipeHandling.utils.PowerFlowDirection;
import org.aec.hydro.utils.VoxelGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Brennstoffzelle extends HorizontalFacingBlock {
    private static final org.aec.hydro.pipeHandling.core.PipeShapeWrapper PipeShapeWrapper = new PipeShapeWrapper(
            VoxelGenerator.makePipeV2LongShape_NORTH_SOUTH(),
            VoxelGenerator.makePipeV2EdgeShape_NORTH_EAST()
    );

    public Brennstoffzelle(AbstractBlock.Settings settings) {
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
        return Brennstoffzelle.PipeShapeWrapper.GetShape(state.get(PipeProperties.PIPE_ID));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        EnergyContext info = new EnergyContext(ctx.getWorld(), ctx.getBlockPos(), ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), null, _HydroBlocks.BRENNSTOFFZELLE);
        info.EvaluateBase(); //is air at start

        Direction dir = ctx.getPlayerLookDirection().getOpposite();
        info.SetFakeDirection(dir); //needs to be evaluated previously

        return info.GetCorrectedState();
    }

//    @Override
//    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
//        EnergyContext info = new EnergyContext(world, pos, ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), null, _HydroBlocks.BRENNSTOFFZELLE);
//        info.EvaluateActual();
//
//        world.setBlockState(pos, info.GetCorrectedState());
//    }
}
