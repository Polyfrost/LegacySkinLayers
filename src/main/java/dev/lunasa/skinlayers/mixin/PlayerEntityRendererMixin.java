package dev.lunasa.skinlayers.mixin;

import dev.lunasa.skinlayers.PlayerLayerBinder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Shadow
    public abstract PlayerEntityModel getModel();

    @Inject(method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("RETURN"))
    private void skinLayers$bindLayers(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerEntityModel model = getModel();
        boolean slim = ((PlayerEntityModelAccessor) model).skinLayers$isThinArms();
        PlayerLayerBinder.bind(model, player, slim);
    }

    @Inject(method = "renderRightArm(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("HEAD"))
    private void skinLayers$beginRightArm(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerLayerBinder.setFirstPerson(true);
    }

    @Inject(method = "renderRightArm(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("RETURN"))
    private void skinLayers$endRightArm(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerLayerBinder.setFirstPerson(false);
    }

    @Inject(method = "renderLeftArm(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("HEAD"))
    private void skinLayers$beginLeftArm(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerLayerBinder.setFirstPerson(true);
    }

    @Inject(method = "renderLeftArm(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("RETURN"))
    private void skinLayers$endLeftArm(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerLayerBinder.setFirstPerson(false);
    }
}
