package org.polyfrost.skinlayers.render

import com.mojang.blaze3d.platform.GlStateManager
import org.polyfrost.skinlayers.SkinLayers
import org.polyfrost.skinlayers.config.SkinLayersConfig

class MeshOffset(
    private val shape: MeshShape,
    private val mirrored: Boolean = false,
    private val firstPerson: Boolean = false,
    private val skull: Boolean = false,
) {

    fun apply() {
        val config = SkinLayers.config
        if (shape == MeshShape.HEAD) applyHead(config) else applyLimb(config)
    }

    private fun applyHead(config: SkinLayersConfig) {
        val voxelSize = if (skull) config.skullVoxelSize else config.headVoxelSize
        GlStateManager.translate(0f, -HEAD_PIVOT, 0f)
        GlStateManager.scale(voxelSize, voxelSize, voxelSize)
        GlStateManager.translate(0f, HEAD_PIVOT - HEAD_DROP, 0f)
    }

    private fun applyLimb(config: SkinLayersConfig) {
        val scale = if (firstPerson) config.firstPersonPixelScaling else config.baseVoxelSize
        val widthScale = if (shape == MeshShape.BODY) config.bodyVoxelWidthSize else scale
        GlStateManager.scale(widthScale, HEIGHT_SCALE, scale)

        val inset = if (mirrored) -shape.inset else shape.inset
        GlStateManager.translate(inset / PIXELS_PER_BLOCK, shape.yOffset / PIXELS_PER_BLOCK, 0f)
    }

    companion object {
        private const val HEIGHT_SCALE = 1.035f
        private const val HEAD_PIVOT = 0.25f
        private const val HEAD_DROP = 0.04f
        private const val PIXELS_PER_BLOCK = 16f

        private const val MIRRORED = 1
        private const val SLIM = 2
        private const val FIRST_PERSON = 4

        val HEAD = MeshOffset(MeshShape.HEAD)
        val SKULL = MeshOffset(MeshShape.HEAD, skull = true)
        val BODY = MeshOffset(MeshShape.BODY)
        val LEG = MeshOffset(MeshShape.LEG)

        private val ARMS = Array(8) { key ->
            MeshOffset(
                shape = if (key and SLIM != 0) MeshShape.SLIM_ARM else MeshShape.ARM,
                mirrored = key and MIRRORED != 0,
                firstPerson = key and FIRST_PERSON != 0,
            )
        }

        fun arm(mirrored: Boolean, slim: Boolean, firstPerson: Boolean): MeshOffset {
            var key = 0
            if (mirrored) key = key or MIRRORED
            if (slim) key = key or SLIM
            if (firstPerson) key = key or FIRST_PERSON
            return ARMS[key]
        }
    }
}
