package com.neo.headhunter.factory;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.general.LevelMultMap;
import com.neo.headhunter.util.mob.MobLibrary;
import com.neo.headhunter.util.mob.MobSettings;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * Requires: MobLibrary
 */
public final class RateFactory {
	private MobLibrary mobLibrary;
	private LevelMultMap playerLMM, mobLMM;
	
	public RateFactory(HeadHunter plugin) {
		this.mobLibrary = plugin.getMobLibrary();
		this.playerLMM = new LevelMultMap(Settings.getEnchant_looting_map());
		this.mobLMM = new LevelMultMap(MobSettings.getEnchant_looting_map());
	}
	
	public double getPlayerSellRate(Player hunter) {
		if(hunter != null) {
			for (PermissionAttachmentInfo pai : hunter.getEffectivePermissions()) {
				String perm = pai.getPermission();
				if (perm.matches("\\Qhunter.sellrate:\\E[0-9.]+")) {
					if(pai.getValue()) {
						String num = perm.replace("hunter.sellrate:", "");
						if (Utils.isNumeric(num))
							return Double.parseDouble(num);
					}
				}
			}
			return Settings.getSell_rate();
		}
		return Settings.getSell_rate();
	}
	
	public int getPlayerDropRate(Player hunter) {
		if(hunter != null) {
			for (PermissionAttachmentInfo pai : hunter.getEffectivePermissions()) {
				String perm = pai.getPermission();
				if (perm.matches("\\Qhunter.droprate:\\E[0-9]+")) {
					if(pai.getValue()) {
						String num = perm.replace("hunter.droprate:", "");
						if (Utils.isInteger(num))
							return Integer.parseInt(num);
					}
				}
			}
			return Settings.getDrop_chance();
		}
		return 100;
	}
	
	public double getMobSellRate(Player hunter, LivingEntity target) {
		return getMobSellRate(hunter, mobLibrary.getEntityTag(target));
	}
	
	public double getMobSellRate(Player hunter, String entityTag) {
		if(entityTag != null) {
			if (hunter != null) {
				for (PermissionAttachmentInfo pai : hunter.getEffectivePermissions()) {
					String perm = pai.getPermission();
					if (perm.matches("\\Qhunter.sellrate.\\E[A-Za-z]+:[0-9.]")) {
						if(pai.getValue()) {
							String info = perm.replace("hunter.sellrate.", "");
							String[] mobRate = info.split(":");
							if (mobRate[0].equalsIgnoreCase(entityTag) && Utils.isNumeric(mobRate[1]))
								return Double.parseDouble(mobRate[1]);
						}
					}
				}
			}
			return mobLibrary.getEntityValue(entityTag);
		}
		return 0.0;
	}
	
	public int getMobDropRate(Player hunter, LivingEntity target) {
		return getMobDropRate(hunter, mobLibrary.getEntityTag(target));
	}
	
	public int getMobDropRate(Player hunter, String entityTag) {
		if(entityTag != null) {
			if(hunter != null) {
				for (PermissionAttachmentInfo pai : hunter.getEffectivePermissions()) {
					String perm = pai.getPermission();
					if (perm.matches("\\Qhunter.droprate.\\E[A-Za-z]+:[0-9]+")) {
						if(pai.getValue()) {
							String info = perm.replace("hunter.droprate.", "");
							String[] mobRate = info.split(":");
							if ((   mobRate[0].equalsIgnoreCase(entityTag) || mobRate[0].equalsIgnoreCase("mobs")) &&
									    Utils.isNumeric(mobRate[1]))
								return Integer.parseInt(mobRate[1]);
						}
					}
				}
			}
			return mobLibrary.getEntityDropRate(entityTag);
		}
		return 0;
	}
	
	public double getLootingMultiplier(ItemStack weapon, LivingEntity target) {
		if(weapon == null || weapon.getType() == Material.AIR)
			return 1.0;
		if(target instanceof Player)
			return playerLMM.getMultiplier(weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
		return mobLMM.getMultiplier(weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS));
	}
	
	public boolean hasDecapitation(ItemStack weapon) {
		if(weapon != null && weapon.getType() != Material.AIR) {
			ItemMeta meta = weapon.getItemMeta();
			if(meta.hasLore()) {
				for(String line : meta.getLore())
					if(Utils.strip(line).startsWith("Decapitation"))
						return true;
			}
		}
		return false;
	}
}
