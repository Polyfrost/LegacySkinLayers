package org.polyfrost.skinlayers.mesh

data class Cell(val x: Int, val y: Int, val z: Int) {
    operator fun plus(face: Face) = Cell(x + face.stepX, y + face.stepY, z + face.stepZ)

    operator fun minus(face: Face) = Cell(x - face.stepX, y - face.stepY, z - face.stepZ)
}
