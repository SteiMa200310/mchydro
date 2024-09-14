package org.aec.hydro.utils.PipeHandling.Utils;

public record PowerLevelInfo(int powerLevel, CustomDirection flowFrom, CustomDirection flowTo)
{
    public static PowerLevelInfo Error = new PowerLevelInfo(30, CustomDirection.NONE, CustomDirection.NONE);
}
