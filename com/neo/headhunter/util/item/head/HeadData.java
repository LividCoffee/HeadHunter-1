package com.neo.headhunter.util.item.head;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class HeadData {
	private final String owner;
	private final String displayName;
	private final String lore;
	
	public HeadData(ItemStack head) {
		if(head != null && head.getType() == Material.SKULL_ITEM) {
			SkullMeta meta = (SkullMeta) head.getItemMeta();
			this.owner = meta.getOwner();
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
		else {
			this.owner = null;
			this.displayName = null;
			this.lore = null;
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
