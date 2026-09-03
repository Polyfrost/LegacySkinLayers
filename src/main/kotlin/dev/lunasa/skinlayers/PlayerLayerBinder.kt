package dev.lunasa.skinlayers

import dev.lunasa.skinlayers.render.CompiledMesh
import dev.lunasa.skinlayers.render.MeshHost
import dev.lunasa.skinlayers.render.MeshOffset
import dev.lunasa.skinlayers.skin.SkinMeshCache
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.render.model.ModelPart
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object PlayerLayerBinder {
    private const val PLAYER_SKULL_META = 3
    private const val HELMET_SLOT = 3

    @JvmStatic
    var firstPerson: Boolean = false

    @JvmStatic
    fun bind(model: PlayerEntityModel, player: AbstractClientPlayerEntity, slim: Boolean) {
        clear(model)

        val client = MinecraftClient.getInstance()
        if (client.world == null) return

        if (!firstPerson) {
            val camera = client.cameraEntity ?: return
            val lod = SkinLayers.config.renderDistanceLOD.toDouble()
            if (player.squaredDistanceTo(camera) > lod * lod) return
        }

        val meshes = SkinMeshCache.forPlayer(player.skinTexture, slim) ?: return
        val config = SkinLayers.config

        if (config.enableHat && !wearsMobHead(player)) attach(model.hat, meshes.head, MeshOffset.HEAD)
        if (config.enableJacket) attach(model.jacket, meshes.body, MeshOffset.BODY)
        if (config.enableLeftSleeve) attach(model.leftSleeve, meshes.leftArm, armOffset(mirrored = false, slim = slim))
        if (config.enableRightSleeve) attach(model.rightSleeve, meshes.rightArm, armOffset(mirrored = true, slim = slim))
        if (config.enableLeftPants) attach(model.leftPants, meshes.leftLeg, MeshOffset.LEG)
        if (config.enableRightPants) attach(model.rightPants, meshes.rightLeg, MeshOffset.LEG)
    }

    fun clear(model: PlayerEntityModel) {
        attach(model.hat, null, null)
        attach(model.jacket, null, null)
        attach(model.leftSleeve, null, null)
        attach(model.rightSleeve, null, null)
        attach(model.leftPants, null, null)
        attach(model.rightPants, null, null)
    }

    private fun armOffset(mirrored: Boolean, slim: Boolean) =
        MeshOffset.arm(mirrored = mirrored, slim = slim, firstPerson = firstPerson)

    private fun attach(part: ModelPart?, mesh: CompiledMesh?, offset: MeshOffset?) {
        (part as? MeshHost)?.attach(mesh, offset)
    }

    private val AbstractClientPlayerEntity.skinTexture: Identifier
        get() = capeId

    private fun wearsMobHead(player: AbstractClientPlayerEntity): Boolean {
        val helmet = player.getArmorSlot(HELMET_SLOT) ?: return false
        return helmet.item === Items.SKULL && helmet.data != PLAYER_SKULL_META
    }
}
