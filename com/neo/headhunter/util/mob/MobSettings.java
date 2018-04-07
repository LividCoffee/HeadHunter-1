package com.neo.headhunter.util.mob;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Accessor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public final class MobSettings {
    private static boolean
            hoardMode,
            drop_anyCause,
            enchant_looting_enabled,
            enchant_custom_enabled,
            support_factions_dropWilderness, support_factions_dropWarzone, support_factions_dropSafezone,
            support_minigames_dropGame;

    private static double enchant_custom_dropRate;

    private static String
            drop_preventSpawnReasons, drop_preventDeathReasons,
            head_format_title, head_format_worth, head_format_worthless,
            enchant_looting_map;
    
    private static List<CreatureSpawnEvent.SpawnReason> preventSpawnReasons;
    private static List<EntityDamageEvent.DamageCause> preventDeathReasons;

    public static void load(HeadHunter plugin) {
        Accessor ax = plugin.access(Utils.CFG);
        FileConfiguration access = ax.getConfig();
        hoardMode = access.getBoolean("mobhunter.hoard-mode", false);
        
        drop_anyCause = access.getBoolean("mobhunter.drop.any-cause", false);
        drop_preventSpawnReasons = access.getString("mobhunter.drop.prevent-spawn-reasons", "");
        loadPreventSpawnReasons();
        drop_preventDeathReasons = access.getString("mobhunter.drop.prevent-death-reasons", "");
        loadPreventDeathReasons();

        head_format_title = access.getString("mobhunter.head.format.title", "&a{TARGET} Head");
        head_format_worth = access.getString("mobhunter.head.format.worth", "&eSell Price:&a ${VALUE}");
        head_format_worthless = access.getString("mobhunter.head.format.worthless", "&eSell Price: &cWorthless");

        enchant_looting_enabled = access.getBoolean("mobhunter.enchant.looting.enabled", false);
        enchant_looting_map = access.getString("mobhunter.enchant.looting.level-multiplier-map", "1:1.1/2:1.3/3:1.5");
        enchant_custom_enabled = access.getBoolean("mobhunter.enchant.custom.enabled", false);
        enchant_custom_dropRate = access.getDouble("mobhunter.enchant.custom.drop-chance", 100);

        support_factions_dropWilderness = access.getBoolean("mobhunter.support.factions.drop-wilderness", true);
        support_factions_dropWarzone = access.getBoolean("mobhunter.support.factions.drop-warzone", false);
        support_factions_dropSafezone = access.getBoolean("mobhunter.support.factions.drop-safezone", false);
        support_minigames_dropGame = access.getBoolean("mobhunter.support.minigames.drop-game", false);
    }

    public static boolean isHoardMode() {
        return hoardMode;
    }
	
	public static boolean isDrop_anyCause() {
		return drop_anyCause;
	}
	
	public static List<CreatureSpawnEvent.SpawnReason> getDrop_preventSpawnReasons() {
		return preventSpawnReasons;
	}

    private static void loadPreventSpawnReasons() {
        String rawList = drop_preventSpawnReasons.replaceAll("\\s", "").toUpperCase();
        List<CreatureSpawnEvent.SpawnReason> result = new ArrayList<>();
        for(String reasonString : rawList.split(",")) {
            try {
                CreatureSpawnEvent.SpawnReason reason = CreatureSpawnEvent.SpawnReason.valueOf(reasonString);
                result.add(reason);
            } catch (IllegalArgumentException | NullPointerException exc) {}
        }
        preventSpawnReasons = result;
    }
    
    public static List<EntityDamageEvent.DamageCause> getDrop_preventDeathReasons() {
    	return preventDeathReasons;
    }

    private static void loadPreventDeathReasons() {
	    String rawList = drop_preventDeathReasons.replaceAll("\\s", "").toUpperCase();
        List<EntityDamageEvent.DamageCause> result = new ArrayList<>();
        for(String reasonString : rawList.split(",")) {
            try {
                EntityDamageEvent.DamageCause reason = EntityDamageEvent.DamageCause.valueOf(reasonString);
                result.add(reason);
            } catch (IllegalArgumentException | NullPointerException exc) {}
        }
        preventDeathReasons = result;
    }

    public static String getHead_format_title() {
        return head_format_title;
    }

    public static String getHead_format_worth() {
        return head_format_worth;
    }

    public static String getHead_format_worthless() {
        return head_format_worthless;
    }

    public static boolean isEnchant_looting_enabled() {
        return enchant_looting_enabled;
    }

    public static String getEnchant_looting_map() {
        return enchant_looting_map;
    }

    public static boolean isEnchant_custom_enabled() {
        return enchant_custom_enabled;
    }

    public static double getEnchant_custom_dropRate() {
        return enchant_custom_dropRate;
    }

    public static boolean isSupport_factions_dropWilderness() {
        return support_factions_dropWilderness;
    }

    public static boolean isSupport_factions_dropWarzone() {
        return support_factions_dropWarzone;
    }

    public static boolean isSupport_factions_dropSafezone() {
        return support_factions_dropSafezone;
    }

    public static boolean isSupport_minigames_dropGame() {
        return support_minigames_dropGame;
    }
}
