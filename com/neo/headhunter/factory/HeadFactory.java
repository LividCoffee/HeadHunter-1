package com.neo.headhunter.factory;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.item.head.HeadLoot;
import com.neo.headhunter.util.item.head.HeadLootData;
import com.neo.headhunter.util.mob.MobLibrary;
import com.neo.headhunter.util.mob.MobSettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

/**
 * Requires: Economy, BountyRegister, MobLibrary, RateFactory
 */
public final class HeadFactory {
	private Economy economy;
	
	private BountyRegister bountyRegister;
	
	private MobLibrary mobLibrary;
	private RateFactory rateFactory;
	
	public HeadFactory(HeadHunter plugin) {
		this.economy = plugin.getEconomy();
		
		this.bountyRegister = plugin.getHHDB().getBountyRegister();
		
		this.mobLibrary = plugin.getMobLibrary();
		this.rateFactory = plugin.getRateFactory();
	}
	
	public HeadLoot createHead(Player hunter, LivingEntity target, HeadLootData data) {
		double headValue = HeadUtils.getFinalHeadValue(data);
		LivingEntity whereToDrop = target;
		boolean insert = false;
		
		ItemStack baseHead;
		String targetName, stackName, stackLore;
		if(data.isMobHead()) {
			baseHead = mobLibrary.getBaseHead(target);
			targetName = mobLibrary.getEntityName(target);
			stackName = MobSettings.getHead_format_title();
			if(headValue == 0)
				stackLore = MobSettings.getHead_format_worthless();
			else
				stackLore = MobSettings.getHead_format_worth();
		}
		else {
			baseHead = HeadUtils.getPlayerHead((Player) target);
			targetName = target.getName();
			stackName = Settings.getHead_format_title();
			if(headValue == 0)
				stackLore = Settings.getHead_format_worthless();
			else
				stackLore = Settings.getHead_format_worth();
			
			if(Settings.isBounty_godfatherMode()) {
				OfflinePlayer godfather = data.getGodfather();
				if(godfather.isOnline()) {
					whereToDrop = godfather.getPlayer();
					insert = true;
				}
			}
		}
		
		stackName = Utils.f(stackName, hunter.getName(), targetName, headValue, 1);
		stackLore = Utils.f(stackLore, hunter.getName(), targetName, headValue, 1);
		SkullMeta meta = (SkullMeta) baseHead.getItemMeta();
		meta.setDisplayName(stackName);
		meta.setLore(Arrays.asList(stackLore.split("\\Q\n\\E")));
		baseHead.setItemMeta(meta);
		
		return new HeadLoot(baseHead, whereToDrop, insert);
	}
	
	public HeadLootData createHeadLootData(Player hunter, LivingEntity target) {
		double balanceValue = 0.0;
		double bountyValue = 0.0;
		double withdraw = 0.0;
		OfflinePlayer godfather = null;
		boolean mobHead = false;
		if(target instanceof Player) {
			Player targetPlayer = (Player) target;
			if(!Settings.isHoardMode()) {
				if (Settings.isHead_value_useBalance()) {
					double targetBalance = economy.getBalance(targetPlayer);
					double hunterSellRate = rateFactory.getPlayerSellRate(hunter);
					if(Settings.isUsePercentage())
						balanceValue = targetBalance * (hunterSellRate / 100.0);
					else
						balanceValue = hunterSellRate;
					balanceValue = Math.min(balanceValue, targetBalance);
					if(balanceValue < Settings.getHead_value_minWorth())
						balanceValue = 0;
					withdraw = balanceValue;
				}
				if (Settings.isHead_value_useBounty()) {
					bountyValue = bountyRegister.getTotalBounty(targetPlayer);
					godfather = bountyRegister.getGodfather(targetPlayer);
				}
			}
		}
		else {
			if(!MobSettings.isHoardMode()) {
				balanceValue = rateFactory.getMobSellRate(hunter, target);
				mobHead = true;
			}
		}
		return new HeadLootData(balanceValue, bountyValue, withdraw, godfather, mobHead);
	}
}
