package org.aec.hydro.utils;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class VoxelGenerator {
    public static VoxelShape makeWindmillShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 4.875, 0.1875, 0.75, 5.3125, 1),
                VoxelShapes.cuboid(0.125, 0, 0.1875, 0.875, 1.625, 0.9375),
                VoxelShapes.cuboid(0.1875, 0, 0, 0.8125, 0.9375, 0.4375),
                VoxelShapes.cuboid(0.1875, 1.375, 0.25, 0.8125, 4.9375, 0.875)
        );
    }

    public static VoxelShape makeSolarPanelShape(){
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.0625, 0, 0.1875, 0.9375, 1.5, 0.9375),
                VoxelShapes.cuboid(0.25, 0, 0.0625, 0.75, 2.75, 0.625),
                VoxelShapes.cuboid(0.375, 1.5, 0.4375, 0.625, 4.3125, 0.6875),
                VoxelShapes.cuboid(0.4375, 4.3125, 0.5, 0.5625, 4.4375, 0.625),
                VoxelShapes.cuboid(0.25, 4.3125, 0.4375, 0.75, 4.375, 0.5),
                VoxelShapes.cuboid(0.25, 4.3125, 0.625, 0.75, 4.375, 0.6875),
                VoxelShapes.cuboid(0.75, 4.25, -1.5625, 2.4375, 4.4375, 2.6875),
                VoxelShapes.cuboid(-1.4375, 4.25, -1.5625, 0.25, 4.4375, 2.6875),
                VoxelShapes.cuboid(0.25, 4.3125, -1.4375, 0.75, 4.375, 0.1875),
                VoxelShapes.cuboid(0.25, 4.3125, 0.9375, 0.75, 4.375, 2.5625),
                VoxelShapes.cuboid(0.3125, 4.4375, 0.375, 0.6875, 4.8125, 0.75),
                VoxelShapes.cuboid(0.1875, 0, 0, 0.8125, 0.9375, 0.4375)
        );
    }

    public static VoxelShape makeWaterwheelShape(){
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.25, -0.25, 0.75, 1.1875, 0.625),
                VoxelShapes.cuboid(-0.4375, 0, 0, 1.4375, 1, 0.75),
                VoxelShapes.cuboid(0.0625, 0, 0.125, 0.9375, 1.375, 0.875),
                VoxelShapes.cuboid(0.1875, 0, 0.5625, 0.8125, 0.9375, 1)
        );
    }

    //---Updated--
    public static VoxelShape makeElectrolyzerShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.1875000000000004, 0, 0.06250000000000044, 0.8125000000000004, 0.875, 0.18750000000000044),
                VoxelShapes.cuboid(0.06250000000000039, 0, 0.18750000000000044, 0.1875000000000004, 0.875, 0.8125000000000004),
                VoxelShapes.cuboid(0.1875000000000004, 0, 0.8125000000000004, 0.8125000000000004, 0.875, 0.9375000000000004),
                VoxelShapes.cuboid(0.8125000000000004, 0, 0.18750000000000044, 0.9375000000000004, 0.875, 0.8125000000000004),
                VoxelShapes.cuboid(0.25, 0.8125, 0.1875, 0.75, 1, 0.375),
                VoxelShapes.cuboid(0, 0.18749999999999992, 0.18750000000000022, 0.0625, 0.8124999999999999, 0.8125),
                VoxelShapes.cuboid(0.9375000000000004, 0.3125, 0.3125000000000009, 1.0000000000000004, 0.6875, 0.6875000000000011),
                VoxelShapes.cuboid(0.1875, 0.18749999999999992, 2.220446049250313e-16, 0.8125, 0.8124999999999999, 0.0625),
                VoxelShapes.cuboid(0.1875, 0.18749999999999992, 0.9375000000000002, 0.8125, 0.8124999999999999, 1),
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.8125, 0.8125),
                VoxelShapes.cuboid(0.25, 0.8125, 0.625, 0.75, 1, 0.8125),
                VoxelShapes.cuboid(0.4375, 0.8125, 0.375, 0.5625, 0.9375, 0.625)
        );
    }

    //---Updated--
    public static VoxelShape makeBrennstoffzelleShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.1875000000000004, 0, 0.06250000000000044, 0.8125000000000004, 0.875, 0.18750000000000044),
                VoxelShapes.cuboid(0.06250000000000039, 0, 0.18750000000000044, 0.1875000000000004, 0.875, 0.8125000000000004),
                VoxelShapes.cuboid(0.1875000000000004, 0, 0.8125000000000004, 0.8125000000000004, 0.875, 0.9375000000000004),
                VoxelShapes.cuboid(0.8125000000000004, 0, 0.18750000000000044, 0.9375000000000004, 0.875, 0.8125000000000004),
                VoxelShapes.cuboid(0, 0.18749999999999992, 0.18750000000000022, 0.0625, 0.8124999999999999, 0.8125),
                VoxelShapes.cuboid(0.9375000000000004, 0.3125, 0.3125000000000009, 1.0000000000000004, 0.6875, 0.6875000000000011),
                VoxelShapes.cuboid(0.1875, 0.18749999999999992, 2.220446049250313e-16, 0.8125, 0.8124999999999999, 0.0625),
                VoxelShapes.cuboid(0.1875, 0.18749999999999992, 0.9375000000000002, 0.8125, 0.8124999999999999, 1),
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.8125, 0.8125),
                VoxelShapes.cuboid(0.1875, 0.8125, 0.1875, 0.8125, 1.0625, 0.8125)
        );
    }

    //---Updated--
    public static VoxelShape makeCableCombinerShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0, 0.3125, 0.3125, 0.25, 0.6875, 0.6875),
                VoxelShapes.cuboid(0.6875, 0.25, 0.25, 1, 0.75, 0.75),
                VoxelShapes.cuboid(0.3125, 0.3125, 0, 0.6875, 0.6875, 1),
                VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.375, 0.6875),
                VoxelShapes.cuboid(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125),
                VoxelShapes.cuboid(0.3125, 0.625, 0.3125, 0.6875, 1, 0.6875)
        );
    }

    public static VoxelShape makeCableSplitterShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.1379441738241592, 0.375, 0.0625, 0.4504441738241592, 0.625, 0.5),
                VoxelShapes.cuboid(0.375, 0.3125, 0.6875, 1.3125, 0.6875, 1.0625),
                VoxelShapes.cuboid(0.25, 0.8125, 0.375, 0.5625, 1.1875, 0.75),
                VoxelShapes.cuboid(0.375, 0, 0.375, 0.6875, 0.375, 0.75),
                VoxelShapes.cuboid(0.6875, 0.3125, 0.3125, 1, 0.6875, 0.6875),
                VoxelShapes.cuboid(0, 0.3125, 0.3125, 0.3125, 0.6875, 0.6875),
                VoxelShapes.cuboid(0.53125, 0.375, 0.04955582617584081, 0.84375, 0.625, 0.4870558261758408),
                VoxelShapes.cuboid(0.375, 0.625, 0.0625, 0.625, 0.875, 0.5),
                VoxelShapes.cuboid(0.375, 0.125, 0, 0.625, 0.375, 0.4375),
                VoxelShapes.cuboid(0.25, 0.25, 0, 0.75, 0.75, 0.0625)
        );
    }

    //---Updated--
    public static VoxelShape makePipeCombinerShape(){
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.25, 0.0625, 0.75, 0.75, 0.75),
                VoxelShapes.cuboid(0.125, 0.125, 0, 0.875, 0.875, 0.0625),
                VoxelShapes.cuboid(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125),
                VoxelShapes.cuboid(0.1875, 0.1875, 0.9375, 0.8125, 0.8125, 1),
                VoxelShapes.cuboid(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
                VoxelShapes.cuboid(0.25, 0.25, 0.75, 0.75, 0.75, 0.9375),
                VoxelShapes.cuboid(0.25, 0.75, 0.25, 0.75, 0.9375, 0.75),
                VoxelShapes.cuboid(0.06249999999999989, 0.2499999999999998, 0.2499999999999999, 0.2499999999999999, 0.7499999999999998, 0.7499999999999999),
                VoxelShapes.cuboid(0, 0.18749999999999975, 0.1874999999999999, 0.0625, 0.8124999999999998, 0.8124999999999999),
                VoxelShapes.cuboid(0.9374999999999999, 0.18750000000000108, 0.1875000000000001, 0.9999999999999999, 0.8125000000000011, 0.8125000000000001),
                VoxelShapes.cuboid(0.7499999999999998, 0.25000000000000067, 0.2500000000000001, 0.9374999999999998, 0.7500000000000007, 0.7500000000000001),
                VoxelShapes.cuboid(0.25, 0.0625, 0.25, 0.75, 0.25, 0.75)
        );
    }

    //---Updated--
    public static VoxelShape makePumpShape() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.0625, 0.25, 0.75, 0.9375, 0.75),
                VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 0.1875, 0.875),
                VoxelShapes.cuboid(0.1875, 0.9375, 0.1875, 0.8125, 1, 0.8125),
                VoxelShapes.cuboid(0.4375, 0, 0.875, 0.5625, 0.75, 1),
                VoxelShapes.cuboid(0.4375, 0, 0, 0.5625, 0.75, 0.125),
                VoxelShapes.cuboid(0, 0, 0.4375, 0.125, 0.75, 0.5625),
                VoxelShapes.cuboid(0.875, 0, 0.4375, 1, 0.75, 0.5625),
                VoxelShapes.cuboid(0.75, 0.5625, 0.4375, 0.875, 0.6875, 0.5625),
                VoxelShapes.cuboid(0.125, 0.5625, 0.4375, 0.25, 0.6875, 0.5625),
                VoxelShapes.cuboid(0.4375, 0.5625, 0.125, 0.5625, 0.6875, 0.25),
                VoxelShapes.cuboid(0.4375, 0.5625, 0.75, 0.5625, 0.6875, 0.875)
        );
    }

    public static VoxelShape makeCableLongShape_NORTH_SOUTH(){
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.3125, 0.3125, 0, 0.6875, 0.6875, 1)
        );
    }

    public static VoxelShape makeCableEdgeShape_NORTH_EAST() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.3125, 0.3125, 0.3125, 1, 0.6875, 0.6875),
                VoxelShapes.cuboid(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.3125)
        );
    }

    public static VoxelShape makePipeLongShape_NORTH_SOUTH() {
        return VoxelShapes.union(
                VoxelShapes.cuboid(0.25, 0.25, 0.0625, 0.75, 0.75, 0.9375),
                VoxelShapes.cuboid(0.1875, 0.1875, 0, 0.8125, 0.8125, 0.0625),
                VoxelShapes.cuboid(0.1875, 0.1875, 0.9375, 0.8125, 0.8125, 1)
        );
    }

    public static VoxelShape makePipeEdgeShape_NORTH_EAST() {
//        VoxelShape shape = VoxelShapes.empty();
//        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.9375, 0.75, 0.75), BooleanBiFunction.OR);
//        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.9375, 0.1875, 0.1875, 1, 0.8125, 0.8125), BooleanBiFunction.OR);
//        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0.1875, 0, 0.8125, 0.8125, 0.0625), BooleanBiFunction.OR);
//        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.25, 0.25, 0.0625, 0.75, 0.75, 0.25), BooleanBiFunction.OR);
//
//        return shape;

        return VoxelShapes.union(VoxelShapes.cuboid(0.25, 0.25, 0.25, 0.9375, 0.75, 0.75),
                VoxelShapes.cuboid(0.9375, 0.1875, 0.1875, 1, 0.8125, 0.8125),
                VoxelShapes.cuboid(0.1875, 0.1875, 0, 0.8125, 0.8125, 0.0625),
                VoxelShapes.cuboid(0.25, 0.25, 0.0625, 0.75, 0.75, 0.25)
        );
    }

    public static VoxelShape rotateShape(int aoX, int aoY, int aoZ, VoxelShape shape) {
        //viewing an axis from a lesser value to a bigger value the rotation happens from right to left

        //consider the following in real life x y are the 2d axis and z is the up/down axis
        //where as in minecraft x and z are the 2d axis and y is the up/down axis

        //i am not sure what the minX minY minZ are actually below if they are based on in minecraft or in realife
        //the rotation matrixes i found in the internet (this is the value that the comment on each one stats and what it would rotate in real life)
        //i switch X Y Z ao's until i finally hit the right comination

        //now based on my explaination you might assume that y and z are swapped but ACTUALLY x and z are swapped i dont know why y is not
        //but i it still makes sense that one of the 2d axises (y/x) is swapped with the up/down one (z) - based on real life
        //maybe minecraft swapped x and y as well and that is way anyways you can provided the values based on REALLIFE and it works

        //forget everything above -> ACTUALLY ITS LIKE THIS
        //the previous pov i had made me think that for some reason a rotation on the x axis is rotating 90degrees when looking into x which was a bit intuitive for me
        //but its actually wrong -> rotation on the x axis means that you spin the object around the x axis based on its core - if you visualize that in your head it makes perfect sense
        //then the coordinates are of cours based on minecraft directions which is what i hoped to be the truth and it is

        //EXPLAINATION IN DC CHAT:
        //der rest macht eh perfekt sinn, und nochwas der einzige grund wieso mir die Lösung vorhin also die die ich dir heute gezeigt hat
        //überhaupt als "falsch" aufgefallen ist - war weil da kein musster zu erkennen war im vergleich zu den anderen shapes ->
        //schau dir nochmal an was ich gepushed habe - ich habe jetzt die übrigen so geändert das wir nur mit x und y achse rotieren wie gesagt so
        //kann man ja auch jede rotation erreichen, mir ist nämlich aufgefallen das wenn ich alles nur über x und y rotiere dann kann es
        //wirklich pro shape nur eine kombination geben um auf die lösung zu kommen (mudolo mit einbezogen weil natrülich ist x1 und y3 das selbe wie x5 und y8) auf
        //jeden fall hab is jetzt auf nur x und y geändert und siehe da -> es ist ein muster zu erkennen es ist nur so das es bei jetzt 3 0 1 2 ist das
        //liegt aber einfach an meiner anordnung von shapes, bei mir ist die reihenfolge nämlich immer
        //"north east south west" - i hoff du verstehst was ich hier schreibe aber i wenn wir nur über zwei aches rotieren gibt es immer
        //nur eine lösung und dann muss auch IMMER ein muster zu erkennen sein - ich würd das so approven wie es jetzt ist schau dir die änderung nochmal an

        shape = RotateX(aoX, shape);
        shape = RotateY(aoY, shape);
        shape = RotateZ(aoZ, shape);

        return shape;
    }

    public static VoxelShape RotateX(int aoX, VoxelShape shape)
    {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        for(int i = 0; i < aoX; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }
        return buffer[0];
    }

    public static VoxelShape RotateY(int aoY, VoxelShape shape)
    {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        for(int i = 0; i < aoY; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    public static VoxelShape RotateZ(int aoZ, VoxelShape shape)
    {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        for(int i = 0; i < aoZ; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxY, minX, minZ, 1 - minY, maxX, maxZ)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }
}