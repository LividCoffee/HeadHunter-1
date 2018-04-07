package com.neo.headhunter.listener;

import com.neo.headhunter.util.MetaUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class ListenerCombustion implements Listener {
	private Map<LivingEntity, Player> ignitions;
	private Map<LivingEntity, ItemStack> weapons;
	
	public ListenerCombustion() {
		this.ignitions = new HashMap<>();
		this.weapons = new HashMap<>();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
		if(event.isCancelled())
			return;
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) event.getEntity();
			if(event.getCombuster() instanceof Player) {
				Player source = (Player) event.getCombuster();
				ignite(source, target, source.getItemInHand());
			}
			else if(event.getCombuster() instanceof Arrow) {
				Arrow source = (Arrow) event.getCombuster();
				ignite(MetaUtils.getShooter(source), target, MetaUtils.getBow(source));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.isCancelled())
			return;
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) event.getEntity();
			if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
				if(target.getFireTicks() < 20)
					extinguish(target);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		extinguish(event.getEntity());
	}
	
	public Player getIgnitionSource(LivingEntity target) {
		return ignitions.get(target);
	}
	
	public ItemStack getIgnitionWeapon(LivingEntity target) {
		return weapons.get(target);
	}
	
	private void ignite(Player hunter, LivingEntity target, ItemStack weapon) {
		ignitions.put(target, hunter);
		weapons.put(target, weapon);
	}
	
	private void extinguish(LivingEntity target) {
		ignitions.remove(target);
		weapons.remove(target);
	}
}
