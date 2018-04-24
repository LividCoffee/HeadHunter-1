package com.neo.headhunter.event.head;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class HeadSellEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private boolean inventory;
	private boolean fromSign;
	
	public HeadSellEvent(Player hunter, boolean inventory, boolean fromSign) {
		super(hunter);
		
		this.cancelled = false;
		
		this.inventory = inventory;
		this.fromSign = fromSign;
	}
	
	public boolean isInventory() {
		return inventory;
	}
	
	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}
	
	public boolean isFromSign() {
		return fromSign;
	}
	
	public void setFromSign(boolean fromSign) {
		this.fromSign = fromSign;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
