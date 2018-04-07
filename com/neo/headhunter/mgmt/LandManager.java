package com.neo.headhunter.mgmt;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.*;

public final class LandManager {
	private HeadHunter plugin;
	private Set<String> validWorlds;
	
	public LandManager(HeadHunter plugin) {
		this.plugin = plugin;
		this.validWorlds = new HashSet<>();
		loadWorlds();
	}
	
	public boolean isValidDropLocation(LivingEntity target) {
		if(Settings.isIgnoreWorlds())
			return true;
		for(String w : validWorlds)
			if(w.equals(target.getWorld().getName()))
				return true;
		return false;
	}
	
	public boolean addWorld(World w) {
		boolean result = false;
		if(w != null)
			result = validWorlds.add(w.getName());
		saveWorlds();
		return result;
	}
	
	public boolean removeWorld(World w) {
		boolean result = false;
		if(w != null) {
			Iterator<String> iter = validWorlds.iterator();
			while(iter.hasNext())
				if(w.getName().equals(iter.next())) {
					iter.remove();
					result = true;
				}
		}
		saveWorlds();
		return result;
	}
	
	private void saveWorlds() {
		List<String> cfgList = new ArrayList<>();
		for(String s : validWorlds) {
			if (Bukkit.getWorld(s) != null)
				cfgList.add(s);
		}
		plugin.getConfig().set("worlds", cfgList);
		plugin.saveConfig();
	}
	
	private void loadWorlds() {
		validWorlds.clear();
		List<String> cfgList = plugin.getConfig().getStringList("worlds");
		if(cfgList != null && !cfgList.isEmpty()) {
			for (String s : cfgList) {
				World w = Bukkit.getWorld(s);
				if (w != null)
					validWorlds.add(s);
			}
		}
	}
}
