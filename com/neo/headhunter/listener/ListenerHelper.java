package com.neo.headhunter.listener;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.MetaUtils;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;

public final class ListenerHelper implements Listener {
	private HeadHunter plugin;
	
	public ListenerHelper(HeadHunter plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
		LivingEntity entity = event.getEntity();
		MetadataValue fmv = new FixedMetadataValue(plugin, reason);
		entity.setMetadata(MetaUtils.SPAWNREASON, fmv);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityShootBow(EntityShootBowEvent event) {
		if(event.isCancelled())
			return;
		LivingEntity shooter = event.getEntity();
		if(shooter instanceof Player) {
			Player owner = (Player) shooter;
			Entity projectile = event.getProjectile();
			if(projectile instanceof Arrow) {
				Arrow arrow = (Arrow) projectile;
				ItemStack bow = event.getBow();
				MetadataValue valueBow = new FixedMetadataValue(plugin, bow);
				MetadataValue valueOwner = new FixedMetadataValue(plugin, owner);
				arrow.setMetadata(MetaUtils.BOW, valueBow);
				arrow.setMetadata(MetaUtils.SHOOTER, valueOwner);
				event.setProjectile(arrow);
			}
		}
	}
	
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event) {
		if(!Settings.isEnchant_custom_enabled())
			return;
		ItemStack item = event.getItem();
		if(!HeadUtils.isAllowedDecapitation(item))
			return;
		if(!(Utils.RANDOM().nextInt(100) < Settings.getEnchant_custom_chance()))
			return;
		int levelCost = event.getExpLevelCost();
		if( levelCost >= Settings.getEnchant_custom_minLevels() &&
				    levelCost <= Settings.getEnchant_custom_maxLevels()) {
			if(event.getEnchantsToAdd().size() <= Settings.getEnchant_custom_maxOthers()) {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<>();
				if(meta.hasLore())
					lore = meta.getLore();
				lore.add("§7Decapitation");
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		plugin.getCooldownManager().setCooldown(event.getPlayer(), PlayerUtils.getCooldown(event.getPlayer()));
	}
}
