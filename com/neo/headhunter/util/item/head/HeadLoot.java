package com.neo.headhunter.util.item.head;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public final class HeadLoot {
	private final ItemStack head;
	private final LivingEntity whereToDrop;
	private final boolean insert;
	
	public HeadLoot(ItemStack head, LivingEntity whereToDrop, boolean insert) {
		this.head = head;
		this.whereToDrop = whereToDrop;
		this.insert = insert;
	}
	
	public ItemStack getHead() {
		return head;
	}
	
	public LivingEntity getWhereToDrop() {
		return whereToDrop;
	}
	
	public boolean willInsert() {
		return insert;
	}
}
