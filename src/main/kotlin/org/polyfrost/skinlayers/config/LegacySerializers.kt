package org.polyfrost.skinlayers.config

import net.ornithemc.osl.config.api.serdes.FileSerializerType
import net.ornithemc.osl.config.api.serdes.SerializerTypes
import net.ornithemc.osl.config.api.serdes.config.ConfigSerializers
import net.ornithemc.osl.core.api.json.JsonFile
import org.polyfrost.skinlayers.SkinLayers

object LegacySerializers {
    val FLAT_JSON: FileSerializerType<JsonFile> =
        SerializerTypes.register("${SkinLayers.MOD_ID}:flat_json", FileSerializerType { JsonFile(it) })

    fun register() {
        ConfigSerializers.register(FLAT_JSON, LegacyJsonConfigSerializer())
    }
}
