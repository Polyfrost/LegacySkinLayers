package dev.lunasa.skinlayers.mixin;

import net.minecraft.client.render.entity.model.SkullOverlayEntityModel;
import net.minecraft.client.render.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkullOverlayEntityModel.class)
public interface SkullOverlayEntityModelAccessor {

    @Accessor("field_10555")
    ModelPart skinLayers$overlayPart();
}
