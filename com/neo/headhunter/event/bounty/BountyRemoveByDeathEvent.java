package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BountyRemoveByDeathEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	
	private Player hunter;
	private OfflinePlayer target;
	private final double total;
	
	public BountyRemoveByDeathEvent(Player hunter, OfflinePlayer target, double total) {
		this.hunter = hunter;
		this.target = target;
		this.total = total;
	}
	
	public Player getHunter() {
		return hunter;
	}
	
	public void setHunter(Player hunter) {
		this.hunter = hunter;
	}
	
	public OfflinePlayer getTarget() {
		return target;
	}
	
	public void setTarget(OfflinePlayer target) {
		this.target = target;
	}
	
	public double getTotal() {
		return total;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
