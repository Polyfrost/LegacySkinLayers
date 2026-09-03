package dev.lunasa.skinlayers.mesh

enum class Face(val axis: Axis, val stepX: Int, val stepY: Int, val stepZ: Int) {
    DOWN(Axis.Y, 0, -1, 0),
    UP(Axis.Y, 0, 1, 0),
    NORTH(Axis.Z, 0, 0, -1),
    SOUTH(Axis.Z, 0, 0, 1),
    WEST(Axis.X, -1, 0, 0),
    EAST(Axis.X, 1, 0, 0);

    val step: Int = stepX + stepY + stepZ

    val opposite: Face get() = VALUES[ordinal xor 1]

    companion object {
        val VALUES: Array<Face> = entries.toTypedArray()
    }
}
