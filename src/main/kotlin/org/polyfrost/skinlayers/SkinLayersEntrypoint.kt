package org.polyfrost.skinlayers

import net.fabricmc.api.ClientModInitializer

class SkinLayersEntrypoint : ClientModInitializer {
    override fun onInitializeClient() {
        SkinLayers.load()
    }
}
