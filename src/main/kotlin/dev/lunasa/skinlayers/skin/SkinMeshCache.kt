package dev.lunasa.skinlayers.skin

import dev.lunasa.skinlayers.SkinLayers
import dev.lunasa.skinlayers.mesh.SkinPixels
import net.minecraft.util.Identifier

object SkinMeshCache {
    private const val CAPACITY = 64

    private data class Key(val skin: Identifier, val slim: Boolean)

    private class Cache<K> : LinkedHashMap<K, SkinMeshes>(32, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, SkinMeshes>): Boolean {
            if (size <= CAPACITY) return false
            eldest.value.dispose()
            return true
        }
    }

    private val players = Cache<Key>()
    private val skulls = Cache<Identifier>()
    private val unusable = HashSet<Identifier>()

    fun forPlayer(skin: Identifier, slim: Boolean): SkinMeshes? {
        val key = Key(skin, slim)
        players[key]?.let { return it }
        val pixels = pixels(skin) ?: return null
        return SkinMeshes.build(pixels, slim).also { players[key] = it }
    }

    fun forSkull(skin: Identifier): SkinMeshes? {
        skulls[skin]?.let { return it }
        val pixels = pixels(skin) ?: return null
        return SkinMeshes.buildSkull(pixels).also { skulls[skin] = it }
    }

    fun clear() {
        players.values.forEach(SkinMeshes::dispose)
        players.clear()
        skulls.values.forEach(SkinMeshes::dispose)
        skulls.clear()
        unusable.clear()
    }

    private fun pixels(skin: Identifier): SkinPixels? {
        if (skin in unusable) return null
        return when (val lookup = SkinImages.read(skin)) {
            is SkinLookup.Loaded -> lookup.pixels
            SkinLookup.Pending -> null
            SkinLookup.Unusable -> {
                SkinLayers.LOGGER.debug("Skin {} cannot be turned into 3d layers", skin)
                unusable.add(skin)
                null
            }
        }
    }
}
