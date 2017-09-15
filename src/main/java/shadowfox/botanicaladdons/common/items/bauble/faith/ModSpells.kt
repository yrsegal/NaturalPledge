package shadowfox.botanicaladdons.common.items.bauble.faith

import net.minecraft.block.material.MapColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemStack
import shadowfox.botanicaladdons.api.SpellRegistry
import shadowfox.botanicaladdons.common.BotanicalAddons
import shadowfox.botanicaladdons.common.achievements.ModAchievements
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants
import shadowfox.botanicaladdons.common.block.BlockStorage.Variants.THUNDERSTEEL
import shadowfox.botanicaladdons.common.block.ModBlocks
import shadowfox.botanicaladdons.common.block.trap.BlockBaseTrap
import shadowfox.botanicaladdons.common.items.ItemResource.Companion.of
import shadowfox.botanicaladdons.common.items.ItemResource.Variants.*
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Companion.of
import shadowfox.botanicaladdons.common.items.ItemSpellIcon.Variants.*
import shadowfox.botanicaladdons.common.items.ModItems
import shadowfox.botanicaladdons.common.lib.LibNames
import shadowfox.botanicaladdons.common.lib.LibOreDict
import vazkii.botania.common.lib.LibOreDict as BotaniaOreDict

/**
 * @author WireSegal
 * Created at 9:50 AM on 5/27/16.
 */
object ModSpells {
    init {

        val iridescence = Spells.ObjectInfusion(of(IRIDESCENCE), LibOreDict.DYES[0], ItemStack(ModItems.iridescentDye), ItemStack(ModItems.awakenedDye), 150, MapColor.getBlockColor(EnumDyeColor.byMetadata(0)).colorValue) {
            player, _ -> player.addStat(ModAchievements.iridescence)
        }
        for (i in 1..16) iridescence.addEntry(LibOreDict.DYES[i], ItemStack(ModItems.iridescentDye, 1, i), ItemStack(ModItems.awakenedDye, 1, i), 150, if (i == 16) BotanicalAddons.PROXY.rainbow().rgb else MapColor.getBlockColor(EnumDyeColor.byMetadata(i)).colorValue) {
            player, _ -> player.addStat(ModAchievements.iridescence)
        }
        SpellRegistry.registerSpell(LibNames.SPELL_RAINBOW, iridescence)
        SpellRegistry.registerSpell(LibNames.SPELL_SPHERE, Spells.Heimdall.BifrostWave)

        SpellRegistry.registerSpell(LibNames.SPELL_PROTECTION, Spells.Idunn.Ironroot)
        SpellRegistry.registerSpell(LibNames.SPELL_IDUNN_INFUSION,
                Spells.ObjectInfusion(of(LIFEMAKER), BotaniaOreDict.LIVING_WOOD,
                        of(LIFE_ROOT), of(LIFE_ROOT, true), 150, 0x0FF469, { player, _ -> player.addStat(ModAchievements.createLife) }))

        SpellRegistry.registerSpell(LibNames.SPELL_LEAP, Spells.Njord.Leap)
        SpellRegistry.registerSpell(LibNames.SPELL_INTERDICT, Spells.Njord.Interdict)
        SpellRegistry.registerSpell(LibNames.SPELL_PUSH, Spells.Njord.PushAway)
        SpellRegistry.registerSpell(LibNames.SPELL_NJORD_INFUSION,
                Spells.ObjectInfusion(of(WIND_INFUSION),
                        "gemPrismarine",
                        of(AQUAMARINE), of(AQUAMARINE, true),
                        150, 0x00E5E5, { player, _ -> player.addStat(ModAchievements.createAqua) }).addEntry(
                        "blockPrismarineBrick",
                        ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal), ItemStack(ModBlocks.storage, 1, Variants.AQUAMARINE.ordinal),
                        1350, 0x00E5E5, { player, _ -> player.addStat(ModAchievements.createAqua) }))

        SpellRegistry.registerSpell(LibNames.SPELL_LIGHTNING, Spells.Thor.Lightning)
        SpellRegistry.registerSpell(LibNames.SPELL_STRENGTH, Spells.Thor.Strength)
        SpellRegistry.registerSpell(LibNames.SPELL_PULL, Spells.Thor.Pull)
        SpellRegistry.registerSpell(LibNames.SPELL_THUNDER_TRAP, Spells.Thor.LightningTrap)
        SpellRegistry.registerSpell(LibNames.SPELL_THOR_INFUSION,
                Spells.ObjectInfusion(of(LIGHTNING_INFUSION),
                        "ingotIron",
                        of(THUNDER_STEEL), of(THUNDER_STEEL, true),
                        150, 0xE5DD00, { player, _ -> player.addStat(ModAchievements.createThunder) }).addEntry(
                        "blockIron",
                        ItemStack(ModBlocks.storage, 1, THUNDERSTEEL.ordinal), ItemStack(ModBlocks.storage, 1, THUNDERSTEEL.ordinal),
                        1350, 0xE5DD00, { player, _ -> player.addStat(ModAchievements.createThunder) }).addEntry(
                        "nuggetIron",
                        of(THUNDERNUGGET), of(THUNDERNUGGET),
                        16, 0xE5DD00, { player, _ -> player.addStat(ModAchievements.createThunder) }))

        SpellRegistry.registerSpell(LibNames.SPELL_LOKI_INFUSION,
                Spells.ObjectInfusion(of(FIRE_INFUSION),
                        "coal",
                        of(HEARTHSTONE), of(HEARTHSTONE, true),
                        150, BlockBaseTrap.COLOR, { player, _ -> player.addStat(ModAchievements.createFire) }))
        SpellRegistry.registerSpell(LibNames.SPELL_TRUESIGHT, Spells.Loki.Truesight)
        SpellRegistry.registerSpell(LibNames.SPELL_DISDAIN, Spells.Loki.Disdain)
        SpellRegistry.registerSpell(LibNames.SPELL_FLAME_JET, Spells.Loki.FlameJet)

        SpellRegistry.registerSpell(LibNames.SPELL_SOUL_MANIFESTATION, Spells.ObjectInfusion.UltimateInfusion)
        Spells.ObjectInfusion.allEntries.add(Spells.ObjectInfusion.ObjectInfusionEntry("netherStar", of(GOD_SOUL), of(GOD_SOUL, true), 1000, 0xD3DD85) {
            player, _ -> player.addStat(ModAchievements.createSpirit)
        })
    }
}
