package dev.lunasa.skinlayers.skin

import dev.lunasa.skinlayers.SkinLayers
import dev.lunasa.skinlayers.mesh.SkinPixels
import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.PlayerSkinTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object SkinImages {
    private const val SKIN_SIZE = 64

    fun read(skin: Identifier): SkinLookup {
        val client = MinecraftClient.getInstance()
        val texture = client.textureManager.getTexture(skin)

        val image: BufferedImage = if (texture is PlayerSkinTexture) {
            (texture as SkinTextureImageAccess).image() ?: return SkinLookup.Pending
        } else {
            runCatching { client.resourceManager.getResource(skin).inputStream.use(ImageIO::read) }
                .onFailure { SkinLayers.LOGGER.debug("No readable image behind skin {}", skin, it) }
                .getOrNull() ?: return SkinLookup.Unusable
        }

        if (image.width != SKIN_SIZE || image.height != SKIN_SIZE) return SkinLookup.Unusable

        return SkinLookup.Loaded(snapshot(image))
    }

    private fun snapshot(image: BufferedImage): SkinPixels {
        val width = image.width
        val height = image.height
        val argb = IntArray(width * height)
        image.getRGB(0, 0, width, height, argb, 0, width)
        return SkinPixels(width, height, argb)
    }
}
