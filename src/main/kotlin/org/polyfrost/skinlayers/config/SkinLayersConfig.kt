package org.polyfrost.skinlayers.config

import net.ornithemc.osl.config.api.ConfigScope
import net.ornithemc.osl.config.api.LoadingPhase
import net.ornithemc.osl.config.api.config.BaseConfig
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.config.option.FloatOption
import net.ornithemc.osl.config.api.config.option.IntegerOption
import net.ornithemc.osl.config.api.config.option.Option
import net.ornithemc.osl.config.api.config.option.validator.IntegerValidators
import net.ornithemc.osl.config.api.serdes.FileSerializerType
import java.util.function.Predicate

object SkinLayersConfig : BaseConfig() {

    private const val LAYERS = "Layers"
    private const val SCALING = "Scaling"
    private const val SKULLS = "Skulls"
    private const val RENDERING = "Rendering"
    private const val COMPATIBILITY = "Compatibility"

    private val positive = Predicate<Float> { it > 0f }

    val enableHat = BooleanOption("Hat", "Render the hat layer in 3d.", true)
    val enableJacket = BooleanOption("Jacket", "Render the jacket layer in 3d.", true)
    val enableLeftSleeve = BooleanOption("Left Sleeve", "Render the left sleeve layer in 3d.", true)
    val enableRightSleeve = BooleanOption("Right Sleeve", "Render the right sleeve layer in 3d.", true)
    val enableLeftPants = BooleanOption("Left Pants Leg", "Render the left trouser layer in 3d.", true)
    val enableRightPants = BooleanOption("Right Pants Leg", "Render the right trouser layer in 3d.", true)

    val baseVoxelSize = FloatOption("Base Voxel Size", "How far arm, leg and torso layers sit off the body.", 1.15f, positive)
    val bodyVoxelWidthSize = FloatOption("Body Voxel Width", "Width scaling of the jacket layer.", 1.05f, positive)
    val headVoxelSize = FloatOption("Head Voxel Size", "How far the hat layer sits off the head.", 1.18f, positive)
    val firstPersonPixelScaling = FloatOption("First Person Scaling", "Sleeve scaling on the first person arm.", 1.1f, positive)

    val enableSkulls = BooleanOption("Skulls", "Give placed and worn player heads a 3d hat layer.", true)
    val enableSkullsItems = BooleanOption("Skull Items", "Give player heads rendered as items a 3d hat layer.", true)
    val skullVoxelSize = FloatOption("Skull Voxel Size", "How far the hat layer sits off a player head.", 1.1f, positive)

    val renderDistanceLOD = IntegerOption("Render Distance", "Layers stop rendering past this many blocks.", 14, IntegerValidators.min(0))
    val fastRender = BooleanOption("Fast Render", "Draw outward faces as one box per layer instead of one quad per pixel.", true)

    val compatibilityMode = BooleanOption("Compatibility Mode", "Unused on 1.8.9.", true)
    val irisCompatibilityMode = BooleanOption("Iris Compatibility Mode", "Unused on 1.8.9.", false)
    val applySodiumWorkaround = BooleanOption("Sodium Workaround", "Unused on 1.8.9.", false)

    val legacyEntries: List<Pair<String, Option>> = listOf(
        "enableHat" to enableHat,
        "enableJacket" to enableJacket,
        "enableLeftSleeve" to enableLeftSleeve,
        "enableRightSleeve" to enableRightSleeve,
        "enableLeftPants" to enableLeftPants,
        "enableRightPants" to enableRightPants,
        "baseVoxelSize" to baseVoxelSize,
        "bodyVoxelWidthSize" to bodyVoxelWidthSize,
        "headVoxelSize" to headVoxelSize,
        "renderDistanceLOD" to renderDistanceLOD,
        "enableSkulls" to enableSkulls,
        "enableSkullsItems" to enableSkullsItems,
        "skullVoxelSize" to skullVoxelSize,
        "fastRender" to fastRender,
        "compatibilityMode" to compatibilityMode,
        "irisCompatibilityMode" to irisCompatibilityMode,
        "applySodiumWorkaround" to applySodiumWorkaround,
        "firstPersonPixelScaling" to firstPersonPixelScaling,
    )

    override fun getNamespace(): String? = null

    override fun getName(): String = "Legacy Skin Layers"

    override fun getSaveName(): String = "skinlayers.json"

    override fun getScope(): ConfigScope = ConfigScope.GLOBAL

    override fun getLoadingPhase(): LoadingPhase = LoadingPhase.START

    override fun getType(): FileSerializerType<*> = LegacySerializers.FLAT_JSON

    override fun getVersion(): Int = 0

    override fun init() {
        registerOptions(LAYERS, enableHat, enableJacket, enableLeftSleeve, enableRightSleeve, enableLeftPants, enableRightPants)
        registerOptions(SCALING, baseVoxelSize, bodyVoxelWidthSize, headVoxelSize, firstPersonPixelScaling)
        registerOptions(SKULLS, enableSkulls, enableSkullsItems, skullVoxelSize)
        registerOptions(RENDERING, renderDistanceLOD, fastRender)
        registerOptions(COMPATIBILITY, compatibilityMode, irisCompatibilityMode, applySodiumWorkaround)
    }
}
