package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants.THUNDERSTEEL
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.items.ItemResource.Companion.of
import shadowfox.botanicaladdons.common.items.ItemResource.Variants.*
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Companion.of
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Variants.*
import shadowfox.botanicaladdons.common.lib.LibNames
import vazkii.botania.common.lib.LibOreDict

/**
 * @author WireSegal
 * Created at 9:50 AM on 5/27/16.
 */
object ModSpells {
    init {

        SpellRegistry.registerSpell(LibNames.SPELL_RAINBOW, Spells.Heimdall.Iridescence)
        SpellRegistry.registerSpell(LibNames.SPELL_SPHERE, Spells.Heimdall.BifrostWave)

        SpellRegistry.registerSpell(LibNames.SPELL_PROTECTION, Spells.Idunn.Ironroot)
        SpellRegistry.registerSpell(LibNames.SPELL_IDUNN_INFUSION,
                Spells.ObjectInfusion(of(LIFEMAKER), LibOreDict.LIVING_WOOD,
                        of(LIFE_ROOT), of(LIFE_ROOT, true), 150, 0x0FF469, { player, entry -> player.addStat(ModAchievements.createLife) }))

        SpellRegistry.registerSpell(LibNames.SPELL_LEAP, Spells.Njord.Leap)
        SpellRegistry.registerSpell(LibNames.SPELL_INTERDICT, Spells.Njord.Interdict)
        SpellRegistry.registerSpell(LibNames.SPELL_PUSH, Spells.Njord.PushAway)
        SpellRegistry.registerSpell(LibNames.SPELL_NJORD_INFUSION,
                Spells.ObjectInfusion(of(WIND_INFUSION),
                        "gemPrismarine",
                        of(AQUAMARINE), of(AQUAMARINE, true),
                        150, 0x00E5E5, { player, entry -> player.addStat(ModAchievements.createAqua) }).addEntry(
                        "blockPrismarineBrick",
                        ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal), ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal),
                        1350, 0x00E5E5, { player, entry -> player.addStat(ModAchievements.createAqua) }))

        SpellRegistry.registerSpell(LibNames.SPELL_LIGHTNING, Spells.Thor.Lightning)
        SpellRegistry.registerSpell(LibNames.SPELL_STRENGTH, Spells.Thor.Strength)
        SpellRegistry.registerSpell(LibNames.SPELL_PULL, Spells.Thor.Pull)
        SpellRegistry.registerSpell(LibNames.SPELL_THUNDER_TRAP, Spells.Thor.LightningTrap)
        SpellRegistry.registerSpell(LibNames.SPELL_THOR_INFUSION,
                Spells.ObjectInfusion(of(LIGHTNING_INFUSION),
                        "ingotIron",
                        of(THUNDER_STEEL), of(THUNDER_STEEL, true),
                        150, 0xE5DD00, { player, entry -> player.addStat(ModAchievements.createThunder) }).addEntry(
                        "blockIron",
                        ItemStack(ModBlocks.storage, 1, THUNDERSTEEL.ordinal), ItemStack(ModBlocks.storage, 1, THUNDERSTEEL.ordinal),
                        1350, 0xE5DD00, { player, entry -> player.addStat(ModAchievements.createThunder) }))
    }
}
