package logikcull.loadfiles

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class XlfLoadfile(private val pathname: String): Loadfile(pathname) {
    private val kotlinXmlMapper = XmlMapper(JacksonXmlModule().apply {
        setDefaultUseWrapper(false)
    }).registerKotlinModule()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun entries(): List<LoadfileEntry> {
        val xlf = parse()
        return xlf.xlfEntriesObject.entries.map { xlfEntry ->
            LoadfileEntry(xlfEntry.controlNumber, xlfEntry.volume, xlfEntry.imagePath + xlfEntry.imageName)
        }
    }

    private fun parse(): XlfLoadfileRoot {
        val xmlString = reader.readText()
        return kotlinXmlMapper.readValue(xmlString)
    }
}

@JsonRootName("loadfile")
data class XlfLoadfileRoot(
        @JsonProperty("entries")
        val xlfEntriesObject: XlfEntries
)

@JsonRootName("entries")
data class XlfEntries(
        @JsonProperty("entry")
        val entries: List<XlfEntry>
)

@JsonRootName("entry")
data class XlfEntry(
        @JsonProperty("control-number")
        val controlNumber: String = "",

        @JsonProperty("volume")
        val volume: String,

        @JsonProperty("image-path")
        val imagePath: String,

        @JsonProperty("image-name")
        val imageName: String
)