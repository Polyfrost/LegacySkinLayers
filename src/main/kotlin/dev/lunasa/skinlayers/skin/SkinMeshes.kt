package dev.lunasa.skinlayers.skin

import dev.lunasa.skinlayers.SkinLayers
import dev.lunasa.skinlayers.mesh.BoxLayout
import dev.lunasa.skinlayers.mesh.LayerMesher
import dev.lunasa.skinlayers.mesh.SkinPixels
import dev.lunasa.skinlayers.render.CompiledMesh

class SkinMeshes(
    val head: CompiledMesh?,
    val body: CompiledMesh? = null,
    val leftArm: CompiledMesh? = null,
    val rightArm: CompiledMesh? = null,
    val leftLeg: CompiledMesh? = null,
    val rightLeg: CompiledMesh? = null,
) {
    fun dispose() {
        head?.dispose()
        body?.dispose()
        leftArm?.dispose()
        rightArm?.dispose()
        leftLeg?.dispose()
        rightLeg?.dispose()
    }

    companion object {
        private const val HEAD_PIVOT = 0.6f
        private const val ARM_PIVOT = -2f

        private val HEAD = BoxLayout(8, 8, 8, 32, 0)
        private val BODY = BoxLayout(8, 12, 4, 16, 32)
        private val LEFT_LEG = BoxLayout(4, 12, 4, 0, 48)
        private val RIGHT_LEG = BoxLayout(4, 12, 4, 0, 32)

        fun build(pixels: SkinPixels, slim: Boolean): SkinMeshes {
            val armWidth = if (slim) 3 else 4
            return SkinMeshes(
                head = mesh(pixels, HEAD, topPivot = false, pivotOffset = HEAD_PIVOT),
                body = mesh(pixels, BODY, topPivot = true),
                leftArm = mesh(pixels, BoxLayout(armWidth, 12, 4, 48, 48), topPivot = true, pivotOffset = ARM_PIVOT),
                rightArm = mesh(pixels, BoxLayout(armWidth, 12, 4, 40, 32), topPivot = true, pivotOffset = ARM_PIVOT),
                leftLeg = mesh(pixels, LEFT_LEG, topPivot = true),
                rightLeg = mesh(pixels, RIGHT_LEG, topPivot = true),
            )
        }

        fun buildSkull(pixels: SkinPixels): SkinMeshes =
            SkinMeshes(head = mesh(pixels, HEAD, topPivot = false, pivotOffset = HEAD_PIVOT))

        private fun mesh(
            pixels: SkinPixels,
            box: BoxLayout,
            topPivot: Boolean,
            pivotOffset: Float = 0f,
        ): CompiledMesh? {
            val quads = LayerMesher(pixels, box, SkinLayers.config.fastRender).build(topPivot, pivotOffset)
            return quads?.let(::CompiledMesh)
        }
    }
}
