package org.polyfrost.skinlayers.render

enum class MeshShape(val yOffset: Float, val inset: Float) {
    HEAD(0f, 0f),
    BODY(-0.2f, 0f),
    LEG(-0.2f, 0f),
    ARM(-0.1f, 0.998f),
    SLIM_ARM(-0.1f, 0.499f),
}
