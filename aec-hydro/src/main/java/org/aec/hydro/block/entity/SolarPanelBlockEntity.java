package org.aec.hydro.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
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
}
