package org.polyfrost.skinlayers.render

import org.polyfrost.skinlayers.mesh.QuadBuffer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.GlAllocationUtils
import org.lwjgl.opengl.GL11

import com.mojang.blaze3d.platform.GlStateManager

class CompiledMesh(private val quads: FloatArray) {
    private var displayList = 0
    private var compiledScale = Float.NaN

    fun render(scale: Float) {
        if (displayList == 0 || compiledScale != scale) compile(scale)
        GlStateManager.callList(displayList)
    }

    fun dispose() {
        if (displayList != 0) {
            GlAllocationUtils.deleteLists(displayList, 1)
            displayList = 0
            compiledScale = Float.NaN
        }
    }

    private fun compile(scale: Float) {
        if (displayList == 0) displayList = GlAllocationUtils.genLists(1)
        GL11.glNewList(displayList, GL11.GL_COMPILE)
        emit(scale)
        GL11.glEndList()
        compiledScale = scale
    }

    private fun emit(scale: Float) {
        val tessellator = Tessellator.getInstance()
        val builder = tessellator.buffer
        builder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_NORMAL)

        for (quad in quads.indices step QuadBuffer.STRIDE) {
            val nx = quads[quad]
            val ny = quads[quad + 1]
            val nz = quads[quad + 2]
            for (corner in 0 until 4) {
                val v = quad + 3 + corner * 5
                builder
                    .vertex(
                        (quads[v] * scale).toDouble(),
                        (quads[v + 1] * scale).toDouble(),
                        (quads[v + 2] * scale).toDouble(),
                    )
                    .texture(quads[v + 3].toDouble(), quads[v + 4].toDouble())
                    .normal(nx, ny, nz)
                    .next()
            }
        }

        tessellator.draw()
    }
}
