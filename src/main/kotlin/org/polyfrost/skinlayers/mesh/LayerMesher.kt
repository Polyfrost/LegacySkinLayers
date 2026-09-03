package org.polyfrost.skinlayers.mesh

class LayerMesher(
    private val pixels: SkinPixels,
    private val box: BoxLayout,
    private val fastRender: Boolean,
) {

    private val buffer = QuadBuffer()
    private val texWidth = pixels.width.toFloat()
    private val texHeight = pixels.height.toFloat()

    private val corners = FloatArray(12)
    private val mitres = arrayOfNulls<Mitre>(3)

    private var originX = 0f
    private var originY = 0f
    private var originZ = 0f
    private var occupied = false

    private data class Mitre(val a: Face, val b: Face)

    fun build(topPivot: Boolean, pivotOffset: Float): FloatArray? {
        originX = -box.width / 2f
        originY = if (topPivot) pivotOffset else -box.height + pivotOffset
        originZ = -box.depth / 2f

        for (face in Face.VALUES) {
            val size = box.sizeOf(face)
            for (u in 0 until size.u) {
                for (v in 0 until size.v) {
                    addVoxel(face, Uv(u, v))
                }
            }
        }

        if (fastRender && occupied) appendOuterBox()

        return if (buffer.quadCount == 0) null else buffer.toFloatArray()
    }

    private fun addVoxel(face: Face, faceUv: Uv) {
        val texel = box.texelOf(face, faceUv)
        if (!pixels.isPresent(texel)) return
        occupied = true

        val cell = box.cellOf(face, faceUv)
        val solid = pixels.isSolid(texel)

        mitres.fill(null)
        var hidden = 0
        var onBorder = false
        var backsideOverlaps = false

        for (side in Face.VALUES) {
            if (side.axis == face.axis) continue

            val neighbour = cell + side
            val neighbourUv = box.project(face, neighbour)

            if (!box.contains(face, neighbourUv)) {
                onBorder = true
                if (pixels.isPresent(box.texelOf(side, box.project(side, cell)))) {
                    backsideOverlaps = true
                    hidden = hidden or side.bit()
                    addMitre(face.opposite, side)
                } else if (pixels.isPresent(box.texelOf(side, box.project(side, cell - face)))) {
                    backsideOverlaps = true
                }
                continue
            }

            val neighbourTexel = box.texelOf(face, neighbourUv)
            if (pixels.isPresent(neighbourTexel)) {
                if (covers(solid, neighbourTexel)) hidden = hidden or side.bit()
                continue
            }

            val beyond = neighbour + side
            if (box.contains(face, box.project(face, beyond))) continue

            val beyondTexel = box.texelOf(side, box.project(side, beyond))
            if (pixels.isPresent(beyondTexel) && covers(solid, beyondTexel)) {
                hidden = hidden or side.bit()
            }
        }

        if (!onBorder || backsideOverlaps) hidden = hidden or face.opposite.bit()
        if (fastRender) hidden = hidden or face.bit()

        emitVoxel(cell, texel, hidden)
    }

    private fun covers(solid: Boolean, texel: Uv) = !solid || pixels.isSolid(texel)

    private fun addMitre(a: Face, b: Face) {
        for (axis in Axis.VALUES) {
            if (a.axis == axis || b.axis == axis) continue
            mitres[axis.ordinal] = Mitre(a, b)
            return
        }
    }

    private fun emitVoxel(cell: Cell, texel: Uv, hidden: Int) {
        val x = originX + cell.x
        val y = originY + cell.y
        val z = originZ + cell.z

        for (face in Face.VALUES) {
            if (hidden and face.bit() != 0) continue
            setCorners(face, x, y, z, 1f, 1f, 1f)
            mitres[face.axis.ordinal]?.let(::collapseCorner)
            writeQuad(face, texel.u, texel.u + 1, texel.v, texel.v + 1)
        }
    }

    private fun appendOuterBox() {
        val w = box.width
        val h = box.height
        val d = box.depth
        val left = box.texU
        val front = left + d
        val right = front + w
        val back = right + d
        val top = box.texV
        val side = top + d
        val bottom = side + h

        writeBoxFace(Face.WEST, left, front, side, bottom)
        writeBoxFace(Face.NORTH, front, right, side, bottom)
        writeBoxFace(Face.EAST, right, back, side, bottom)
        writeBoxFace(Face.SOUTH, back, back + w, side, bottom)
        writeBoxFace(Face.DOWN, front, right, top, side)
        writeBoxFace(Face.UP, right, right + w, side, top)
    }

    private fun writeBoxFace(face: Face, uMin: Int, uMax: Int, vMin: Int, vMax: Int) {
        setCorners(face, originX, originY, originZ, box.width.toFloat(), box.height.toFloat(), box.depth.toFloat())
        writeQuad(face, uMin, uMax, vMin, vMax)
    }

    private fun setCorners(face: Face, x: Float, y: Float, z: Float, sx: Float, sy: Float, sz: Float) {
        val offsets = CubeCorners.of(face)
        var i = 0
        while (i < corners.size) {
            corners[i] = x + offsets[i] * sx
            corners[i + 1] = y + offsets[i + 1] * sy
            corners[i + 2] = z + offsets[i + 2] * sz
            i += 3
        }
    }

    private fun collapseCorner(mitre: Mitre) {
        var drop = 0
        for (i in 1..3) if (!isOuter(drop, i, mitre)) drop = i

        var kept = 0
        for (i in 0..3) {
            if (i == drop) continue
            corners[kept * 3] = corners[i * 3]
            corners[kept * 3 + 1] = corners[i * 3 + 1]
            corners[kept * 3 + 2] = corners[i * 3 + 2]
            kept++
        }
        corners[9] = corners[6]
        corners[10] = corners[7]
        corners[11] = corners[8]
    }

    private fun isOuter(i: Int, j: Int, mitre: Mitre): Boolean {
        val alongA = compare(i, j, mitre.a)
        if (alongA != 0) return alongA > 0
        val alongB = compare(i, j, mitre.b)
        return alongB == 0 || alongB > 0
    }

    private fun compare(i: Int, j: Int, direction: Face): Int {
        val delta = direction.axis.choose(
            corners[i * 3] - corners[j * 3],
            corners[i * 3 + 1] - corners[j * 3 + 1],
            corners[i * 3 + 2] - corners[j * 3 + 2],
        )
        return (delta * direction.step).compareTo(0f)
    }

    private fun writeQuad(face: Face, uMin: Int, uMax: Int, vMin: Int, vMax: Int) {
        val u0 = uMin / texWidth
        val u1 = uMax / texWidth
        val v0 = vMin / texHeight
        val v1 = vMax / texHeight

        buffer.beginQuad(face.stepX.toFloat(), face.stepY.toFloat(), face.stepZ.toFloat())
        buffer.vertex(corners[0], corners[1], corners[2], u1, v0)
        buffer.vertex(corners[3], corners[4], corners[5], u0, v0)
        buffer.vertex(corners[6], corners[7], corners[8], u0, v1)
        buffer.vertex(corners[9], corners[10], corners[11], u1, v1)
    }

    private fun Face.bit() = 1 shl ordinal
}
