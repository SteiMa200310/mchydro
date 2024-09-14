package org.aec.hydro.pipeHandling.delegates;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.core.EnergyContext;

import java.util.List;

@FunctionalInterface
public interface GetContextInDirDelegate {
    EnergyContext GetContextInDir(World world, BlockPos pos, List<Block> powerProviders, Block block, Direction direction);
}
