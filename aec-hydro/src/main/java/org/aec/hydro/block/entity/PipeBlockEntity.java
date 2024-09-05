package org.aec.hydro.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.block.Pipe;
import org.aec.hydro.block._HydroBlocks;
import org.aec.hydro.block.custom.WindMill;

import java.util.HashSet;
import java.util.Set;

public class PipeBlockEntity extends BlockEntity {
    private int powerLevel = 0;

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(_HydroBlockEntities.PIPE_BLOCK_ENTITY, pos, state);
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
        markDirty();
    }

    // Called to propagate power when a pipe is connected or when the world is loaded
    public void checkAndPropagatePower(World world, BlockPos pos) {
        if (isConnectedToPower(world, pos)) {
            int powerLevel = getPowerFromNeighbors(world, pos);
            setPowerLevel(powerLevel);
            propagatePower(world, pos, powerLevel);
        } else {
            setPowerLevel(0); // No power source connected
        }
    }

    // Propagate power to connected pipes
    public void propagatePower(World world, BlockPos pos, int powerLevel) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

            if (neighborEntity instanceof PipeBlockEntity) {
                PipeBlockEntity pipeEntity = (PipeBlockEntity) neighborEntity;
                if (pipeEntity.getPowerLevel() < powerLevel) {
                    pipeEntity.setPowerLevel(powerLevel);
                    pipeEntity.propagatePower(world, neighborPos, powerLevel);
                }
            }
        }
    }

    // Remove power from pipes that are no longer connected to a power source
    public void removePowerFromDisconnectedPipes(World world, BlockPos pos, Set<BlockPos> visited) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

            if (neighborEntity instanceof PipeBlockEntity pipeEntity) {
                if (!pipeEntity.isConnectedToPowerSource(world, neighborPos, visited)) {
                    System.out.println("Disconnected From powersource");
                    setPowerLevel(0);
                    visited.add(pos);  // Mark as visited to prevent loops
                    if (!visited.contains(neighborPos)) {
                        pipeEntity.removePowerFromDisconnectedPipes(world, neighborPos, visited);
                    }
                }
            }
        }
    }

    // Helper method to check if a pipe is connected to a power source or another powered pipe
    public boolean isConnectedToPower(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

            if (neighborEntity instanceof PipeBlockEntity) {
                PipeBlockEntity pipeEntity = (PipeBlockEntity) neighborEntity;
                if (pipeEntity.getPowerLevel() > 0) {
                    return true;  // Connected to a powered pipe
                }
            } else if (isPowerProvider(world, neighborPos)) {
                return true;  // Connected to a power provider
            }
        }
        return false;
    }

    public boolean isConnectedToPowerSource(World world, BlockPos pos, Set<BlockPos> visited) {
        visited.add(pos);

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

            if (neighborEntity instanceof PipeBlockEntity) {
                PipeBlockEntity pipeEntity = (PipeBlockEntity) neighborEntity;

                if (pipeEntity.getPowerLevel() > 0 && !visited.contains(neighborPos)) {
                    // If the neighbor has power, continue checking its connection
                    if (pipeEntity.isConnectedToPowerSource(world, neighborPos, visited)) {
                        return true;  // Found a valid connection to a power source
                    }
                }
            } else if (isPowerProvider(world, neighborPos)) {
                // If connected to a power provider, return true
                return true;
            }
        }

        return false; // No valid power connection found
    }

    // Helper method to get the power level from neighboring blocks
    public int getPowerFromNeighbors(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.offset(direction);
            BlockEntity neighborEntity = world.getBlockEntity(neighborPos);

            if (isPowerProvider(world, neighborPos)) {
                return getPowerFromProvider(world, neighborPos);  // Assuming you have a method for this
            }
            if (neighborEntity instanceof PipeBlockEntity entity && entity.getPowerLevel() > 0) {
                return ((PipeBlockEntity) neighborEntity).getPowerLevel();
            }

        }
        return 0;
    }

    // Helper method to check if a block is a power provider (custom logic needed)
    private boolean isPowerProvider(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof WindMill;
    }

    // Optional method to get power level from a provider (implement if needed)
    private int getPowerFromProvider(World world, BlockPos pos) {
        return 15;  // Example: assume max power level
    }

    // Save and load powerLevel to NBT (to persist across world saves)
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.powerLevel = nbt.getInt("PowerLevel");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("PowerLevel", powerLevel);
    }
}
