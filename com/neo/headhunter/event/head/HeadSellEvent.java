package com.neo.headhunter.event.head;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class HeadSellEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private boolean inventory;
	private boolean fromSign;
	
	private Map<Integer, ItemStack> sellData;
	private int heldItemSlot;
	
	public HeadSellEvent(Player hunter, boolean inventory, boolean fromSign) {
		super(hunter);
		
		this.cancelled = false;
		
		this.inventory = inventory;
		this.fromSign = fromSign;
		
		this.sellData = remakeSellData();
		this.heldItemSlot = hunter.getInventory().getHeldItemSlot();
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
	
	public Map<Integer, ItemStack> remakeSellData() {
		sellData = new HashMap<>();
		PlayerInventory inv = super.player.getInventory();
		for(int slot = 0; slot < 36; slot++) {
			ItemStack item = inv.getItem(slot);
			if(item != null && item.getType() == Material.SKULL_ITEM)
				sellData.put(slot, item);
		}
		return sellData;
	}
	
	public Map<Integer, ItemStack> getSellData() {
		return sellData;
	}
	
	public ItemStack getSlot(int slot) {
		return sellData.get(slot);
	}
	
	public ItemStack removeSlot(int slot) {
		return sellData.remove(slot);
	}
	
	public int getHeldItemSlot() {
		return heldItemSlot;
	}
	
	public void setHeldItemSlot(int heldItemSlot) {
		this.heldItemSlot = heldItemSlot;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}
