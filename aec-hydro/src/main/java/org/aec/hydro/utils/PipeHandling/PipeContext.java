package org.aec.hydro.utils.PipeHandling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;

public class PipeContext {
    private final World World;
    private final BlockPos Pos;
    private final ContextType PipeContextType;
    private boolean IsEvaluated = false;
    private Pair<CustomDirection, CustomDirection> FlowDirection = new Pair<>(CustomDirection.NONE, CustomDirection.NONE);

    private BlockState ToBeUsedBlockState = null;
    private PipeContext North = null;
    private PipeContext South = null;
    private PipeContext East = null;
    private PipeContext West = null;
    private PipeContext Up = null;
    private PipeContext Down = null;

    public PipeContext(World world, BlockPos pos, ContextType contextType) {
        World = world;
        Pos = pos;
        PipeContextType = contextType;
    }

    //Logic
    private BlockState GetCorrectedFacingState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        //prevents pipes that are already connected to still seek for new connections
        ContextConnectionState state = this.GetConnectionState();
        if (state.IsFull()) {
            //can also be just a triggered neighbor update where im prefectly fine
            return this.ToBeUsedBlockState.with(PipeProperties.PowerLevel, 10);
        }

        if(this.PipeContextType == ContextType.PowerProvider) return this.ToBeUsedBlockState;

        //BEGIN Priority Ifs ------------------- (null check unnessecary)
        //3
        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.West != null && PipeContext.CSH_INCW(this, Direction.WEST)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
        }

        if (this.Up != null && PipeContext.CSH_INCW(this, Direction.UP) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
        }

        //12
        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.East != null && PipeContext.CSH_INCW(this, Direction.EAST)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E1);
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E2);
        }

        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.West != null && PipeContext.CSH_INCW(this, Direction.WEST)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E3);
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E4);
        }


        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E5);
        }

        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E6);
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E7);
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E8);
        }


        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E9);
        }

        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E10);
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E11);
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.E12);
        }
        //END Priority Ifs -------------------

        if (this.North != null && this.ConnectedToContext(Direction.NORTH))
            return this.ToBeUsedBlockState;

        if (this.South != null && this.ConnectedToContext(Direction.SOUTH))
            return this.ToBeUsedBlockState;

        if (this.East != null && this.ConnectedToContext(Direction.EAST))
            return this.ToBeUsedBlockState;

        if (this.West != null && this.ConnectedToContext(Direction.WEST))
            return this.ToBeUsedBlockState;

        if (this.Up != null && this.ConnectedToContext(Direction.UP))
            return this.ToBeUsedBlockState;

        if (this.Down != null && this.ConnectedToContext(Direction.DOWN))
            return this.ToBeUsedBlockState;

        //if no actuals hit then look for looking direction
        for (Direction dir : Direction.values()) {
            PipeContext cur = this.GetContextBasedOnDirection(dir);
            if (cur != null && cur.PipeContextType == ContextType.FakeConnectie) {
                if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);
                }

                if (dir == Direction.EAST || dir == Direction.WEST) {
                    return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);
                }

                if (dir == Direction.UP || dir == Direction.DOWN) {
                    return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);
                }
            }
        }
        //here would be the logic with that one error to preserve one edge of the previously existing connection not sure if that would fit into the context logic tho
        if (this.North != null && this.North.PipeContextType == ContextType.Pipe || this.South != null && this.South.PipeContextType == ContextType.Pipe)
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F1);

        if (this.East != null && this.East.PipeContextType == ContextType.Pipe || this.West != null && this.West.PipeContextType == ContextType.Pipe)
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F2);

        if (this.Up != null && this.Up.PipeContextType == ContextType.Pipe || this.Down != null && this.Down.PipeContextType == ContextType.Pipe)
            return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, PipeID.F3);

        //backup
        return this.ToBeUsedBlockState.with(PipeProperties.ProviderFace, CustomDirection.NONE).with(PipeProperties.RecieverFace, CustomDirection.NONE);
    }

    public BlockState GetCorrectedState(){
        BlockState outState = GetCorrectedFacingState();

        if (!this.IsEvaluated)
            this.Evaluate();

        CalculateFlowDirection(outState);

        if(FlowDirection.getLeft() != CustomDirection.NONE && FlowDirection.getRight() != CustomDirection.NONE){
            outState = outState.with(PipeProperties.RecieverFace, FlowDirection.getLeft())
                    .with(PipeProperties.ProviderFace, FlowDirection.getRight())
                    .with(PipeProperties.PowerLevel, GetPowerLevelBasedOnDir(FlowDirection.getLeft()))
                    .with(PipeProperties.IsProvider, true);
        }

        return outState;
    }

    private void CalculateFlowDirection(BlockState state) {
        Pair<Direction, Direction> openFaces = GetOpenFaces(state);
        CustomDirection Left = DirectionToCustomDirection(openFaces.getLeft());
        CustomDirection Right = DirectionToCustomDirection(openFaces.getRight());

        PipeContext ctxLeft = GetContextBasedOnDirection(openFaces.getLeft());
        PipeContext ctxRight = GetContextBasedOnDirection(openFaces.getRight());

        //Can't be connected to two PowerProvider or Transmitter
        if(ctxLeft != null && ctxRight != null && (ctxLeft.IsPowerProvider() && ctxRight.IsPowerProvider() || (ctxLeft.IsPowerTransmitter() && ctxRight.IsPowerTransmitter()))){
            FlowDirection = new Pair<>(CustomDirection.NONE, CustomDirection.NONE);
            return;
        }
        if(ctxLeft != null && ctxRight != null && ctxLeft.IsPowerProvider() && !ctxRight.IsPowerProvider()) {
            FlowDirection = new Pair<>(Left, Right);
            return;
        }
        if(ctxLeft != null && ctxRight != null && !ctxLeft.IsPowerProvider() && ctxRight.IsPowerProvider()){
            FlowDirection = new Pair<>(Right, Left);
            return;
        }
        if(ctxLeft != null && ctxRight == null && (ctxLeft.IsPowerProvider() || ctxLeft.IsPowerTransmitter())){
            FlowDirection = new Pair<>(Left, Right);
            return;
        }
        if(ctxLeft == null && ctxRight != null && (ctxRight.IsPowerProvider() || ctxRight.IsPowerTransmitter())){
            FlowDirection = new Pair<>(Right, Left);
        }
    }

    public int GetPowerLevelBasedOnDir(CustomDirection dir){
        if(dir == null || dir == CustomDirection.NONE) return -1;

        PipeContext ctx = GetContextBasedOnDirection(CustomDirectionToDirection(dir));
        if(ctx == null) return -1;
        return ctx.GetActualBlockState().get(PipeProperties.PowerLevel);
    }

    public CustomDirection DirectionToCustomDirection(Direction dir){
        return switch (dir){
            case NORTH -> CustomDirection.NORTH;
            case EAST -> CustomDirection.EAST;
            case SOUTH -> CustomDirection.SOUTH;
            case WEST -> CustomDirection.WEST;
            case UP -> CustomDirection.UP;
            case DOWN -> CustomDirection.DOWN;
        };
    }

    public Direction CustomDirectionToDirection(CustomDirection dir){
        return switch (dir){
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case NONE -> null;
        };
    }

    //Evaluators
    public void Evaluate() {
        this.ToBeUsedBlockState = this.GetActualBlockState(); //based on actual block at pos in world

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, Direction.NORTH);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, Direction.SOUTH);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, Direction.EAST);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, Direction.WEST);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, Direction.UP);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, Direction.DOWN);

        this.IsEvaluated = true;
    }

    public void EvaluateMatch(Block block) {
        this.ToBeUsedBlockState = block.getDefaultState(); //based on dummy provided block

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, Direction.NORTH, block);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, Direction.SOUTH, block);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, Direction.EAST, block);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, Direction.WEST, block);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, Direction.UP, block);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, Direction.DOWN, block);

        this.IsEvaluated = true;
    }

    //Helper
    public ContextConnectionState GetConnectionState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        //check all pipe neighbors
        CustomDirection connectedDirection1 = null;
        CustomDirection connectedDirection2 = null;

        int sum = 0;
        for(Direction direction : Direction.values()) {
            if (ConnectedToContext(direction)) {
                sum++;

                if (connectedDirection1 == null)
                    connectedDirection1 = DirectionToCustomDirection(direction);
                else if (connectedDirection2 == null)
                    connectedDirection2 = DirectionToCustomDirection(direction);
                else
                    System.out.println("ERROR: both connected directions already set");
            }
        }

        /*
        PipeContext ctx1 = (connectedDirection1 == null) ? null : GetContextBasedOnDirection(CustomDirectionToDirection(connectedDirection1));
        PipeContext ctx2 = (connectedDirection2 == null) ? null : GetContextBasedOnDirection(CustomDirectionToDirection(connectedDirection2));
        if(ctx1 != null && ctx2 != null && ctx1.IsPowerProvider()) {
            return ContextConnectionState.GetFlowing(
                ctx1, connectedDirection1, ctx2, connectedDirection2
            );
        }
        if(ctx1 != null && ctx2 != null && ctx2.IsPowerProvider()) {
            return ContextConnectionState.GetFlowing(
                    ctx2, connectedDirection2, ctx1, connectedDirection1
            );
        }
        if(ctx1 != null && ctx2 == null && ctx1.IsPowerProvider()) {
            return ContextConnectionState.GetFlowing(
                    ctx1, connectedDirection1, null, CustomDirection.NONE
            );
        }
        if(ctx2 != null && ctx1 == null && ctx2.IsPowerProvider()) {
            return ContextConnectionState.GetFlowing(
                    ctx2, connectedDirection2, null, CustomDirection.NONE
            );
        }
        */
        if (sum == 0)
            return ContextConnectionState.GetNot();

        if (sum == 1) {
            return ContextConnectionState.GetOne(
                this.GetContextBasedOnDirection(CustomDirectionToDirection(connectedDirection1)),
                connectedDirection1
            );
        }

        if (sum == 2) {
            return ContextConnectionState.GetFull(
                this.GetContextBasedOnDirection(CustomDirectionToDirection(connectedDirection1)),
                connectedDirection1,
                this.GetContextBasedOnDirection(CustomDirectionToDirection(connectedDirection2)),
                connectedDirection2
            );
        }

        System.out.println("ERROR: connected to more than two pipes or power providers");
        return ContextConnectionState.GetNot();
    }

    public boolean IsPowerProvider(){
        return this.PipeContextType == ContextType.PowerProvider;
    }
    public boolean IsPowerTransmitter(){
        return this.PipeContextType == ContextType.PowerTransmitter;
    }

    public BlockState GetActualBlockState() {
        return this.World.getBlockState(this.Pos);
    }

    public Pair<Direction, Direction> GetOpenFaces() {
        if (this.ToBeUsedBlockState.contains(PipeProperties.PIPE_ID))
            return PipeContext.GetOpenFacesBasedOnPipeId(this.ToBeUsedBlockState.get(PipeProperties.PIPE_ID));

        System.out.println("ERROR: GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }
    public Pair<Direction, Direction> GetOpenFaces(BlockState state) {
        if (state.contains(PipeProperties.PIPE_ID))
            return PipeContext.GetOpenFacesBasedOnPipeId(state.get(PipeProperties.PIPE_ID));

        System.out.println("ERROR: GetOpenFaces called on non Pipe");
        return new Pair<>(null, null);
    }

    public Pair<CustomDirection, CustomDirection> GetFlowDirection() {
        if (this.PipeContextType == ContextType.Pipe) {
            CustomDirection dir1 = this.ToBeUsedBlockState.get(PipeProperties.RecieverFace);
            CustomDirection dir2 = this.ToBeUsedBlockState.get(PipeProperties.ProviderFace);
            return new Pair<>(dir1, dir2);
        }

        System.out.println("ERROR: GetFlowDirection called on non Pipe");
        return new Pair<>(null, null);
    }
    public int GetAmoutOfWillingNeighbors() {
        int sum = 0;
        for (Direction direction : Direction.values()) {
            if (PipeContext.CSH_INCW(this, direction)) sum++;
        }
        return sum;
    }
    public void SetFakeDirection(Direction direction) {
        if (!this.IsEvaluated)
            this.Evaluate();

        if (this.GetAmoutOfWillingNeighbors() >= 2)
            return;

        PipeContext ctx = this.GetContextBasedOnDirection(direction);
        if (ctx == null)
            this.SetContextBasedOnDirection(
                    direction,
                    new PipeContext(World, this.Pos.offset(direction), ContextType.FakeConnectie)
            );

        //note that this function has lower priority than if an actual neighbor is detected so its only taken into account if there is not already a neighbor there
        //and if there are less than two neighbors
    }
    public boolean ConnectedToContext(Direction direction) {
        Pair<Direction, Direction> openFaces = this.GetOpenFaces();
        if (openFaces.getLeft() != direction && openFaces.getRight() != direction)
            return false;

        PipeContext neighborInfo = this.GetContextBasedOnDirection(direction);

        if (neighborInfo == null || neighborInfo.PipeContextType == ContextType.FakeConnectie) return false; //cannot be connected to FakeConnectie

        if (!neighborInfo.IsEvaluated)
            neighborInfo.Evaluate(); //does eveluate by the actual block type which is why i then can request properties on demand

        if (neighborInfo.PipeContextType == ContextType.Pipe) {
            Pair<Direction, Direction> neighborOpenFaces = neighborInfo.GetOpenFaces();

            return  direction == neighborOpenFaces.getLeft().getOpposite() || //opposit obviously only relevant for edge pieces
                    direction == neighborOpenFaces.getRight().getOpposite();
        }

        if (neighborInfo.PipeContextType == ContextType.PowerProvider) {
            Direction neighborOpenFace = neighborInfo.ToBeUsedBlockState.get(Properties.FACING);

            return direction == neighborOpenFace.getOpposite();
        }

        return false;
    }

    public PipeContext GetContextBasedOnDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> this.North;
            case SOUTH -> this.South;
            case EAST -> this.East;
            case WEST -> this.West;
            case UP -> this.Up;
            case DOWN -> this.Down;
        };
    }
    public void SetContextBasedOnDirection(Direction direction, PipeContext context) {
        switch (direction) {
            case NORTH -> this.North = context;
            case SOUTH -> this.South = context;
            case EAST -> this.East = context;
            case WEST -> this.West = context;
            case UP -> this.Up = context;
            case DOWN -> this.Down = context;
        };
    }

    //Statics
    public static Pair<Direction, Direction> GetOpenFacesBasedOnPipeId(PipeID pipeId) {
        return switch (pipeId) {
            case F1 -> new Pair<>(Direction.NORTH, Direction.SOUTH);
            case F2 -> new Pair<>(Direction.EAST, Direction.WEST);
            case F3 -> new Pair<>(Direction.UP, Direction.DOWN);

            case E1 -> new Pair<>(Direction.NORTH, Direction.EAST);
            case E2 -> new Pair<>(Direction.EAST, Direction.SOUTH);
            case E3 -> new Pair<>(Direction.SOUTH, Direction.WEST);
            case E4 -> new Pair<>(Direction.WEST, Direction.NORTH);

            case E5 -> new Pair<>(Direction.NORTH, Direction.UP);
            case E6 -> new Pair<>(Direction.NORTH, Direction.DOWN);
            case E7 -> new Pair<>(Direction.EAST, Direction.UP);
            case E8 -> new Pair<>(Direction.EAST, Direction.DOWN);

            case E9 -> new Pair<>(Direction.SOUTH, Direction.UP);
            case E10 -> new Pair<>(Direction.SOUTH, Direction.DOWN);
            case E11 -> new Pair<>(Direction.WEST, Direction.UP);
            case E12 -> new Pair<>(Direction.WEST, Direction.DOWN);
        };
    }
    public static PipeContext GetContextInDir(World world, BlockPos pos, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        if (neighborBlock.equals(_HydroBlocks.PIPE) && neighborBlockState.contains(PipeProperties.IsProvider) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerTransmitter);

        if (neighborBlockState.contains(PipeProperties.IsProvider) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider);

        if (neighborBlock.equals(_HydroBlocks.PIPE))
            return new PipeContext(world, neighborBlockPos, ContextType.Pipe);

        return null;
    }

    public static PipeContext GetContextInDir(World world, BlockPos pos, Direction direction, Block block) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        if (neighborBlock.equals(block) && neighborBlockState.contains(PipeProperties.IsProvider) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerTransmitter);

        if (neighborBlockState.contains(PipeProperties.IsProvider) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider);

        if (neighborBlock.equals(_HydroBlocks.PIPE))
            return new PipeContext(world, neighborBlockPos, ContextType.Pipe);

        return null;
    }

    //Statics - CSH (Corrected State Helper)
    //IsNeighbourConnectionWilling
//    public static boolean CSH_INCW(PipeContext self, Direction dir1, Direction dir2) {
//        PipeContext ctx1 = self.GetContextBasedOnDirection(dir1);
//        PipeContext ctx2 = self.GetContextBasedOnDirection(dir2);
//
//        return  ctx1 != null || self.ConnectedToContext(dir1) &&
//                ctx2 != null || self.ConnectedToContext(dir2);
//    }
    public static boolean CSH_INCW(PipeContext self, Direction dir1) {
        PipeContext ctx = self.GetContextBasedOnDirection(dir1);

        if (ctx == null)
            return false;

        if (ctx.PipeContextType == ContextType.FakeConnectie)
            return true;

        return !ctx.GetConnectionState().IsFull() || self.ConnectedToContext(dir1);
    }
}