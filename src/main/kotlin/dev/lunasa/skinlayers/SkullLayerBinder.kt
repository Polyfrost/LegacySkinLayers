package dev.lunasa.skinlayers

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import dev.lunasa.skinlayers.render.MeshHost
import dev.lunasa.skinlayers.render.MeshOffset
import dev.lunasa.skinlayers.skin.SkinMeshCache
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.ModelPart
import net.minecraft.client.util.DefaultSkinHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

object SkullLayerBinder {
    @JvmStatic
    var itemRender: Boolean = false

    @JvmStatic
    fun bind(overlay: ModelPart?, profile: GameProfile?) {
        val host = overlay as? MeshHost ?: return
        host.attach(null, null)

        val config = SkinLayers.config
        if (!config.enableSkulls) return
        if (itemRender && !config.enableSkullsItems) return
        if (profile == null) return

        val mesh = SkinMeshCache.forSkull(skinOf(profile))?.head ?: return
        host.attach(mesh, MeshOffset.SKULL)
    }

    @JvmStatic
    fun clear(overlay: ModelPart?) {
        (overlay as? MeshHost)?.attach(null, null)
    }

    private fun skinOf(profile: GameProfile): Identifier {
        val provider = MinecraftClient.getInstance().skinProvider
        val skin = provider.m_22524589(profile)?.get(MinecraftProfileTexture.Type.SKIN)

        return if (skin != null) {
            provider.m_65195975(skin, MinecraftProfileTexture.Type.SKIN)
        } else {
            DefaultSkinHelper.getTexture(PlayerEntity.getUuidFromProfile(profile))
        }
    }
}
