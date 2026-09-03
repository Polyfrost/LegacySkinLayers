package org.polyfrost.skinlayers.config

import net.ornithemc.osl.config.api.config.Config
import net.ornithemc.osl.config.api.config.option.BooleanOption
import net.ornithemc.osl.config.api.config.option.FloatOption
import net.ornithemc.osl.config.api.config.option.IntegerOption
import net.ornithemc.osl.config.api.config.option.Option
import net.ornithemc.osl.config.api.serdes.SerializationSettings
import net.ornithemc.osl.config.api.serdes.config.ConfigSerializer
import net.ornithemc.osl.core.api.json.JsonFile

class LegacyJsonConfigSerializer : ConfigSerializer<JsonFile> {
    override fun serialize(config: Config, settings: SerializationSettings, json: JsonFile) {
        json.use {
            it.write()
            for ((key, option) in entriesOf(config)) {
                when (option) {
                    is BooleanOption -> it.writeBoolean(key, option.get())
                    is FloatOption -> it.writeNumber(key, option.get())
                    is IntegerOption -> it.writeNumber(key, option.get())
                }
            }
        }
    }

    override fun deserialize(config: Config, settings: SerializationSettings, json: JsonFile) {
        val options = entriesOf(config).toMap()
        json.use {
            it.read()
            while (it.hasNext()) {
                when (val option = options[it.readName()]) {
                    is BooleanOption -> option.set(it.readBoolean())
                    is FloatOption -> option.set(it.readNumber().toFloat())
                    is IntegerOption -> option.set(it.readNumber().toInt())
                    else -> it.skipValue()
                }
            }
        }
    }

    private fun entriesOf(config: Config): List<Pair<String, Option>> =
        (config as SkinLayersConfig).legacyEntries
}
