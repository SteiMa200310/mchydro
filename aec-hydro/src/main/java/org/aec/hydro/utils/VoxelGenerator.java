package org.aec.hydro.utils;

import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class VoxelGenerator {
    public static VoxelShape makeSolarShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0, -1, 0, 1, -0.875, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0625, -0.875, 0.0625, 0.9375, -0.5, 0.9375), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, -0.5, 0.25, 0.75, -0.0625, 0.75),BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.6875, 1.625, -1, 1.6875, 1.75, 2), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.3125, -0.6875, 0.75, 0.6875, -0.3125, 1), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.375, -0.125, 0.375, 0.625, 0.875, 0.625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(-0.6875, 1.625, -1, 0.3125, 1.75, 2), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.4375, 0.875, 0.4375, 0.5625, 1.5625, 0.5625), BooleanBiFunction.OR);
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.3125, 1.4375, 0.3125, 0.6875, 1.8125, 0.6875), BooleanBiFunction.OR);

        return shape;
    }

    public static VoxelShape makeWindmillShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.375, 0, 0.375, 0.625, 4.9375, 0.625),
                VoxelShapes.cuboid(0.3125, 4.875, 0.3125, 0.6875, 5.25, 0.6875),
                VoxelShapes.cuboid(0.375, 5.1875, 0.125, 0.625, 8.125, 0.25),
                VoxelShapes.cuboid(0.375, 4.9375, 0.0625, 0.625, 5.1875, 0.3125),
                VoxelShapes.cuboid(0.375, 5.1875, 0.125, 0.625, 8.125, 0.25),
                VoxelShapes.cuboid(0.375, 5.1875, 0.125, 0.625, 8.125, 0.25)
        );
    }

    public static VoxelShape createTestShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-1.625, 0, -1.75, 3.5625, 0.125, 3.4375));

        return shape;
    }
}
