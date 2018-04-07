package com.neo.headhunter.util.item;

import org.bukkit.inventory.ItemStack;

public enum ToolType {
	SWORD, BOW, AXE, PICKAXE, SPADE, HOE, ROD, FIST;
	
	public static ToolType getType(ItemStack item) {
		if(item != null && item.getType() != null) {
			String matString = item.getType().toString();
			if(matString.endsWith("_SWORD"))
				return SWORD;
			if(matString.endsWith("BOW"))
				return BOW;
			if(matString.endsWith("_PICKAXE"))
				return PICKAXE;
			if(matString.endsWith("_AXE"))
				return AXE;
			if(matString.endsWith("_SPADE"))
				return SPADE;
			if(matString.endsWith("_HOE"))
				return HOE;
			if(matString.endsWith("FISHING_ROD"))
				return ROD;
		}
		return FIST;
	}
}
