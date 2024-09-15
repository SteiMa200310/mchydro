package org.aec.hydro.pipeHandling.utils;

import net.minecraft.block.BlockState;

public record PowerLevelInfo(int powerLevel, PowerFlowDirection flowFrom, PowerFlowDirection flowTo)
{
    public static PowerLevelInfo Error() {
        return new PowerLevelInfo(30, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
    }

    public static PowerLevelInfo Default() {
        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
    }

    public boolean IsError() {
        return this.powerLevel == 30 && this.flowFrom == PowerFlowDirection.NONE && this.flowTo == PowerFlowDirection.NONE;
    }

    public boolean IsDefault() {
        return this.powerLevel == 0 && this.flowFrom == PowerFlowDirection.NONE && this.flowTo == PowerFlowDirection.NONE;
    }

    public BlockState ApplyOn(BlockState state) {
        return state
            .with(PipeProperties.PowerLevel, this.powerLevel())
            .with(PipeProperties.ProviderFace, this.flowTo())
            .with(PipeProperties.RecieverFace, this.flowFrom());
    }
}