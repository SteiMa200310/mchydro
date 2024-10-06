package org.aec.hydro.pipeHandling.utils;

import net.minecraft.block.BlockState;

public class PowerLevelInfo
{
    private static PowerLevelInfo changeLess = new PowerLevelInfo(-2, PowerFlowDirection.NONE, PowerFlowDirection.NONE);

    private int powerLevel;
    private PowerFlowDirection flowFrom;
    private PowerFlowDirection flowTo;

    public int powerLevel() {
        return powerLevel;
    }

    public PowerFlowDirection flowFrom() {
        return flowFrom;
    }

    public PowerFlowDirection flowTo() {
        return flowTo;
    }

    private PowerLevelInfo(int powerLevel, PowerFlowDirection flowFrom, PowerFlowDirection flowTo) {
        this.powerLevel = powerLevel;
        this.flowFrom = flowFrom;
        this.flowTo = flowTo;
    }

    public static PowerLevelInfo Construct(int powerLevel, PowerFlowDirection flowFrom, PowerFlowDirection flowTo) {
        if (powerLevel == 0)
            return PowerLevelInfo.Default();

        if (powerLevel < 0)
            return PowerLevelInfo.Error();

        return new PowerLevelInfo(powerLevel, flowFrom, flowTo);
    }

    public static PowerLevelInfo Error() {
        return new PowerLevelInfo(30, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
    }

    public static PowerLevelInfo Default() {
        return new PowerLevelInfo(0, PowerFlowDirection.NONE, PowerFlowDirection.NONE);
    }

    public static PowerLevelInfo Current() {
        return PowerLevelInfo.changeLess;
    }

    public boolean IsError() {
        return this.powerLevel == 30 && this.flowFrom == PowerFlowDirection.NONE && this.flowTo == PowerFlowDirection.NONE;
    }

    public boolean IsDefault() {
        return this.powerLevel == 0 && this.flowFrom == PowerFlowDirection.NONE && this.flowTo == PowerFlowDirection.NONE;
    }

    public BlockState ApplyOn(BlockState state) {
        if (this == PowerLevelInfo.changeLess)
            return state;

        return state
            .with(PipeProperties.PowerLevel, this.powerLevel)
            .with(PipeProperties.ProviderFace, this.flowTo)
            .with(PipeProperties.RecieverFace, this.flowFrom);
    }
}