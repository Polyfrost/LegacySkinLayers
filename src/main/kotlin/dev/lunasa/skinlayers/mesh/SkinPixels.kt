package dev.lunasa.skinlayers.mesh

class SkinPixels(val width: Int, val height: Int, private val argb: IntArray) {
    fun isPresent(texel: Uv): Boolean = alpha(texel) != 0

    fun isSolid(texel: Uv): Boolean = alpha(texel) == 0xFF

    private fun alpha(texel: Uv): Int {
        if (texel.u < 0 || texel.v < 0 || texel.u >= width || texel.v >= height) return 0
        return argb[texel.v * width + texel.u] ushr 24
    }
}
