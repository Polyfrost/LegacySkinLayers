package org.polyfrost.skinlayers.config

@Suppress("unused")
class SkinLayersConfig {
    var enableHat: Boolean = true
    var enableJacket: Boolean = true
    var enableLeftSleeve: Boolean = true
    var enableRightSleeve: Boolean = true
    var enableLeftPants: Boolean = true
    var enableRightPants: Boolean = true

    var baseVoxelSize: Float = 1.15f
    var bodyVoxelWidthSize: Float = 1.05f
    var headVoxelSize: Float = 1.18f

    var renderDistanceLOD: Int = 14

    var enableSkulls: Boolean = true
    var enableSkullsItems: Boolean = true
    var skullVoxelSize: Float = 1.1f

    var fastRender: Boolean = true

    // retained for compatibility
    var compatibilityMode: Boolean = true
    var irisCompatibilityMode: Boolean = false
    var applySodiumWorkaround: Boolean = false

    var firstPersonPixelScaling: Float = 1.1f
}
