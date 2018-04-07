package com.neo.headhunter.listener.support;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.MetaUtils;
import com.neo.headhunter.util.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public final class ListenerMobStacker implements Listener {
	private HeadHunter plugin;
	
	public ListenerMobStacker(HeadHunter plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(Utils.isPluginEnabled("MobStacker") || Utils.isPluginEnabled("StackMob")) {
			LivingEntity target = event.getEntity();
			if(target instanceof Player)
				(new UnmarkRunnable(plugin, target)).run();
			else
				(new MarkRunnable(plugin, target)).runTaskLater(plugin, 3L);
		}
	}
	
	private static class MarkRunnable extends BukkitRunnable {
		private static final double STACK_RANGE = 0.001;
		
		private final HeadHunter plugin;
		private final LivingEntity oldMob;
		
		public MarkRunnable(HeadHunter plugin, LivingEntity oldMob) {
			this.plugin = plugin;
			this.oldMob = oldMob;
		}
		
		@Override
		public void run() {
			for(Entity e : oldMob.getNearbyEntities(STACK_RANGE, STACK_RANGE, STACK_RANGE)) {
				if(e.getType() == oldMob.getType()) {
					MetadataValue mv = new FixedMetadataValue(plugin, null);
					e.setMetadata(MetaUtils.HUNTERMARKED, mv);
					break;
				}
			}
		}
	}
	
	private static class UnmarkRunnable extends BukkitRunnable {
		private final HeadHunter plugin;
		private final LivingEntity mob;
		
		public UnmarkRunnable(HeadHunter plugin, LivingEntity mob) {
			this.plugin = plugin;
			this.mob = mob;
		}
		
		@Override
		public void run() {
			mob.removeMetadata(MetaUtils.HUNTERMARKED, plugin);
		}
	}
}
