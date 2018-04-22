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
    private final HashMap<String, String> ENTITY_TAG_MAP = new HashMap<>();
    private final HashMap<String, ItemStack> BASE_HEAD_MAP = new HashMap<>();

    public MobLibrary(HeadHunter plugin) {
        this.plugin = plugin;
        loadEntityTags();
        loadBaseHeads();
        loadDefaults();
    }

    public String getEntityName(String entityTag) {
        if(entityTag == null) return null;
        return ENTITY_TAG_MAP.get(entityTag);
    }

    public String getEntityName(LivingEntity entity) {
        return ENTITY_TAG_MAP.get(getEntityTag(entity));
    }

    public double getEntityValue(String entityTag) {
        return entityTag == null ? 0 : plugin.getAuxiliary(Utils.MOB).getConfig().getDouble(entityTag + ".value");
    }

    public double getEntityValue(LivingEntity entity) {
        return getEntityValue(getEntityTag(entity));
    }

    public int getEntityDropRate(String entityTag) {
        return entityTag == null ? 0 : plugin.getAuxiliary(Utils.MOB).getConfig().getInt(entityTag + ".drop-rate");
    }

    public int getEntityDropRate(LivingEntity entity) {
        return getEntityDropRate(getEntityTag(entity));
    }

    public String getEntityTag(LivingEntity entity) {
        String className = entity.getClass().getName();
        String[] pieces = className.split("\\Q.\\E");
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
            case "ZombieVillager":
                return "Zombie";
            case "Bat":
            case "Blaze":
            case "CaveSpider":
            case "Chicken":
            case "Cow":
            case "Creeper":
            case "Donkey":
            case "ElderGuardian":
            case "Enderman":
            case "Endermite":
            case "Evoker":
            case "Ghast":
            case "Guardian":
            case "Horse":
            case "Husk":
            case "IronGolem":
            case "Llama":
            case "MagmaCube":
            case "Mule":
            case "Ocelot":
            case "Pig":
            case "PolarBear":
            case "Rabbit":
            case "Sheep":
            case "Shulker":
            case "Silverfish":
            case "Slime":
            case "Spider":
            case "Squid":
            case "Stray":
            case "Villager":
            case "Vindicator":
            case "Witch":
            case "WitherSkeleton":
            case "Wolf":
            case "Zombie":
                return className;
            default:
                return null;
        }
    }

    public ItemStack getBaseHead(String entityTag) {
        return BASE_HEAD_MAP.get(entityTag);
    }

    public ItemStack getBaseHead(LivingEntity entity) {
        return getBaseHead(getEntityTag(entity));
    }
    
    public String getTagFromHead(ItemStack head) {
    	if(head == null || head.getType() != Material.SKULL_ITEM)
    		throw new IllegalArgumentException("head must be of Material SKULL_ITEM");
    	SkullMeta meta = (SkullMeta) head.getItemMeta();
    	switch(head.getDurability()) {
	    case 0:
	    	return "Skeleton";
	    case 1:
	    	return "WitherSkeleton";
	    case 2:
	    	return "Zombie";
	    case 4:
	    	return "Creeper";
	    default:
	    	String owner = meta.getOwner();
	    	switch(owner) {
		    case "Bat":
		    case "Horse":
		    case "Husk":
		    case "Llama":
		    case "ParrotBlue":
		    case "ParrotCyan":
		    case "ParrotGreen":
		    case "ParrotGray":
		    case "ParrotRed":
		    case "Silverfish":
		    	return owner;
		    case "MHF_LavaSlime":
		    	return "MagmaCube";
		    case "MHF_MushroomCow":
		    	return "Mooshroom";
		    case "Polar Bear":
		    	return "PolarBear";
		    case "MHF_Wither":
		    	return "WitherBoss";
		    case "MHF_PigZombie":
		    	return "ZombiePigman";
		    default:
			    if(owner.startsWith("MHF_"))
				    return owner.replace("MHF_", "");
		    	return null;
		    }
	    }
    }

    public boolean isValidEntityTag(String tag) {
        return ENTITY_TAG_MAP.containsKey(tag) && BASE_HEAD_MAP.containsKey(tag);
    }

    public void loadDefaults() {
	    AuxResource aux = plugin.getAuxiliary(Utils.MOB);
        FileConfiguration config = aux.getConfig();
        for(String key : ENTITY_TAG_MAP.keySet()) {
            if(!config.contains(key + ".value"))
	            config.set(key + ".value", 20.0);
            if(!config.contains(key + ".drop-rate"))
	            config.set(key + ".drop-rate", 100);
        }
	    aux.saveConfig();
    }

    //
    private void loadEntityTags() {
        ENTITY_TAG_MAP.put("Bat", "Bat");
        ENTITY_TAG_MAP.put("Blaze", "Blaze");
        ENTITY_TAG_MAP.put("CaveSpider", "Cave Spider");
        ENTITY_TAG_MAP.put("Chicken", "Chicken");
        ENTITY_TAG_MAP.put("Cow", "Cow");
        ENTITY_TAG_MAP.put("Creeper", "Creeper");
        ENTITY_TAG_MAP.put("Donkey", "Donkey");
        ENTITY_TAG_MAP.put("ElderGuardian", "Elder Guardian");
        ENTITY_TAG_MAP.put("Enderman", "Enderman");
        ENTITY_TAG_MAP.put("Endermite", "Endermite");
        ENTITY_TAG_MAP.put("Evoker", "Evoker");
        ENTITY_TAG_MAP.put("Ghast", "Ghast");
        ENTITY_TAG_MAP.put("Guardian", "Guardian");
        ENTITY_TAG_MAP.put("Horse", "Horse");
        ENTITY_TAG_MAP.put("Husk", "Husk");
        ENTITY_TAG_MAP.put("IronGolem", "Iron Golem");
        ENTITY_TAG_MAP.put("Llama", "Llama");
        ENTITY_TAG_MAP.put("MagmaCube", "Magma Cube");
        ENTITY_TAG_MAP.put("Mooshroom", "Mooshroom");
        ENTITY_TAG_MAP.put("Mule", "Mule");
        ENTITY_TAG_MAP.put("Ocelot", "Ocelot");
	    ENTITY_TAG_MAP.put("ParrotBlue", "Parrot");
	    ENTITY_TAG_MAP.put("ParrotCyan", "Parrot");
	    ENTITY_TAG_MAP.put("ParrotGray", "Parrot");
	    ENTITY_TAG_MAP.put("ParrotGreen", "Parrot");
	    ENTITY_TAG_MAP.put("ParrotRed", "Parrot");
        ENTITY_TAG_MAP.put("Pig", "Pig");
        ENTITY_TAG_MAP.put("PolarBear", "Polar Bear");
        ENTITY_TAG_MAP.put("Rabbit", "Rabbit");
        ENTITY_TAG_MAP.put("Sheep", "Sheep");
        ENTITY_TAG_MAP.put("Shulker", "Shulker");
        ENTITY_TAG_MAP.put("Silverfish", "Silverfish");
        ENTITY_TAG_MAP.put("Skeleton", "Skeleton");
        ENTITY_TAG_MAP.put("Slime", "Slime");
        ENTITY_TAG_MAP.put("SnowGolem", "Snow Golem");
        ENTITY_TAG_MAP.put("Spider", "Spider");
        ENTITY_TAG_MAP.put("Squid", "Squid");
        ENTITY_TAG_MAP.put("Stray", "Stray");
        ENTITY_TAG_MAP.put("Villager", "Villager");
        ENTITY_TAG_MAP.put("Vindicator", "Vindicator");
        ENTITY_TAG_MAP.put("Witch", "Witch");
        ENTITY_TAG_MAP.put("WitherBoss", "Wither");
        ENTITY_TAG_MAP.put("WitherSkeleton", "Wither Skeleton");
        ENTITY_TAG_MAP.put("Wolf", "Wolf");
        ENTITY_TAG_MAP.put("Zombie", "Zombie");
        ENTITY_TAG_MAP.put("ZombiePigman", "Zombie Pigman");
    }

    private void loadBaseHeads() {
        AuxResource aux = plugin.getAuxiliary(Utils.MDB);
        FileConfiguration config = aux.getConfig();
        for(String tag : config.getKeys(false))
            BASE_HEAD_MAP.put(tag, config.getItemStack(tag));
    }

    public boolean isMobHead(ItemStack head) {
        if(head != null && head.getType() == Material.SKULL_ITEM) {
	        SkullMeta meta = (SkullMeta) head.getItemMeta();
	        for (ItemStack base : BASE_HEAD_MAP.values()) {
		        SkullMeta beta = (SkullMeta) base.getItemMeta();
		        if (!meta.hasOwner() || meta.getOwner().equals(beta.getOwner()))
			        return true;
	        }
        }
        return false;
    }
}
