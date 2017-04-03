package shadowfox.botanicaladdons.common.items

import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.oredict.OreDictionary
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.items.armor.ItemEclipseArmor
import shadowfox.botanicaladdons.common.items.armor.ItemFenrisArmor
import shadowfox.botanicaladdons.common.items.armor.ItemSunmakerArmor
import shadowfox.botanicaladdons.common.items.base.IPreventBreakInCreative
import shadowfox.botanicaladdons.common.items.base.ItemRainbow
import shadowfox.botanicaladdons.common.items.bauble.ItemDivineCloak
import shadowfox.botanicaladdons.common.items.bauble.ItemIronBelt
import shadowfox.botanicaladdons.common.items.bauble.ItemSymbol
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemFaithBauble
import shadowfox.botanicaladdons.common.items.bauble.faith.ItemRagnarokPendant
import shadowfox.botanicaladdons.common.items.colored.ItemAwakenedDye
import shadowfox.botanicaladdons.common.items.colored.ItemLightPlacer
import shadowfox.botanicaladdons.common.items.colored.ItemManaDye
import shadowfox.botanicaladdons.common.items.sacred.*
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemFoodBelt
import shadowfox.botanicaladdons.common.items.travel.bauble.ItemToolbelt
import shadowfox.botanicaladdons.common.items.travel.stones.*
import shadowfox.botanicaladdons.common.items.weapons.ItemFlarebringer
import shadowfox.botanicaladdons.common.items.weapons.ItemNightscourge
import shadowfox.botanicaladdons.common.items.weapons.ItemShadowbreaker
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.common.item.ModItems as BotaniaItems

/**
 * @author WireSegal
 * Created at 5:39 PM on 4/13/16.
 */
object ModItems {
    val ECLIPSE = EnumHelper.addArmorMaterial("RAGNAROK.ECLIPSE", "eclipse", 34, intArrayOf(3, 6, 8, 3), 26, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f)!!
    val FLAREBRINGER = EnumHelper.addToolMaterial("RAGNAROK.FLAREBRINGER", 4, 2300, 9F, 4F, 26)!!
    val SUNMAKER = EnumHelper.addArmorMaterial("RAGNAROK.SUNMAKER", "sunmaker", 34, intArrayOf(3, 6, 8, 3), 26, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0f)!!
    val SHADOWBREAKER = EnumHelper.addToolMaterial("RAGNAROK.SHADOWBREAKER", 4, 2300, 9F, 3F, 26)!!
    val FENRIS = EnumHelper.addArmorMaterial("RAGNAROK.FENRIS", "fenris", 34, intArrayOf(3, 6, 8, 3), 26, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 4.0f)!!

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
    val perditionFist: ItemMod

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

    val ragnarok: ItemRagnarokPendant

    val eclipseHelm: ItemEclipseArmor
    val eclipseChest: ItemEclipseArmor
    val eclipseLegs: ItemEclipseArmor
    val eclipseBoots: ItemEclipseArmor

    val flarebringer: ItemFlarebringer

    val sunmakerHelm: ItemSunmakerArmor
    val sunmakerChest: ItemSunmakerArmor
    val sunmakerLegs: ItemSunmakerArmor
    val sunmakerBoots: ItemSunmakerArmor

    val shadowbreaker: ItemShadowbreaker

    val fenrisHelm: ItemFenrisArmor
    val fenrisChest: ItemFenrisArmor
    val fenrisLegs: ItemFenrisArmor
    val fenrisBoots: ItemFenrisArmor

    val nightscourge: ItemNightscourge

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
        perditionFist = ItemPerditionFist(LibNames.PERDITION_FIST)

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

        ragnarok = ItemRagnarokPendant(LibNames.RAGNAROK_EMBLEM)

        eclipseHelm = ItemEclipseArmor(LibNames.ECLIPSE_HELM, EntityEquipmentSlot.HEAD)
        eclipseChest = ItemEclipseArmor(LibNames.ECLIPSE_CHEST, EntityEquipmentSlot.CHEST)
        eclipseLegs = ItemEclipseArmor(LibNames.ECLIPSE_LEGS, EntityEquipmentSlot.LEGS)
        eclipseBoots = ItemEclipseArmor(LibNames.ECLIPSE_BOOTS, EntityEquipmentSlot.FEET)

        flarebringer = ItemFlarebringer(LibNames.FLAREBRINGER, FLAREBRINGER)

        sunmakerHelm = ItemSunmakerArmor(LibNames.SUNMAKER_HELM, EntityEquipmentSlot.HEAD)
        sunmakerChest = ItemSunmakerArmor(LibNames.SUNMAKER_CHEST, EntityEquipmentSlot.CHEST)
        sunmakerLegs = ItemSunmakerArmor(LibNames.SUNMAKER_LEGS, EntityEquipmentSlot.LEGS)
        sunmakerBoots = ItemSunmakerArmor(LibNames.SUNMAKER_BOOTS, EntityEquipmentSlot.FEET)

        shadowbreaker = ItemShadowbreaker(LibNames.SHADOWBREAKER, SHADOWBREAKER)

        fenrisHelm = ItemFenrisArmor(LibNames.FENRIS_HELM, EntityEquipmentSlot.HEAD)
        fenrisChest = ItemFenrisArmor(LibNames.FENRIS_CHEST, EntityEquipmentSlot.CHEST)
        fenrisLegs = ItemFenrisArmor(LibNames.FENRIS_LEGS, EntityEquipmentSlot.LEGS)
        fenrisBoots = ItemFenrisArmor(LibNames.FENRIS_BOOTS, EntityEquipmentSlot.FEET)

        nightscourge = ItemNightscourge(LibNames.NIGHTSCOURGE)

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
