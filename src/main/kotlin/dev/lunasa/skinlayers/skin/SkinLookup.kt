package dev.lunasa.skinlayers.skin

import dev.lunasa.skinlayers.mesh.SkinPixels

sealed interface SkinLookup {
    data object Pending : SkinLookup
    data object Unusable : SkinLookup
    data class Loaded(val pixels: SkinPixels) : SkinLookup
}
