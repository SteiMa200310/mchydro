package org.aec.hydro.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.aec.hydro.pipeHandling.utils.PipeProperties;
import org.aec.hydro.sound._HydroSounds;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SolarPanelBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(_HydroBlockEntities.SOLARPANEL_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void markRemoved() {
        if (!world.isClient) {
            // Ensure sound is stopped when block entity is removed
            stopSolarSound((ServerWorld) world);
        }
        super.markRemoved();
    }

    public static void tick(World world, BlockPos pos, BlockState state, SolarPanelBlockEntity blockEntity) {
//        if (world.isNight())
//            state.with(PipeProperties.)

        if (!world.isClient) {
            blockEntity.startSolarSound((ServerWorld) world);
        }
    }

    public void startSolarSound(ServerWorld world) {
        world.playSound(
                null, // Player - null means it plays for all players nearby
                getPos(), // Position of the block entity
                _HydroSounds.SOLAR_LOOP, // Custom windmill sound event
                SoundCategory.BLOCKS, // Sound category
                0.05F, // Volume
                1.0F  // Pitch
        );
    }

    public void stopSolarSound(ServerWorld world) {
        Identifier soundId = new Identifier("hydro", "windmill_loop");

        // Iterate over all players nearby to stop the sound
        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.squaredDistanceTo(getPos().getX(), getPos().getY(), getPos().getZ()) < 10000) { // Example range check
                player.networkHandler.sendPacket(
                        new StopSoundS2CPacket(soundId, SoundCategory.BLOCKS)
                );
            }
        }
    }
}
