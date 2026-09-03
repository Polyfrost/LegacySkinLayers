package dev.lunasa.skinlayers.mesh

object CubeCorners {
    private val OFFSETS = arrayOf(
        floatArrayOf(1f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f),
        floatArrayOf(1f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 1f, 1f, 1f),
        floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f),
        floatArrayOf(0f, 0f, 1f, 1f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 1f),
        floatArrayOf(0f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f, 0f),
        floatArrayOf(1f, 0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f, 1f, 1f, 1f),
    )

    fun of(face: Face): FloatArray = OFFSETS[face.ordinal]
}
