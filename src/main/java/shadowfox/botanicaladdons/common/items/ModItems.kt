package shadowfox.botanicaladdons.common.items

import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.api.lib.LibMisc
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.base.ItemMod
import shadowfox.botanicaladdons.common.items.base.ItemRainbow
import shadowfox.botanicaladdons.common.items.bauble.ItemSymbol
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.colored.ItemAwakenedDye
import shadowfox.botanicaladdons.common.items.colored.ItemLightPlacer
import shadowfox.botanicaladdons.common.items.colored.ItemManaDye
import shadowfox.botanicaladdons.common.items.sacred.ItemDagger
import shadowfox.botanicaladdons.common.items.sacred.ItemFateHorn
import shadowfox.botanicaladdons.common.items.sacred.ItemImmortalApple
import shadowfox.botanicaladdons.common.items.sacred.ItemMjolnir
import shadowfox.botanicaladdons.common.items.travel.ItemTravelstone
import shadowfox.botanicaladdons.common.items.travel.ItemWaystone
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemFoodBelt
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 5:39 PM on 4/13/16.
 */
object ModItems {
    val emblem: ItemMod
    val symbol: ItemMod
    val spellIcon: ItemMod
    val spellFocus: ItemMod

    val mortalStone: ItemMod

    val travelStone: ItemMod
    val toolbelt: ItemMod
    val foodbelt: ItemMod
    val lightPlacer: ItemMod
    val finder: ItemMod

    val fists: ItemMod

    val mjolnir: ItemMod
    val dagger: ItemMod
    val apple: Item
    val fateHorn: ItemMod

    val iridescentDye: ItemMod
    val awakenedDye: ItemMod
    val manaDye: ItemManaDye

    val resource: ItemMod

    val mjolnirMaterial: Item.ToolMaterial
    val daggerMaterial: Item.ToolMaterial
    val thunderMaterial: Item.ToolMaterial

    lateinit var gaiaKiller: ItemMod

    init {
        mjolnirMaterial = EnumHelper.addToolMaterial("${LibMisc.MOD_ID}:MJOLNIR", 3, 1561, 9.0f, 8.0f, 26).setRepairItem(ItemStack(Items.IRON_INGOT))
        thunderMaterial = EnumHelper.addToolMaterial("${LibMisc.MOD_ID}:THUNDER", 3, 1561, 9.0f, 2.5f, 14).setRepairItem(ItemStack(Items.IRON_INGOT))
        daggerMaterial = EnumHelper.addToolMaterial("${LibMisc.MOD_ID}:DAGGER", 3, 1561, 9.0f, 0.0f, 14).setRepairItem(ItemStack(BotaniaItems.manaResource, 1, 7)) // Elementium

        emblem = ItemFaithBauble(LibNames.PRIESTLY_EMBLEM)
        symbol = ItemSymbol(LibNames.HOLY_SYMBOL)
        spellIcon = ItemSpellIcon(LibNames.SPELL_ICON)
        spellFocus = ItemTerrestrialFocus(LibNames.SPELL_FOCUS)

        mortalStone = ItemMortalstone(LibNames.MORTAL_STONE)

        travelStone = ItemTravelstone(LibNames.TRAVEL_STONE)
        toolbelt = ItemToolbelt(LibNames.TOOLBELT)
        foodbelt = ItemFoodBelt(LibNames.FOODBELT)
        lightPlacer = ItemLightPlacer(LibNames.LIGHT_PLACER)
        finder = ItemWaystone(LibNames.FINDER)

        fists = ItemThunderFists(LibNames.THUNDERFIST, thunderMaterial)

        mjolnir = ItemMjolnir(LibNames.MJOLNIR, mjolnirMaterial)
        dagger = ItemDagger(LibNames.DAGGER, daggerMaterial)
        apple = ItemImmortalApple(LibNames.APPLE)
        fateHorn = ItemFateHorn(LibNames.FATE_HORN)

        iridescentDye = ItemRainbow(LibNames.IRIDESCENT_DYE, true).mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey(LibOreDict.IRIS_DYE)
        awakenedDye = ItemAwakenedDye(LibNames.IRIDESCENT_DYE_AWAKENED).mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey(LibOreDict.IRIS_DYE).mapOreKey(LibOreDict.IRIS_DYE_AWAKENED)
        manaDye = ItemManaDye(LibNames.INFINITE_DYE)
        manaDye.mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey(LibOreDict.IRIS_DYE)

        resource = ItemResource(LibNames.RESOURCE)

        if (BotanicalAddons.isDevEnv)
            gaiaKiller = ItemGaiaSlayer("gaiaKiller")

        OreDictionary.registerOre(LibOreDict.HOLY_SYMBOL, symbol)

        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL, ItemResource.of(ItemResource.Variants.THUNDER_STEEL))
        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL, ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true))
        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL_AWAKENED, ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true))

        OreDictionary.registerOre(LibOreDict.LIFE_ROOT, ItemResource.of(ItemResource.Variants.LIFE_ROOT))
        OreDictionary.registerOre(LibOreDict.LIFE_ROOT, ItemResource.of(ItemResource.Variants.LIFE_ROOT, true))
        OreDictionary.registerOre(LibOreDict.LIFE_ROOT_AWAKENED, ItemResource.of(ItemResource.Variants.LIFE_ROOT, true))

        OreDictionary.registerOre(LibOreDict.AQUAMARINE, ItemResource.of(ItemResource.Variants.AQUAMARINE))
        OreDictionary.registerOre(LibOreDict.AQUAMARINE, ItemResource.of(ItemResource.Variants.AQUAMARINE, true))
        OreDictionary.registerOre(LibOreDict.AQUAMARINE_AWAKENED, ItemResource.of(ItemResource.Variants.AQUAMARINE, true))
    }
}
