package com.neo.headhunter.event;

import com.neo.headhunter.util.item.head.HeadLoot;
import com.neo.headhunter.util.item.head.HeadLootData;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HeadDropEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private OfflinePlayer hunter;
	private LivingEntity target;
	private double dropChance;
	private HeadLootData lootData;
	private HeadLoot loot;
	
	public HeadDropEvent(OfflinePlayer hunter, LivingEntity target, double dropChance, HeadLootData lootData, HeadLoot loot) {
		this.cancelled = false;
		
		this.hunter = hunter;
		this.target = target;
		this.dropChance = dropChance;
		this.lootData = lootData;
		this.loot = loot;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
	
	public OfflinePlayer getHunter() {
		return hunter;
	}
	
	public void setHunter(OfflinePlayer hunter) {
		this.hunter = hunter;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public void setTarget(LivingEntity target) {
		this.target = target;
	}
	
	public double getDropChance() {
		return dropChance;
	}
	
	public void setDropChance(double dropChance) {
		this.dropChance = dropChance;
	}
	
	public HeadLootData getLootData() {
		return lootData;
	}
	
	public void setLootData(HeadLootData lootData) {
		this.lootData = lootData;
	}
	
	public HeadLoot getLoot() {
		return loot;
	}
	
	public void setLoot(HeadLoot loot) {
		this.loot = loot;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
