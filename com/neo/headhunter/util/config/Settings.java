package com.neo.headhunter.util.config;

import com.neo.headhunter.HeadHunter;
import org.bukkit.configuration.file.FileConfiguration;

public final class Settings {
    private static boolean
            hoardMode, usePercentage, ignoreWorlds,
            sell_notify, sell_broadcast, sell_console, sell_signOnly,
            head_value_useBalance, head_value_useBounty, head_value_cumulative,
            drop_onlyWithBounty, drop_fireTick, drop_anyCause,
			drop_weapon_swords, drop_weapon_bows, drop_weapon_axes,
		    drop_weapon_pickaxes, drop_weapon_spades, drop_weapon_hoes, drop_weapon_rods, drop_weapon_fists,
            bounty_godfatherMode, bounty_notify, bounty_broadcast, bounty_console,
            enchant_looting_enabled, enchant_custom_enabled, enchant_custom_allowedBows, enchant_custom_allowedTools,
            support_factions_dropWilderness, support_factions_dropWarzone, support_factions_dropSafezone,
            support_minigames_dropGame, support_mobstacker_dropEach;

    private static double
            sell_rate,
            head_value_minWorth,
            bounty_minimum,
            enchant_custom_dropRate;

    private static int
            drop_chance,
            bounty_listSize,
            enchant_custom_chance, enchant_custom_minLevels, enchant_custom_maxLevels,
            enchant_custom_maxOthers;
    
    private static long drop_defaultCooldown;

    private static String
            head_format_title, head_format_worth, head_format_worthless,
            sign_selling_1, sign_selling_2, sign_selling_3, sign_selling_4,
            sign_wanted_1, sign_wanted_2, sign_wanted_3, sign_wanted_4,
            enchant_looting_map;

    public static void load(HeadHunter plugin) {
        FileConfiguration config = plugin.getConfig();
        hoardMode = config.getBoolean("hoard-mode", false);
        usePercentage = config.getBoolean("use-percentage", true);
        ignoreWorlds = config.getBoolean("ignore-worlds", true);

        sell_rate = config.getDouble("sell.rate", 10.0);
        sell_notify = config.getBoolean("sell.notify", true);
        sell_broadcast = config.getBoolean("sell.broadcast", true);
        sell_console = config.getBoolean("sell.console", false);
        sell_signOnly = config.getBoolean("sell.sign-only", false);

        head_value_useBalance = config.getBoolean("head.value.use-balance", true);
        head_value_useBounty = config.getBoolean("head.value.use-bounty", true);
        head_value_cumulative = config.getBoolean("head.value.cumulative", false);
        head_value_minWorth = config.getDouble("head.value.min-worth", 0.0);
        head_format_title = config.getString("head.format.title", "&a{TARGET}''s Head");
        head_format_worth = config.getString("head.format.worth", "&eSell Price:&a ${VALUE}");
        head_format_worthless = config.getString("head.format.worthless", "&eSell Price: &cWorthless");

        drop_chance = config.getInt("drop.chance", 100);
        drop_onlyWithBounty = config.getBoolean("drop.only-with-bounty", false);
        drop_fireTick = config.getBoolean("drop.fire-tick", false);
        drop_anyCause = config.getBoolean("drop.any-cause", false);
        drop_defaultCooldown = config.getLong("drop.default-cooldown", 0);
        
        drop_weapon_swords = config.getBoolean("drop.weapon.swords", true);
        drop_weapon_bows = config.getBoolean("drop.weapon.bows", true);
	    drop_weapon_axes = config.getBoolean("drop.weapon.axes", true);
	    drop_weapon_pickaxes = config.getBoolean("drop.weapon.pickaxes", false);
	    drop_weapon_spades = config.getBoolean("drop.weapon.spades", false);
	    drop_weapon_hoes = config.getBoolean("drop.weapon.hoes", false);
	    drop_weapon_rods = config.getBoolean("drop.weapon.rods", false);
	    drop_weapon_fists = config.getBoolean("drop.weapon.fists", false);

        bounty_listSize = config.getInt("bounty.list-size", -1);
        bounty_minimum = config.getDouble("bounty.minimum", 20.0);
        bounty_godfatherMode = config.getBoolean("bounty.godfather-mode", false);
        bounty_notify = config.getBoolean("bounty.notify", true);
        bounty_broadcast = config.getBoolean("bounty.broadcast", true);
	    bounty_console = config.getBoolean("bounty.console", false);

        sign_selling_1 = config.getString("sign.selling.1", "--&4&lSell Heads&r--");
        sign_selling_2 = config.getString("sign.selling.2", "Right-click to");
        sign_selling_3 = config.getString("sign.selling.3", "sell heads");
        sign_selling_4 = config.getString("sign.selling.4", "---------------");

        sign_wanted_1 = config.getString("sign.wanted.1", "----&4&lWanted&r----");
        sign_wanted_2 = config.getString("sign.wanted.2", "{TARGET}");
        sign_wanted_3 = config.getString("sign.wanted.3", "${VALUE}");
        sign_wanted_4 = config.getString("sign.wanted.4", "---------------");

        enchant_looting_enabled = config.getBoolean("enchant.looting.enabled", false);
        enchant_looting_map = config.getString("enchant.looting.level-multiplier-map", "1:1.1/2:1.3/3:1.5");
        enchant_custom_enabled = config.getBoolean("enchant.custom.enabled", false);
        enchant_custom_chance = config.getInt("enchant.custom.chance", 10);
        enchant_custom_minLevels = config.getInt("enchant.custom.min-levels", 20);
        enchant_custom_maxLevels = config.getInt("enchant.custom.max-levels", 30);
        enchant_custom_maxOthers = config.getInt("enchant.custom.max-other-enchantments", 2);
        enchant_custom_allowedBows = config.getBoolean("enchant.custom.allowed-bows", true);
        enchant_custom_allowedTools = config.getBoolean("enchant.custom.allowed-tools", false);
        enchant_custom_dropRate = config.getDouble("enchant.custom.drop-rate", -1);

        support_factions_dropWilderness = config.getBoolean("support.factions.drop-wilderness", true);
        support_factions_dropWarzone = config.getBoolean("support.factions.drop-warzone", false);
        support_factions_dropSafezone = config.getBoolean("support.factions.drop-safezone", false);
        support_minigames_dropGame = config.getBoolean("support.minigames.drop-game", false);
        support_mobstacker_dropEach = config.getBoolean("support.mobstacker.drop-each", true);
    }

    public static boolean isHoardMode() {
        return hoardMode;
    }

    public static boolean isUsePercentage() {
        return usePercentage;
    }

    public static boolean isIgnoreWorlds() {
        return ignoreWorlds;
    }
	
	//SELL
    public static double getSell_rate() {
        return sell_rate;
    }

    public static boolean isSell_notify() {
        return sell_notify;
    }

    public static boolean isSell_broadcast() {
        return sell_broadcast;
    }
	
	public static boolean isSell_console() {
		return sell_console;
	}
	
	public static boolean isSell_signOnly() {
		return sell_signOnly;
	}
	
	//HEAD_VALUE
    public static boolean isHead_value_useBalance() {
        return head_value_useBalance;
    }

    public static boolean isHead_value_useBounty() {
        return head_value_useBounty;
    }

    public static boolean isHead_value_cumulative() {
        return head_value_cumulative;
    }

    public static double getHead_value_minWorth() {
        return head_value_minWorth;
    }

    //HEAD_FORMAT
    public static String getHead_format_title() {
        return head_format_title;
    }

    public static String getHead_format_worth() {
        return head_format_worth;
    }

    public static String getHead_format_worthless() {
        return head_format_worthless;
    }

    //DROP
    public static int getDrop_chance() {
        return drop_chance;
    }

    public static boolean isDrop_onlyWithBounty() {
        return drop_onlyWithBounty;
    }

    public static boolean isDrop_fireTick() {
        return drop_fireTick;
    }

    public static boolean isDrop_anyCause() {
        return drop_anyCause;
    }
	
	public static long getDrop_defaultCooldown() {
		return drop_defaultCooldown;
	}
	
	//DROP_WEAPON
	public static boolean isDrop_weapon_swords() {
		return drop_weapon_swords;
	}
	
	public static boolean isDrop_weapon_bows() {
		return drop_weapon_bows;
	}
	
	public static boolean isDrop_weapon_axes() {
		return drop_weapon_axes;
	}
	
	public static boolean isDrop_weapon_pickaxes() {
		return drop_weapon_pickaxes;
	}
	
	public static boolean isDrop_weapon_spades() {
		return drop_weapon_spades;
	}
	
	public static boolean isDrop_weapon_hoes() {
		return drop_weapon_hoes;
	}
	
	public static boolean isDrop_weapon_rods() {
		return drop_weapon_rods;
	}
	
	public static boolean isDrop_weapon_fists() {
		return drop_weapon_fists;
	}
	
	//BOUNTY
    public static int getBounty_listSize() {
        return bounty_listSize;
    }

    public static double getBounty_minimum() {
        return bounty_minimum;
    }

    public static boolean isBounty_godfatherMode() {
        return bounty_godfatherMode;
    }

    public static boolean isBounty_notify() {
        return bounty_notify;
    }

    public static boolean isBounty_broadcast() {
        return bounty_broadcast;
    }
	
	public static boolean isBounty_console() {
		return bounty_console;
	}
	
	//SIGN_SELLING
    public static String getSign_selling_1() {
        return sign_selling_1;
    }

    public static String getSign_selling_2() {
        return sign_selling_2;
    }

    public static String getSign_selling_3() {
        return sign_selling_3;
    }

    public static String getSign_selling_4() {
        return sign_selling_4;
    }

    //SIGN_WANTED
    public static String getSign_wanted_1() {
        return sign_wanted_1;
    }

    public static String getSign_wanted_2() {
        return sign_wanted_2;
    }

    public static String getSign_wanted_3() {
        return sign_wanted_3;
    }

    public static String getSign_wanted_4() {
        return sign_wanted_4;
    }

    //ENCHANT_LOOTING
    public static boolean isEnchant_looting_enabled() {
        return enchant_looting_enabled;
    }

    public static String getEnchant_looting_map() {
        return enchant_looting_map;
    }

    //ENCHANT_CUSTOM
    public static boolean isEnchant_custom_enabled() {
        return enchant_custom_enabled;
    }

    public static int getEnchant_custom_chance() {
        return enchant_custom_chance;
    }

    public static int getEnchant_custom_minLevels() {
        return enchant_custom_minLevels;
    }

    public static int getEnchant_custom_maxLevels() {
        return enchant_custom_maxLevels;
    }

    public static int getEnchant_custom_maxOthers() {
        return enchant_custom_maxOthers;
    }

    public static boolean isEnchant_custom_allowedBows() {
        return enchant_custom_allowedBows;
    }

    public static boolean isEnchant_custom_allowedTools() {
        return enchant_custom_allowedTools;
    }

    public static double getEnchant_custom_dropRate() {
        return enchant_custom_dropRate;
    }

    //SUPPORT_FACTIONS
    public static boolean isSupport_factions_dropWilderness() {
        return support_factions_dropWilderness;
    }

    public static boolean isSupport_factions_dropWarzone() {
        return support_factions_dropWarzone;
    }

    public static boolean isSupport_factions_dropSafezone() {
        return support_factions_dropSafezone;
    }

    //SUPPORT_MINIGAMES
    public static boolean isSupport_minigames_dropGame() {
        return support_minigames_dropGame;
    }

    //SUPPORT_MOBSTACKER
    public static boolean isSupport_mobstacker_dropEach() {
        return support_mobstacker_dropEach;
    }
}
