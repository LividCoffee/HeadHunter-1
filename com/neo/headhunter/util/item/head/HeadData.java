package com.neo.headhunter.util.item.head;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class HeadData {
	private String owner;
	private String displayName;
	private String lore;
	
	public HeadData(ItemStack head) {
		this.owner = null;
		this.displayName = null;
		this.lore = null;
		if(head != null && head.getType() == Material.SKULL_ITEM) {
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			if(meta.hasOwner())
				this.owner = meta.getOwner();
			if(meta.hasDisplayName())
				this.displayName = meta.getDisplayName();
			if(meta.hasLore()) {
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
	}
	
	public String getOwner() {
		return owner;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getLore() {
		return lore;
	}
}
