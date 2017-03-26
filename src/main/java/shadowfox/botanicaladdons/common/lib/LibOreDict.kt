package shadowfox.botanicaladdons.common.lib

/**
 * @author WireSegal
 * Created at 9:30 AM on 4/29/16.
 */
object LibOreDict {

    val COLORS = arrayOf("White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray",
            "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black", "Rainbow")

    val IRIS_DYES = Array(COLORS.size, { "irisDye" + COLORS[it] })
    val DYES = Array(COLORS.size, { "dye" + COLORS[it] })

    val IRIS_DYE = "irisDye"
    val IRIS_DYE_AWAKENED = "irisDyeAwakened"
    val HOLY_SYMBOL = "holySymbol"

    val THUNDERSTEEL = "ingotThundersteel"
    val THUNDERSTEEL_NUGGET = "nuggetThundersteel"
    val THUNDERSTEEL_AWAKENED = "ingotThundersteelAwakened"

    val LIFE_ROOT = "lifeRoot"
    val LIFE_ROOT_AWAKENED = "lifeRootAwakened"

    val AQUAMARINE = "gemAquamarine"
    val AQUAMARINE_AWAKENED = "gemAquamarineAwakened"

    val HEARTHSTONE = "hearthstone"
    val HEARTHSTONE_AWAKENED = "hearthstoneAwakened"

    val BLOCK_AQUAMARINE = "blockAquamarine"
    val BLOCK_THUNDERSTEEL = "blockThundersteel"

    val BLOCK_MANASTEEL = "blockManasteel"
    val BLOCK_TERRASTEEL = "blockTerrasteel"
    val BLOCK_ELEMENTIUM = "blockElvenElementium"

    val THUNDER_PLANKS = "plankThunder"
    val SEAL_PLANKS = "plankSeal"
    val CIRCUIT_PLANKS = "plankCircuit"
    val CALICO_PLANKS = "plankCalico"

    val HOLY_MATERIALS = arrayOf(IRIS_DYE, THUNDERSTEEL, LIFE_ROOT, AQUAMARINE)

    val IRIS_DIRT = "irisDirt"
}
