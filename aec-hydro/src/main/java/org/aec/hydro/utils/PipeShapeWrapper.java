package org.aec.hydro.utils;

import net.minecraft.util.shape.VoxelShape;
import org.aec.hydro.block.PipeV2;

public class PipeShapeWrapper {
    public VoxelShape SHAPE_NORTH_SOUTH;
    public VoxelShape SHAPE_EAST_WEST;
    public VoxelShape SHAPE_UP_DOWN;

    public VoxelShape SHAPE_NORTH_EAST;
    public VoxelShape SHAPE_EAST_SOUTH;
    public VoxelShape SHAPE_SOUTH_WEST;
    public VoxelShape SHAPE_WEST_NORTH;

    public VoxelShape SHAPE_NORTH_UP;
    public VoxelShape SHAPE_NORTH_DOWN;
    public VoxelShape SHAPE_EAST_UP;
    public VoxelShape SHAPE_EAST_DOWN;

    public VoxelShape SHAPE_SOUTH_UP;
    public VoxelShape SHAPE_SOUTH_DOWN;
    public VoxelShape SHAPE_WEST_UP;
    public VoxelShape SHAPE_WEST_DOWN;

    public PipeShapeWrapper(VoxelShape baseF, VoxelShape baseE) {
        this.SHAPE_NORTH_SOUTH = baseF;
        this.SHAPE_EAST_WEST = VoxelGenerator.rotateShape(0, 1, 0, SHAPE_NORTH_SOUTH);
        this.SHAPE_UP_DOWN = VoxelGenerator.rotateShape(1, 0, 0, SHAPE_NORTH_SOUTH);

        this.SHAPE_NORTH_EAST = baseE;
        this.SHAPE_EAST_SOUTH = VoxelGenerator.rotateShape(0, 1, 0, SHAPE_NORTH_EAST);
        this.SHAPE_SOUTH_WEST = VoxelGenerator.rotateShape(0, 2, 0, SHAPE_NORTH_EAST);
        this.SHAPE_WEST_NORTH = VoxelGenerator.rotateShape(0, 3, 0, SHAPE_NORTH_EAST);

        this.SHAPE_NORTH_UP = VoxelGenerator.rotateShape(1, 3, 0, SHAPE_NORTH_EAST);
        this.SHAPE_NORTH_DOWN = VoxelGenerator.rotateShape(3, 3, 0, SHAPE_NORTH_EAST);
        this.SHAPE_EAST_UP = VoxelGenerator.rotateShape(1, 0, 0, SHAPE_NORTH_EAST);
        this.SHAPE_EAST_DOWN = VoxelGenerator.rotateShape(3, 0, 0, SHAPE_NORTH_EAST);

        this.SHAPE_SOUTH_UP = VoxelGenerator.rotateShape(1, 1, 0, SHAPE_NORTH_EAST);
        this.SHAPE_SOUTH_DOWN = VoxelGenerator.rotateShape(3, 1, 0, SHAPE_NORTH_EAST);
        this.SHAPE_WEST_UP = VoxelGenerator.rotateShape(1, 2, 0, SHAPE_NORTH_EAST);
        this.SHAPE_WEST_DOWN = VoxelGenerator.rotateShape(3, 2, 0, SHAPE_NORTH_EAST);
    }

    public VoxelShape GetShape(PipeID id) {
        return switch (id) {
            case F1 -> this.SHAPE_NORTH_SOUTH;
            case F2 -> this.SHAPE_EAST_WEST;
            case F3 -> this.SHAPE_UP_DOWN;

            case E1 -> this.SHAPE_NORTH_EAST;
            case E2 -> this.SHAPE_EAST_SOUTH;
            case E3 -> this.SHAPE_SOUTH_WEST;
            case E4 -> this.SHAPE_WEST_NORTH;

            case E5 -> this.SHAPE_NORTH_UP;
            case E6 -> this.SHAPE_NORTH_DOWN;
            case E7 -> this.SHAPE_EAST_UP;
            case E8 -> this.SHAPE_EAST_DOWN;

            case E9 -> this.SHAPE_SOUTH_UP;
            case E10 -> this.SHAPE_SOUTH_DOWN;
            case E11 -> this.SHAPE_WEST_UP;
            case E12 -> this.SHAPE_WEST_DOWN;
        };
    }
}