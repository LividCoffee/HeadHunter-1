package com.neo.headhunter.util;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.config.AuxResource;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.item.head.HeadData;
import com.neo.headhunter.util.item.head.HeadLootData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class HeadUtils {
	public static void updateHead(Location headLoc, String headOwner) {
		if(headLoc != null && headLoc.getBlock().getState() instanceof Skull) {
			Skull head = (Skull) headLoc.getBlock().getState();
			head.setSkullType(SkullType.PLAYER);
			head.setOwner(headOwner != null ? headOwner : "MHF_Question");
			head.update(true, false);
		}
	}
	
	public static double getFinalHeadValue(HeadLootData data) {
		double result = data.getBalanceValue();
		if(!data.isMobHead() && data.getBountyValue() > 0) {
			if(Settings.isHead_value_cumulative())
				result += data.getBountyValue();
			else
				result = data.getBountyValue();
		}
		return result;
	}
	
	public static ItemStack getPlayerHead(Player target) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwner(target.getName());
		head.setItemMeta(meta);
		return head;
	}
	
	public static double getStackValue(ItemStack head) {
		return getSingleValue(head) * head.getAmount();
	}
	
	public static double getSingleValue(ItemStack head) {
		double headValue = 0.0;
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		if(meta.hasLore()) {
			for (String line : meta.getLore()) {
				String toWork = findSuitableMoneyString(Utils.strip(line));
				if (toWork != null && !toWork.isEmpty()) {
					headValue = Double.parseDouble(toWork);
					break;
				}
			}
		}
		return headValue;
	}
	
	private static String findSuitableMoneyString(String s) {
		String[] tokens = s.split("[^0-9.,%]");
		for(int i = 0; i < tokens.length; i++)
			tokens[i] = tokens[i].replace(",", ".");
		String result = null;
		for(String token : tokens) {
			if(Utils.isMoney(token)) {
				result = token;
			}
		}
		return result;
	}
	
	public static boolean isAllowedDecapitation(ItemStack item) {
		if(item == null)
			return false;
		if(Settings.isEnchant_custom_allowedBows() && EnchantmentTarget.BOW.includes(item))
			return true;
		if(Settings.isEnchant_custom_allowedTools() && EnchantmentTarget.TOOL.includes(item))
			return true;
		return EnchantmentTarget.WEAPON.includes(item);
	}
	
	public static HeadData getData(ItemStack head) {
		return new HeadData(head);
	}
	
	public static void saveItemStack(HeadHunter plugin, String key, ItemStack item) {
		AuxResource aux = new AuxResource(plugin, Utils.TMP);
		FileConfiguration config = aux.getConfig();
		config.set(key, item);
		aux.saveConfig();
	}
}
