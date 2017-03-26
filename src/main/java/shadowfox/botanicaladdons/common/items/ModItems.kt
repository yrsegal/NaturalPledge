package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.items.base.ItemRainbow
import shadowfox.botanicaladdons.common.items.bauble.ItemDivineCloak
import shadowfox.botanicaladdons.common.items.bauble.ItemIronBelt
import shadowfox.botanicaladdons.common.items.bauble.ItemSymbol
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.colored.ItemAwakenedDye
import shadowfox.botanicaladdons.common.items.colored.ItemLightPlacer
import shadowfox.botanicaladdons.common.items.colored.ItemManaDye
import shadowfox.botanicaladdons.common.items.sacred.ItemFateHorn
import shadowfox.botanicaladdons.common.items.sacred.ItemImmortalApple
import shadowfox.botanicaladdons.common.items.sacred.ItemMjolnir
import shadowfox.botanicaladdons.common.items.sacred.ItemSealerArrow
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemFoodBelt
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt
import shadowfox.botanicaladdons.common.items.travel.stones.*
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
    val deathFinder: ItemMod

    val fists: ItemMod

    val mjolnir: ItemMod
    val sealArrow: Item
    val apple: Item
    val fateHorn: ItemMod

    val iridescentDye: ItemMod
    val awakenedDye: ItemMod
    val manaDye: ItemManaDye

    val resource: ItemMod

    val cloak: ItemMod

    val ironBelt: ItemIronBelt

    val xpTome: ItemXPStealer

    val corporeaFocus: ItemCorporeaFocus
    val sleepStone: ItemSleepStone
    val portalStone: ItemPortalStone
    val polyStone: ItemPolyStone

    lateinit var gaiaKiller: ItemMod

    init {

        IPreventBreakInCreative

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
        deathFinder = ItemDeathCompass(LibNames.DEATH_FINDER)

        fists = ItemThunderFists(LibNames.THUNDERFIST)

        mjolnir = ItemMjolnir(LibNames.MJOLNIR)
        sealArrow = ItemSealerArrow(LibNames.SEAL_ARROW)
        apple = ItemImmortalApple(LibNames.APPLE)
        fateHorn = ItemFateHorn(LibNames.FATE_HORN)

        iridescentDye = ItemRainbow(LibNames.IRIDESCENT_DYE, true).mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey(LibOreDict.IRIS_DYE).mapOreKey("dye")
        awakenedDye = ItemAwakenedDye(LibNames.IRIDESCENT_DYE_AWAKENED).mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey(LibOreDict.IRIS_DYE).mapOreKey(LibOreDict.IRIS_DYE_AWAKENED).mapOreKey("dye")
        manaDye = ItemManaDye(LibNames.INFINITE_DYE).mapOreDict(LibOreDict.DYES).mapOreDict(LibOreDict.IRIS_DYES).mapOreKey("dye")

        resource = ItemResource(LibNames.RESOURCE)

        cloak = ItemDivineCloak(LibNames.DIVINE_CLOAK)

        ironBelt = ItemIronBelt(LibNames.IRON_BELT)

        xpTome = ItemXPStealer(LibNames.XP_TOME)
        corporeaFocus = ItemCorporeaFocus(LibNames.CORPOREA_FOCUS)
        sleepStone = ItemSleepStone(LibNames.SLEEP_STONE)
        portalStone = ItemPortalStone(LibNames.PORTAL_STONE)
        polyStone = ItemPolyStone(LibNames.POLY_STONE)

        if (BotanicalAddons.DEV_ENVIRONMENT)
            gaiaKiller = ItemGaiaSlayer(LibNames.DEV_ONLY_GAIA_SLAYER)

        OreDictionary.registerOre(LibOreDict.HOLY_SYMBOL, symbol)

        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL_NUGGET, ItemResource.of(ItemResource.Variants.THUNDERNUGGET))
        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL, ItemResource.of(ItemResource.Variants.THUNDER_STEEL))
        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL, ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true))
        OreDictionary.registerOre(LibOreDict.THUNDERSTEEL_AWAKENED, ItemResource.of(ItemResource.Variants.THUNDER_STEEL, true))

        OreDictionary.registerOre(LibOreDict.LIFE_ROOT, ItemResource.of(ItemResource.Variants.LIFE_ROOT))
        OreDictionary.registerOre(LibOreDict.LIFE_ROOT, ItemResource.of(ItemResource.Variants.LIFE_ROOT, true))
        OreDictionary.registerOre(LibOreDict.LIFE_ROOT_AWAKENED, ItemResource.of(ItemResource.Variants.LIFE_ROOT, true))

        OreDictionary.registerOre(LibOreDict.AQUAMARINE, ItemResource.of(ItemResource.Variants.AQUAMARINE))
        OreDictionary.registerOre(LibOreDict.AQUAMARINE, ItemResource.of(ItemResource.Variants.AQUAMARINE, true))
        OreDictionary.registerOre(LibOreDict.AQUAMARINE_AWAKENED, ItemResource.of(ItemResource.Variants.AQUAMARINE, true))

        OreDictionary.registerOre(LibOreDict.HEARTHSTONE, ItemResource.of(ItemResource.Variants.HEARTHSTONE))
        OreDictionary.registerOre(LibOreDict.HEARTHSTONE, ItemResource.of(ItemResource.Variants.HEARTHSTONE, true))
        OreDictionary.registerOre(LibOreDict.HEARTHSTONE_AWAKENED, ItemResource.of(ItemResource.Variants.HEARTHSTONE, true))

        OreDictionary.registerOre("coal", Items.COAL)
        OreDictionary.registerOre("coal", ItemStack(Items.COAL, 1, 1))
    }
}
