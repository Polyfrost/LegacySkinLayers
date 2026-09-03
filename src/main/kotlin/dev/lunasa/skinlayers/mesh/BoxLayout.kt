package dev.lunasa.skinlayers.mesh

class BoxLayout(
    val width: Int,
    val height: Int,
    val depth: Int,
    val texU: Int,
    val texV: Int,
) {
    fun sizeOf(face: Face): Uv = when (face) {
        Face.DOWN, Face.UP -> Uv(width, depth)
        Face.NORTH, Face.SOUTH -> Uv(width, height)
        Face.WEST, Face.EAST -> Uv(depth, height)
    }

    fun contains(face: Face, uv: Uv): Boolean {
        val size = sizeOf(face)
        return uv.u >= 0 && uv.v >= 0 && uv.u < size.u && uv.v < size.v
    }

    fun texelOf(face: Face, uv: Uv): Uv = Uv(
        texU + uv.u + when (face) {
            Face.WEST -> 0
            Face.DOWN, Face.NORTH -> depth
            Face.UP, Face.EAST -> depth + width
            Face.SOUTH -> depth + width + depth
        },
        texV + uv.v + when (face) {
            Face.DOWN, Face.UP -> 0
            else -> depth
        },
    )

    fun cellOf(face: Face, uv: Uv): Cell = Cell(
        when (face) {
            Face.DOWN, Face.UP, Face.NORTH -> uv.u
            Face.SOUTH -> width - 1 - uv.u
            Face.WEST -> 0
            Face.EAST -> width - 1
        },
        when (face) {
            Face.DOWN -> 0
            Face.UP -> height - 1
            else -> uv.v
        },
        when (face) {
            Face.DOWN, Face.UP -> depth - 1 - uv.v
            Face.NORTH -> 0
            Face.SOUTH -> depth - 1
            Face.WEST -> depth - 1 - uv.u
            Face.EAST -> uv.u
        },
    )

    fun project(face: Face, cell: Cell): Uv = Uv(
        when (face) {
            Face.DOWN, Face.UP, Face.NORTH -> cell.x
            Face.SOUTH -> width - 1 - cell.x
            Face.WEST -> depth - 1 - cell.z
            Face.EAST -> cell.z
        },
        when (face) {
            Face.DOWN, Face.UP -> depth - 1 - cell.z
            else -> cell.y
        },
    )
}
