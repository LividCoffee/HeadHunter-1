package com.neo.headhunter.util.item.head;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class HeadData {
	private String owner;
	private short durability;
	private String displayName;
	private String lore;
	
	public HeadData(ItemStack head) {
		if(head != null && head.getType() == Material.SKULL_ITEM) {
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			this.owner = meta.getOwner();
			this.durability = head.getDurability();
			this.displayName = meta.getDisplayName();
			StringBuilder lore = new StringBuilder();
			boolean newLine = false;
			for (String line : meta.getLore()) {
				if (newLine)
					lore.append("\n");
				lore.append(line);
				newLine = true;
			}
			this.lore = lore.toString();
		}
	}
	
	public String getOwner() {
		return owner;
	}
	
	public short getDurability() {
		return durability;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getLore() {
		return lore;
	}
}
