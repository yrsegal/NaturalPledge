package com.wiresegal.naturalpledge.common.lib;

/**
 * @author WireSegal
 *         Created at 10:51 PM on 5/28/16.
 */
public class LibObfuscation {
    // Cooldown handling
    public static final String[] COOLDOWNTRACKER_COOLDOWNS = new String[]{"a", "field_185147_a", "cooldowns"};
    public static final String[] COOLDOWNTRACKER_TICKS = new String[]{"b", "field_185148_b", "ticks"};
    public static final String[] COOLDOWN_EXPIRETICKS = new String[]{"b", "field_185138_b", "expireTicks"};
    public static final String[] COOLDOWN_CREATETICKS = new String[]{"a", "field_185137_a", "createTicks"};

    // Item name handling
    public static final String[] GUIINGAME_REMAININGHIGHLIGHTTICKS = new String[]{"q", "field_92017_k", "remainingHighlightTicks"};

    // Swing handling
    public static final String[] ENTITYLIVINGBASE_TICKSSINCELASTSWING = new String[]{"aE", "field_184617_aD", "ticksSinceLastSwing"};

    // Lightning handling
    public static final String[] ENTITYLIGHTNINGBOLT_EFFECTONLY = new String[]{"d", "field_184529_d", "effectOnly"};

    // Explosion handling
    public static final String[] EXPLOSION_EXPLOSIONSIZE = new String[]{"i", "field_77280_f", "size"};

    // Food handling
    public static final String[] ITEMFOOD_ALWAYSEDIBLE = new String[]{"e", "field_77852_bZ", "alwaysEdible"};

    // Heimdall Cloak
    public static final String[] ENTITYLIVINGBASE_ISJUMPING = new String[]{"be", "field_70703_bu", "isJumping"};
    public static final String[] ENTITYLIVINGBASE_JUMPTICKS = new String[]{"bC", "field_70773_bE", "jumpTicks"};
    public static final String[] NETHANDLERPLAYSERVER_CAPTURECURRENTPOSITION = new String[]{"d", "func_184342_d", "captureCurrentPosition"};
}
