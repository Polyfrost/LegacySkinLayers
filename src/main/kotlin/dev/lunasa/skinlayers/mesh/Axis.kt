package dev.lunasa.skinlayers.mesh

enum class Axis {
    X, Y, Z;

    fun choose(x: Float, y: Float, z: Float): Float = when (this) {
        X -> x
        Y -> y
        Z -> z
    }

    companion object {
        val VALUES: Array<Axis> = entries.toTypedArray()
    }
}