package org.aec.hydro.block;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
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
import org.aec.hydro.utils.*;
import org.aec.hydro.utils.PipeHandling.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Pipe extends HorizontalFacingBlock {
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
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        PipeContext info = new PipeContext(ctx.getWorld(), ctx.getBlockPos(), ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), _HydroBlocks.PIPE);
        info.EvaluateJustPlaced();
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

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        PipeContext info = new PipeContext(world, pos, ContextType.Pipe, Arrays.asList(_HydroBlocks.WIND_MILL), _HydroBlocks.PIPE);
        world.setBlockState(pos, info.GetCorrectedState());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        player.sendMessage(Text.of("Power Level: " + world.getBlockState(pos).get(PipeProperties.PowerLevel) + " || " + world.getBlockState(pos).get(PipeProperties.PIPE_ID)), true);

        return super.onUse(state, world, pos, player, hand, hit);
    }
}