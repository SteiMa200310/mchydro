package org.aec.hydro.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class SolarPanelBlockEntity extends EntityModel<Entity> {
    private final ModelPart bone;
    public SolarPanelBlockEntity(ModelPart root) {
        this.bone = root.getChild("bone");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 58).cuboid(-16.0F, -2.0F, 0.0F, 16.0F, 2.0F, 16.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-15.0F, -8.0F, 1.0F, 14.0F, 6.0F, 14.0F, new Dilation(0.0F))
                .uv(0, 20).cuboid(-12.0F, -15.0F, 4.0F, 8.0F, 7.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 20).cuboid(-10.0F, -30.0F, 6.0F, 4.0F, 16.0F, 4.0F, new Dilation(0.0F))
                .uv(34, 22).cuboid(-9.0F, -41.0F, 7.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F))
                .uv(89, 67).cuboid(-11.0F, -45.0F, 5.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(8, 8).cuboid(-27.0F, -44.0F, -16.0F, 16.0F, 2.0F, 48.0F, new Dilation(0.0F))
                .uv(8, 8).cuboid(-5.0F, -44.0F, -16.0F, 16.0F, 2.0F, 48.0F, new Dilation(0.0F))
                .uv(129, 73).cuboid(-11.0F, -11.0F, 12.0F, 6.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }
    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
