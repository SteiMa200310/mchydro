package org.aec.hydro.utils.PipeHandling;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block._HydroBlocks;

import java.util.List;

public class PipeContext {
    private final World World;
    private final BlockPos Pos;
    private final List<Block> PowerProviders;
    private final ContextType PipeContextType;
    private boolean IsEvaluated = false;
    private PipeID ID;
    private Pair<CustomDirection, CustomDirection> FlowDirection;

    private BlockState ToBeUsedBlockState = null;
    private PipeContext North = null;
    private PipeContext South = null;
    private PipeContext East = null;
    private PipeContext West = null;
    private PipeContext Up = null;
    private PipeContext Down = null;

    public PipeContext(World world, BlockPos pos, ContextType contextType, List<Block> powerProviders) {
        World = world;
        Pos = pos;
        PowerProviders = powerProviders;
        PipeContextType = contextType;
    }

    //Logic
    public BlockState GetCorrectedState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        //prevents pipes that are already connected to still seek for new connections
        ContextConnectionState state = this.GetConnectionState();

        if (state.IsFull()) {
            //can also be just a triggered neighbor update where im prefectly fine
            return this.ToBeUsedBlockState;
        }

        ID = getPipeID();

        if(this.PipeContextType == ContextType.PowerProvider) return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, ID);

        if(ID != null){
            this.ToBeUsedBlockState = CalculateFlowDirection(GetOpenFacesBasedOnPipeId(ID));
            int pwLvL = GetPowerLevelBasedOnRecieverFace();
            if(pwLvL != -1) {
                return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, ID).with(PipeProperties.PowerLevel, pwLvL).with(PipeProperties.IsProvider, true);
            } else {
                return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, ID).with(PipeProperties.PowerLevel, 10);
            }
        }

        //backup
        return this.ToBeUsedBlockState.with(PipeProperties.PIPE_ID, null).with(PipeProperties.ProviderFace, CustomDirection.NONE).with(PipeProperties.RecieverFace, CustomDirection.NONE).with(PipeProperties.IsProvider, false);
    }

    public int GetPowerLevelBasedOnRecieverFace(){
        Direction dir = CustomDirectionToDirection(this.ToBeUsedBlockState.get(PipeProperties.RecieverFace));
        if(dir ==null) return -1;

        PipeContext ctx = GetContextBasedOnDirection(dir);
        if(ctx == null) return -1;
        return ctx.GetActualBlockState().get(PipeProperties.PowerLevel);
    }

    public BlockState CalculateFlowDirection(Pair<Direction, Direction> openFaces){
        PipeContext dir1ctx = GetContextBasedOnDirection(openFaces.getLeft());
        PipeContext dir2ctx = GetContextBasedOnDirection(openFaces.getRight());

        if(dir1ctx != null && dir1ctx.PipeContextType == ContextType.PowerProvider) {
            return this.ToBeUsedBlockState.with(PipeProperties.RecieverFace, DirectionToCustomDirection(openFaces.getLeft())).with(PipeProperties.ProviderFace, DirectionToCustomDirection(openFaces.getRight()));
        }

        if(dir2ctx != null && dir2ctx.PipeContextType == ContextType.PowerProvider) {
            return this.ToBeUsedBlockState.with(PipeProperties.ProviderFace, DirectionToCustomDirection(openFaces.getLeft())).with(PipeProperties.RecieverFace, DirectionToCustomDirection(openFaces.getRight()));
        }

        return this.ToBeUsedBlockState.with(PipeProperties.RecieverFace, CustomDirection.NONE).with(PipeProperties.ProviderFace, CustomDirection.NONE);
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

    public PipeID getPipeID(){
        //BEGIN Priority Ifs ------------------- (null check unnessecary)
        //3
        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH)) {
            return PipeID.F1;
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.West != null && PipeContext.CSH_INCW(this, Direction.WEST)) {
            return PipeID.F2;
        }

        if (this.Up != null && PipeContext.CSH_INCW(this, Direction.UP) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return PipeID.F3;
        }

        //12
        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.East != null && PipeContext.CSH_INCW(this, Direction.EAST)) {
            return PipeID.E1;
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH)) {
            return PipeID.E2;
        }

        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.West != null && PipeContext.CSH_INCW(this, Direction.WEST)) {
            return PipeID.E3;
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH)) {
            return PipeID.E4;
        }


        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return PipeID.E5;
        }

        if (this.North != null && PipeContext.CSH_INCW(this, Direction.NORTH) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return PipeID.E6;
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return PipeID.E7;
        }

        if (this.East != null && PipeContext.CSH_INCW(this, Direction.EAST) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return PipeID.E8;
        }


        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return PipeID.E9;
        }

        if (this.South != null && PipeContext.CSH_INCW(this, Direction.SOUTH) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return PipeID.E10;
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.Up != null && PipeContext.CSH_INCW(this, Direction.UP)) {
            return PipeID.E11;
        }

        if (this.West != null && PipeContext.CSH_INCW(this, Direction.WEST) && this.Down != null && PipeContext.CSH_INCW(this, Direction.DOWN)) {
            return PipeID.E12;
        }
        //END Priority Ifs -------------------

        if (this.North != null && this.ConnectedToContext(Direction.NORTH))
            return null;

        if (this.South != null && this.ConnectedToContext(Direction.SOUTH))
            return null;

        if (this.East != null && this.ConnectedToContext(Direction.EAST))
            return null;

        if (this.West != null && this.ConnectedToContext(Direction.WEST))
            return null;

        if (this.Up != null && this.ConnectedToContext(Direction.UP))
            return null;

        if (this.Down != null && this.ConnectedToContext(Direction.DOWN))
            return null;

        //if no actuals hit then look for looking direction
        for (Direction dir : Direction.values()) {
            PipeContext cur = this.GetContextBasedOnDirection(dir);
            if (cur != null && cur.PipeContextType == ContextType.FakeConnectie) {
                if (dir == Direction.NORTH || dir == Direction.SOUTH) {
                    return PipeID.F1;
                }

                if (dir == Direction.EAST || dir == Direction.WEST) {
                    return PipeID.F2;
                }

                if (dir == Direction.UP || dir == Direction.DOWN) {
                    return PipeID.F3;
                }
            }
        }

        //here would be the logic with that one error to preserve one edge of the previously existing connection not sure if that would fit into the context logic tho
        if (this.North != null && this.North.PipeContextType == ContextType.Pipe || this.South != null && this.South.PipeContextType == ContextType.Pipe)
            return PipeID.F1;

        if (this.East != null && this.East.PipeContextType == ContextType.Pipe || this.West != null && this.West.PipeContextType == ContextType.Pipe)
            return PipeID.F2;

        if (this.Up != null && this.Up.PipeContextType == ContextType.Pipe || this.Down != null && this.Down.PipeContextType == ContextType.Pipe)
            return PipeID.F3;

        return null;
    }

    //Evaluators
    public void Evaluate() {
        this.ToBeUsedBlockState = this.GetActualBlockState(); //based on actual block at pos in worl

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN);

        this.IsEvaluated = true;
    }
    public void EvaluateMatch(Block block) {
        this.ToBeUsedBlockState = block.getDefaultState(); //based on dummy provided block

        this.North = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.NORTH, block);
        this.South = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.SOUTH, block);
        this.East = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.EAST, block);
        this.West = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.WEST, block);
        this.Up = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.UP, block);
        this.Down = PipeContext.GetContextInDir(this.World, this.Pos, this.PowerProviders, Direction.DOWN, block);

        this.IsEvaluated = true;
    }

    //Helper
    public ContextConnectionState GetConnectionState() {
        if (!this.IsEvaluated)
            this.Evaluate();

        //check all pipe neighbors
        Direction connectedDirection1 = null;
        Direction connectedDirection2 = null;

        int sum = 0;
        for(Direction direction : Direction.values()) {
            if (this.ConnectedToContext(direction)) {
                sum++;

                if (connectedDirection1 == null)
                    connectedDirection1 = Direction.NORTH;
                else if (connectedDirection2 == null)
                    connectedDirection2 = Direction.NORTH;
                else
                    System.out.println("ERROR: both connected directions already set");
            }
        }

        if (sum == 0)
            return ContextConnectionState.GetNot();

        if (sum == 1) {
            return ContextConnectionState.GetOne(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1
            );
        }

        if (sum == 2) {
            return ContextConnectionState.GetFull(
                this.GetContextBasedOnDirection(connectedDirection1),
                connectedDirection1,
                this.GetContextBasedOnDirection(connectedDirection2),
                connectedDirection2
            );
        }

        System.out.println("ERROR: connected to more than two pipes or power providers");
        return ContextConnectionState.GetNot();
    }
    public BlockState GetActualBlockState() {
        return this.World.getBlockState(this.Pos);
    }

    public Pair<Direction, Direction> GetOpenFaces() {
        if (this.PipeContextType == ContextType.Pipe)
            return PipeContext.GetOpenFacesBasedOnPipeId(this.ToBeUsedBlockState.get(PipeProperties.PIPE_ID));

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
    public int GetPowerLevel() {
        if (this.PipeContextType == ContextType.Pipe) {
            return this.ToBeUsedBlockState.get(PipeProperties.PowerLevel);
        }

        System.out.println("ERROR: GetPowerLevel called on non Pipe");
        return -1;
    }
    public int GetAmoutOfContextNeighbors() {
        int sum = 0;
        sum += this.North != null ? 1 : 0;
        sum += this.South != null ? 1 : 0;
        sum += this.East != null ? 1 : 0;
        sum += this.West != null ? 1 : 0;
        sum += this.Up != null ? 1 : 0;
        sum += this.Down != null ? 1 : 0;

        return sum;
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
                    new PipeContext(World, this.Pos.offset(direction), ContextType.FakeConnectie, PowerProviders)
            );

        //note that this function has lower priority than if an actual neighbor is detected so its only taken into account if there is not already a neighbor there
        //and if there are less than two neighbors
    }
    public boolean ConnectedToContext(Direction direction) {
        Pair<Direction, Direction> openFaces = this.GetOpenFaces();
        if (openFaces.getLeft() != direction && openFaces.getRight() != direction)
            return false;

        PipeContext neighborInfo = this.GetContextBasedOnDirection(direction);
        if (neighborInfo != null && neighborInfo.PipeContextType != ContextType.FakeConnectie) { //cannot be connected to FakeConnectie
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
    public static PipeContext GetContextInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction) {
        BlockPos neighborBlockPos = pos.offset(direction);

        BlockState blockState = world.getBlockState(pos);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);

        Block block = blockState.getBlock();
        Block neighborBlock = neighborBlockState.getBlock();

        if ((neighborBlock.equals(_HydroBlocks.PIPE) || neighborBlock.equals(_HydroBlocks.WIND_MILL)) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider,powerProviders);

        if (neighborBlock.equals(_HydroBlocks.PIPE))
            return new PipeContext(world, neighborBlockPos, ContextType.Pipe, powerProviders);

        return null;
    }
    public static PipeContext GetContextInDir(World world, BlockPos pos, List<Block> powerProviders, Direction direction, Block block) {
        BlockPos neighborBlockPos = pos.offset(direction);
        BlockState neighborBlockState = world.getBlockState(neighborBlockPos);
        Block neighborBlock = neighborBlockState.getBlock();

        if ((neighborBlock.equals(_HydroBlocks.PIPE) || neighborBlock.equals(_HydroBlocks.WIND_MILL)) && neighborBlockState.get(PipeProperties.IsProvider))
            return new PipeContext(world, neighborBlockPos, ContextType.PowerProvider, powerProviders);

        if (neighborBlock.equals(_HydroBlocks.PIPE))
            return new PipeContext(world, neighborBlockPos, ContextType.Pipe, powerProviders);

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