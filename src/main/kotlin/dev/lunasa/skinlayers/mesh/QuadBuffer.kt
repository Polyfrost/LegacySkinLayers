package dev.lunasa.skinlayers.mesh

class QuadBuffer(initialQuads: Int = 512) {
    private var data = FloatArray(initialQuads * STRIDE)
    private var cursor = 0

    val quadCount: Int get() = cursor / STRIDE

    fun beginQuad(x: Float, y: Float, z: Float) {
        ensure()
        data[cursor] = x
        data[cursor + 1] = y
        data[cursor + 2] = z
        cursor += 3
    }

    fun vertex(x: Float, y: Float, z: Float, u: Float, v: Float) {
        data[cursor] = x
        data[cursor + 1] = y
        data[cursor + 2] = z
        data[cursor + 3] = u
        data[cursor + 4] = v
        cursor += 5
    }

    fun toFloatArray(): FloatArray = data.copyOf(cursor)

    private fun ensure() {
        if (cursor + STRIDE > data.size) {
            data = data.copyOf(maxOf(data.size * 2, cursor + STRIDE))
        }
    }

    companion object {
        const val STRIDE: Int = 3 + 4 * 5
    }
}
