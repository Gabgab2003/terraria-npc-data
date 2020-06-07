import com.beust.klaxon.*
import java.io.File

fun main() {
    val npcs = read()

    val outputFile = File("npcdata.json")
    val outputMapFile = File("npcdatamap.json")

    outputFile.createNewFile()
    outputFile.writeText(toPrettyJsonString(npcs))

    val npcMap = npcs.map {
        it.name to it
    }.toMap()

    outputMapFile.createNewFile()
    outputMapFile.writeText(toPrettyJsonString(npcMap))
}

fun read(): List<Npc> {
    val lines = String(object {}.javaClass.getResourceAsStream("npcdata.csv").readBytes()).trim().lines()
    return lines.subList(1, lines.size).map {
        val parts = it.split(",")
        Npc(
                name = parts[0],
                neighbours = Preferences(
                        loves = parts[1].split("/").filterNot(String::isBlank),
                        likes = parts[2].split("/").filterNot(String::isBlank),
                        dislikes = parts[3].split("/").filterNot(String::isBlank),
                        hates = parts[4].split("/").filterNot(String::isBlank)
                ),
                biomes = Preferences(
                        loves = parts[5].split("/").filterNot(String::isBlank),
                        likes = parts[6].split("/").filterNot(String::isBlank),
                        dislikes = parts[7].split("/").filterNot(String::isBlank),
                        hates = parts[8].split("/").filterNot(String::isBlank)
                )
        )
    }
}

fun npcToCsv(npc: Npc) = with(npc) {
    listOf(
            name,
            neighbours.loves.joinToString("/"),
            neighbours.likes.joinToString("/"),
            neighbours.dislikes.joinToString("/"),
            neighbours.hates.joinToString("/"),
            biomes.loves.joinToString("/"),
            biomes.likes.joinToString("/"),
            biomes.dislikes.joinToString("/"),
            biomes.hates.joinToString("/")
    ).joinToString(",")
}

fun toPrettyJsonString(value: Any): String {
    val builder = StringBuilder(Klaxon().toJsonString(value))
    return (Parser.default().parse(builder) as JsonBase).toJsonString(true)
}

data class Npc(
        @Json(index = 0) val name: String,
        @Json(index = 1) val neighbours: Preferences,
        @Json(index = 2) val biomes: Preferences
)

data class Preferences(
        @Json(index = 0) val loves: List<String>,
        @Json(index = 1) val likes: List<String>,
        @Json(index = 2) val dislikes: List<String>,
        @Json(index = 3) val hates: List<String>
)