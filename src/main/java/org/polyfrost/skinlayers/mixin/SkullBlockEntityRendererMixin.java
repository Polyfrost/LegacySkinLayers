package org.polyfrost.skinlayers.mixin;

import com.mojang.authlib.GameProfile;
import org.polyfrost.skinlayers.SkullLayerBinder;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.entity.model.SkullOverlayEntityModel;
import net.minecraft.client.render.model.ModelPart;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkullBlockEntityRenderer.class)
public abstract class SkullBlockEntityRendererMixin {
    @Unique
    private static final int PLAYER_SKULL = 3;

    @Final
    @Shadow
    private SkullEntityModel overlayModel;

    @Inject(
        method = "render(FFFLnet/minecraft/util/math/Direction;FILcom/mojang/authlib/GameProfile;I)V",
        at = @At("HEAD")
    )
    private void skinLayers$bind(
        float x, float y, float z, Direction facing, float rotation,
        int skullType, GameProfile profile, int destroyStage, CallbackInfo ci
    ) {
        SkullLayerBinder.bind(skinLayers$overlayPart(), skullType == PLAYER_SKULL ? profile : null);
    }

    @Inject(
        method = "render(FFFLnet/minecraft/util/math/Direction;FILcom/mojang/authlib/GameProfile;I)V",
        at = @At("RETURN")
    )
    private void skinLayers$unbind(
        float x, float y, float z, Direction facing, float rotation,
        int skullType, GameProfile profile, int destroyStage, CallbackInfo ci
    ) {
        SkullLayerBinder.clear(skinLayers$overlayPart());
    }

    @Unique
    private ModelPart skinLayers$overlayPart() {
        SkullEntityModel model = this.overlayModel;
        if (model instanceof SkullOverlayEntityModel) {
            return ((SkullOverlayEntityModelAccessor) model).skinLayers$overlayPart();
        }
        return null;
    }
}
