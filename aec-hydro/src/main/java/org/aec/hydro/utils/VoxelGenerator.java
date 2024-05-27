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
                VoxelShapes.cuboid(0.25, 4.875, 0.1875, 0.75, 5.3125, 1),
                VoxelShapes.cuboid(0.375, 5.1875, 0, 0.625, 8.125, 0.125),
                VoxelShapes.cuboid(0.3125, 4.875, -0.0625, 0.6875, 5.25, 0.1875),
                VoxelShapes.cuboid(0.375, 5.1875, 0, 0.625, 8.125, 0.125),
                VoxelShapes.cuboid(0.125, 0, 0.1875, 0.875, 1.625, 0.9375),
                VoxelShapes.cuboid(0.125, 1.375, 0.625, 0.4375, 4.9375, 0.9375),
                VoxelShapes.cuboid(0.5625, 1.375, 0.625, 0.875, 4.9375, 0.9375),
                VoxelShapes.cuboid(0.5627379446045355, 1.3858944678434577, 0.18702433726146817, 0.8752379446045355, 4.948394467843457, 0.49952433726146817),
                VoxelShapes.cuboid(0.125, 1.375, 0.1875, 0.4375, 4.9375, 0.5),
                VoxelShapes.cuboid(0.375, 5.1875, 0, 0.625, 8.125, 0.125),
                VoxelShapes.cuboid(0.1875, 0, 0, 0.8125, 0.9375, 0.4375)
        );
    }

    public static VoxelShape createTestShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-1.625, 0, -1.75, 3.5625, 0.125, 3.4375));

        return shape;
    }
}
