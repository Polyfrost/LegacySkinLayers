package org.polyfrost.skinlayers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.polyfrost.skinlayers.config.SkinLayersConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.nio.charset.StandardCharsets

object SkinLayers {
    const val MOD_ID = "legacy-skin-layers"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    private val configFile = File("config", "skinlayers.json")
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    var config: SkinLayersConfig = SkinLayersConfig()
        private set

    fun load() {
        val loaded = runCatching {
            if (!configFile.isFile) return@runCatching null
            gson.fromJson(configFile.readText(StandardCharsets.UTF_8), SkinLayersConfig::class.java)
        }.onFailure { LOGGER.error("Could not read {}, falling back to defaults.", configFile, it) }
            .getOrNull()

        if (loaded != null) {
            config = loaded
        } else {
            save()
        }
    }

    fun save() {
        runCatching {
            configFile.parentFile?.mkdirs()
            configFile.writeText(gson.toJson(config), StandardCharsets.UTF_8)
        }.onFailure { LOGGER.error("Could not write {}.", configFile, it) }
    }
}
