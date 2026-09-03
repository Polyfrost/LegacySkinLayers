package dev.lunasa.skinlayers.mixin;

import dev.lunasa.skinlayers.SkullLayerBinder;
import net.minecraft.client.render.block.entity.BlockEntityItemStackRenderHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityItemStackRenderHelper.class)
public abstract class BlockEntityItemStackRenderHelperMixin {
    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void skinLayers$beginItem(ItemStack itemStack, CallbackInfo ci) {
        SkullLayerBinder.setItemRender(true);
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    private void skinLayers$endItem(ItemStack itemStack, CallbackInfo ci) {
        SkullLayerBinder.setItemRender(false);
    }
}
