package com.neo.headhunter.listener;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.factory.DropFactory;
import com.neo.headhunter.factory.HeadFactory;
import com.neo.headhunter.util.MetaUtils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.item.head.HeadLoot;
import com.neo.headhunter.util.item.head.HeadLootData;
import com.neo.headhunter.util.mob.MobSettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Requires: Economy, HeadFactory</p>
 */
public final class ListenerDeath implements Listener {
	private Economy economy;
	
	private ListenerCombustion listenerCombustion;
	
	private DropFactory dropFactory;
	private HeadFactory headFactory;
	
	public ListenerDeath(HeadHunter plugin) {
		this.economy = plugin.getEconomy();
		
		this.listenerCombustion = plugin.getListenerCombustion();
		
		this.dropFactory = plugin.getDropFactory();
		this.headFactory = plugin.getHeadFactory();
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity target = event.getEntity();
		EntityDamageEvent damageEvent = target.getLastDamageCause();
		EntityDamageEvent.DamageCause deathCause;
		if(damageEvent != null)
			deathCause = damageEvent.getCause();
		else
			return;
		
		Player hunter = null;
		ItemStack weapon = null;
		switch(deathCause) {
		case ENTITY_ATTACK:
		case PROJECTILE:
			EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) damageEvent;
			Entity damager = damageByEntityEvent.getDamager();
			if(damager instanceof Player) {
				hunter = (Player) damager;
				weapon = hunter.getItemInHand();
			}
			else if(damager instanceof Arrow) {
				hunter = MetaUtils.getShooter((Arrow) damager);
				weapon = MetaUtils.getBow((Arrow) damager);
			}
			break;
		case FIRE_TICK:
			if(Settings.isDrop_fireTick()) {
				hunter = listenerCombustion.getIgnitionSource(target);
				weapon = listenerCombustion.getIgnitionWeapon(target);
			}
			else
				return;
			break;
		default:
			return;
		}
		
		if(hunter == null) {
			if(target instanceof Player) {
				if (!Settings.isDrop_anyCause())
					return;
			}
			else {
				if(!MobSettings.isDrop_anyCause())
					return;
			}
		}
		
		World targetWorld = target.getWorld();
		if(dropFactory.resolveDrop(hunter, target, weapon)) {
			HeadLootData lootData = headFactory.createHeadLootData(hunter, target);
			if(Settings.isDrop_onlyWithBounty() && lootData.getBountyValue() == 0)
				return;
			if(!lootData.isMobHead())
				economy.withdrawPlayer((OfflinePlayer) target, lootData.getWithdraw());
			HeadLoot loot = headFactory.createHead(hunter, target, lootData);
			LivingEntity whereToDrop = loot.getWhereToDrop();
			if(loot.willInsert() && whereToDrop instanceof InventoryHolder) {
				Inventory inv = ((InventoryHolder) whereToDrop).getInventory();
				Map<Integer, ItemStack> excess = inv.addItem(loot.getHead());
				for(ItemStack item : excess.values())
					whereToDrop.getWorld().dropItemNaturally(whereToDrop.getLocation(), item);
			}
			else
				targetWorld.dropItemNaturally(target.getLocation(), loot.getHead());
		}
	}
}
