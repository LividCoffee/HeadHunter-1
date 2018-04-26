package com.neo.headhunter.util.mob;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.AuxResource;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

/**
 * Reloadable: Yes<p>
 * Requires: None</p>
 */
public final class MobLibrary {
    private HeadHunter plugin;
    private final HashMap<String, String> PROPER_NAMES = new HashMap<>();
    private final HashMap<String, ItemStack> BASE_HEADS = new HashMap<>();

    public MobLibrary(HeadHunter plugin) {
        this.plugin = plugin;
        loadIrregularEntityTags();
        loadBaseHeads();
        loadDefaults();
    }

    public String getEntityName(String entityTag) {
    	if(PROPER_NAMES.containsKey(entityTag))
    		return PROPER_NAMES.get(entityTag);
        return entityTag;
    }

    public double getEntityValue(String entityTag) {
    	FileConfiguration config = plugin.getAuxiliary(Utils.MOB).getConfig();
    	if(config.contains(entityTag))
    		return config.getDouble(entityTag + ".value");
    	return 0;
    }

    public int getEntityDropRate(String entityTag) {
    	FileConfiguration config = plugin.getAuxiliary(Utils.MOB).getConfig();
    	if(config.contains(entityTag))
    		return config.getInt(entityTag + ".drop-rate");
    	return 0;
    }
    
    public String getEntityTag(ItemStack head) {
	    if(head == null || head.getType() != Material.SKULL_ITEM)
		    throw new IllegalArgumentException("head must be of Material SKULL_ITEM");
	    SkullMeta meta = (SkullMeta) head.getItemMeta();
	    if(meta.hasOwner())
	    	return meta.getOwner();
	    return null;
    }

    public ItemStack getBaseHead(String entityTag) {
        return BASE_HEADS.get(entityTag);
    }
    
    public boolean isMobHead(ItemStack head) {
	    if(head == null || head.getType() != Material.SKULL_ITEM)
		    throw new IllegalArgumentException("head must be of Material SKULL_ITEM");
	    SkullMeta meta = (SkullMeta) head.getItemMeta();
	    if(meta.hasOwner())
	    	return plugin.getAuxiliary(Utils.MDB).getConfig().getKeys(false).contains(meta.getOwner());
    	return false;
    }

    private void loadDefaults() {
	    AuxResource aux = plugin.getAuxiliary(Utils.MOB);
        FileConfiguration config = aux.getConfig();
        for(String tag : plugin.getAuxiliary(Utils.MDB).getConfig().getKeys(false)) {
            if(!config.contains(tag + ".value"))
	            config.set(tag + ".value", 20.0);
            if(!config.contains(tag + ".drop-rate"))
	            config.set(tag + ".drop-rate", 100);
        }
	    aux.saveConfig();
    }
	
	private void loadBaseHeads() {
		AuxResource aux = plugin.getAuxiliary(Utils.MDB);
		FileConfiguration config = aux.getConfig();
		for(String tag : config.getKeys(false))
			BASE_HEADS.put(tag, config.getItemStack(tag));
	}
	
	//corrects for entity tag inconsistencies
	public String getEntityTag(LivingEntity entity) {
		String className = entity.getClass().getName();
		String[] pieces = className.split("\\.");
		if(pieces.length < 1)
			return null;
		className = pieces[pieces.length-1];
		if(className.startsWith("Craft"))
			className = className.replaceFirst("Craft", "");
		switch(className) {
		case "MushroomCow":
			return "Mooshroom";
		case "Parrot":
			switch(((Parrot) entity).getVariant()) {
			case BLUE:
				return "ParrotBlue";
			case CYAN:
				return "ParrotCyan";
			case GRAY:
				return "ParrotGray";
			case GREEN:
				return "ParrotGreen";
			default:
				return "ParrotRed";
			}
		case "PigZombie":
			return "ZombiePigman";
		case "Skeleton":
			switch(((Skeleton) entity).getSkeletonType()) {
			case WITHER:
				return "WitherSkeleton";
			default:
				return "Skeleton";
			}
		case "Snowman":
			return "SnowGolem";
		case "Wither":
			return "WitherBoss";
		case "VillagerZombie":
			return "Zombie";
		default:
			return className;
		}
	}
 
	//list of entity tags that are not the same as their proper names
    private void loadIrregularEntityTags() {
        PROPER_NAMES.put("CaveSpider", "Cave Spider");
        PROPER_NAMES.put("ElderGuardian", "Elder Guardian");
        PROPER_NAMES.put("IronGolem", "Iron Golem");
        PROPER_NAMES.put("MagmaCube", "Magma Cube");
	    PROPER_NAMES.put("ParrotBlue", "Parrot");
	    PROPER_NAMES.put("ParrotCyan", "Parrot");
	    PROPER_NAMES.put("ParrotGray", "Parrot");
	    PROPER_NAMES.put("ParrotGreen", "Parrot");
	    PROPER_NAMES.put("ParrotRed", "Parrot");
        PROPER_NAMES.put("PolarBear", "Polar Bear");
	    PROPER_NAMES.put("SkeletonHorse", "Skeleton Horse");
        PROPER_NAMES.put("SnowGolem", "Snow Golem");
        PROPER_NAMES.put("WitherBoss", "Wither");
        PROPER_NAMES.put("WitherSkeleton", "Wither Skeleton");
	    PROPER_NAMES.put("ZombieHorse", "Zombie Horse");
        PROPER_NAMES.put("ZombiePigman", "Zombie Pigman");
    }
}
