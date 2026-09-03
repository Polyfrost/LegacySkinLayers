package org.polyfrost.skinlayers.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import org.polyfrost.skinlayers.render.CompiledMesh;
import org.polyfrost.skinlayers.render.MeshHost;
import org.polyfrost.skinlayers.render.MeshOffset;
import net.minecraft.client.render.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements MeshHost {
    @Unique
    private static final float DEGREES_PER_RADIAN = 57.295776F;

    @Shadow
    public float pivotX;
    @Shadow
    public float pivotY;
    @Shadow
    public float pivotZ;

    @Shadow
    public float posX;
    @Shadow
    public float posY;
    @Shadow
    public float posZ;

    @Shadow
    public float offsetX;
    @Shadow
    public float offsetY;
    @Shadow
    public float offsetZ;

    @Shadow
    public boolean visible;
    @Shadow
    public boolean hide;

    @Unique
    private CompiledMesh skinLayers$mesh;
    @Unique
    private MeshOffset skinLayers$offset;

    @Override
    public void attach(CompiledMesh mesh, MeshOffset offset) {
        this.skinLayers$mesh = mesh;
        this.skinLayers$offset = offset;
    }

    @Inject(method = "render(F)V", at = @At("HEAD"), cancellable = true)
    private void skinLayers$renderMesh(float scale, CallbackInfo ci) {
        CompiledMesh mesh = this.skinLayers$mesh;
        MeshOffset offset = this.skinLayers$offset;
        if (mesh == null || offset == null) {
            return;
        }

        ci.cancel();
        if (this.hide || !this.visible) {
            return;
        }

        GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.pivotX * scale, this.pivotY * scale, this.pivotZ * scale);
        if (this.posZ != 0.0F) {
            GlStateManager.rotate(this.posZ * DEGREES_PER_RADIAN, 0.0F, 0.0F, 1.0F);
        }
        if (this.posY != 0.0F) {
            GlStateManager.rotate(this.posY * DEGREES_PER_RADIAN, 0.0F, 1.0F, 0.0F);
        }
        if (this.posX != 0.0F) {
            GlStateManager.rotate(this.posX * DEGREES_PER_RADIAN, 1.0F, 0.0F, 0.0F);
        }

        offset.apply();
        mesh.render(scale);

        GlStateManager.popMatrix();
        GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
}
