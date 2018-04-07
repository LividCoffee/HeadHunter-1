package com.neo.headhunter.mgmt;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class CooldownManager extends BukkitRunnable {
	private Map<OfflinePlayer, Long> cooldowns;
	
	public CooldownManager() {
		this.cooldowns = new HashMap<>();
	}
	
	public void setCooldown(OfflinePlayer target, long seconds) {
		if(seconds > 0)
			cooldowns.put(target, seconds);
	}
	
	public void resetCooldown(OfflinePlayer target) {
		cooldowns.remove(target);
	}
	
	public long getCooldown(OfflinePlayer target) {
		return cooldowns.getOrDefault(target, 0L);
	}
	
	public boolean isOnCooldown(OfflinePlayer target) {
		return cooldowns.containsKey(target);
	}
	
	@Override
	public void run() {
		Iterator<Map.Entry<OfflinePlayer, Long>> iter = cooldowns.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<OfflinePlayer, Long> entry = iter.next();
			entry.setValue(entry.getValue() - 1);
			if(entry.getValue() <= 0)
				iter.remove();
		}
	}
}
