package org.polyfrost.skinlayers.mixin;

import org.polyfrost.skinlayers.skin.SkinTextureImageAccess;
import java.awt.image.BufferedImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerSkinTexture.class)
public abstract class PlayerSkinTextureMixin implements SkinTextureImageAccess {
    @Shadow
    private BufferedImage field_6550;

    @Override
    public BufferedImage image() {
        return this.field_6550;
    }
}
