package org.polyfrost.skinlayers

import net.ornithemc.osl.config.api.ConfigManager
import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer
import org.polyfrost.skinlayers.config.LegacySerializers
import org.polyfrost.skinlayers.config.SkinLayersConfig

class SkinLayersEntrypoint : ClientModInitializer {
    override fun initClient() {
        LegacySerializers.register()
        ConfigManager.register(SkinLayersConfig)
    }
}
