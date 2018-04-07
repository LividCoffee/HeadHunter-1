package com.neo.headhunter.util;

import com.neo.headhunter.HeadHunter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

public final class MetaUtils {
	public static final String SPAWNREASON = "spawn_reason";
	public static final String HUNTERMARKED = "hunter_marked";
	public static final String BOW = "weapon_bow";
	public static final String SHOOTER = "weapon_shooter";
	
	public static CreatureSpawnEvent.SpawnReason getSpawnReason(LivingEntity entity) {
		for(MetadataValue mv : entity.getMetadata(SPAWNREASON)) {
			if(mv.getOwningPlugin() instanceof HeadHunter) {
				if(mv.value() instanceof CreatureSpawnEvent.SpawnReason)
					return (CreatureSpawnEvent.SpawnReason) mv.value();
			}
		}
		return null;
	}
	
	public static boolean isHunterMarked(LivingEntity entity) {
		for(MetadataValue mv : entity.getMetadata(HUNTERMARKED)) {
			if(mv.getOwningPlugin() instanceof HeadHunter)
				return true;
		}
		return false;
	}
	
	public static ItemStack getBow(Arrow arrow) {
		for(MetadataValue mv : arrow.getMetadata(BOW)) {
			if(mv.getOwningPlugin() instanceof HeadHunter) {
				if(mv.value() instanceof ItemStack)
					return (ItemStack) mv.value();
			}
		}
		return null;
	}
	
	public static Player getShooter(Arrow arrow) {
		for(MetadataValue mv : arrow.getMetadata(SHOOTER)) {
			if(mv.getOwningPlugin() instanceof HeadHunter) {
				if(mv.value() instanceof Player)
					return (Player) mv.value();
			}
		}
		return null;
	}
}
