package com.neo.headhunter.factory;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;
import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.WorldRegister;
import com.neo.headhunter.listener.support.ListenerMinigames;
import com.neo.headhunter.mgmt.CooldownManager;
import com.neo.headhunter.util.MetaUtils;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.item.ToolType;
import com.neo.headhunter.util.mob.MobSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Requires: ListenerMinigames, CooldownManager, WorldRegister, RateFactory
 */
public final class DropFactory {
	private ListenerMinigames listenerMinigames;
	private CooldownManager cooldownManager;
	
	private WorldRegister worldRegister;
	private RateFactory rateFactory;
	
	public DropFactory(HeadHunter plugin) {
		this.listenerMinigames = plugin.getListenerMinigames();
		this.cooldownManager = plugin.getCooldownManager();
		
		this.worldRegister = plugin.getHHDB().getWorldRegister();
		this.rateFactory = plugin.getRateFactory();
	}
	
	public double getDropChance(Player hunter, LivingEntity target, ItemStack weapon) {
		if(isNoDrop(target))
			return 0;
		if(isMaster(hunter))
			return 100;
		if(!worldRegister.isValidDropLocation(target))
			return 0;
		
		if(!isValidDrop_Factions(target))
			return 0;
		if(!isValidDrop_Minigames(hunter, target))
			return 0;
		if(!isValidDrop_MobStacker(target))
			return 0;
		
		if(!isValidWeapon(weapon))
			return 0;
		
		double finalDropRate;
		if(target instanceof Player) {
			if(cooldownManager.isOnCooldown((Player) target))
				return 0;
			finalDropRate = rateFactory.getPlayerDropRate(hunter);
		}
		else {
			if(!isAllowedSpawnReason(target))
				return 0;
			if(!isAllowedDeathReason(target))
				return 0;
			finalDropRate = rateFactory.getMobDropRate(hunter, target);
		}
		finalDropRate = alterByEnchantments(finalDropRate, target, weapon);
		return finalDropRate;
	}
	
	private boolean isValidDrop_Factions(LivingEntity target) {
		if(!Utils.isPluginEnabled("Factions"))
			return true;
		boolean player = target instanceof Player;
		boolean mainWilderness = Settings.isSupport_factions_dropWilderness();
		boolean mobWilderness = MobSettings.isSupport_factions_dropWilderness();
		boolean mainSafezone = Settings.isSupport_factions_dropSafezone();
		boolean mobSafezone = MobSettings.isSupport_factions_dropSafezone();
		boolean mainWarzone = Settings.isSupport_factions_dropWarzone();
		boolean mobWarzone = MobSettings.isSupport_factions_dropWarzone();
		if(Utils.isPluginEnabled("Factions")) {
			if (Bukkit.getPluginManager().getPlugin("Factions").getDescription().getDepend().contains("MassiveCore")) {
				//Factions plugin is MassiveCore Factions
				com.massivecraft.factions.entity.Faction d = FactionColl.get().getNone();
				com.massivecraft.factions.entity.Faction s = FactionColl.get().getSafezone();
				com.massivecraft.factions.entity.Faction w = FactionColl.get().getWarzone();
				com.massivecraft.factions.entity.Faction f = BoardColl.get().getFactionAt(PS.valueOf(target.getLocation()));
				if (f.equals(d)) return player ? mainWilderness : mobWilderness;
				else if (f.equals(s)) return player ? mainSafezone : mobSafezone;
				else if (f.equals(w)) return player ? mainWarzone : mobWarzone;
			} else if (Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("drtshock")) {
				//Factions plugin is FactionsUUID
				FLocation loc = new FLocation(target.getLocation());
				com.massivecraft.factions.Faction f = Board.getInstance().getFactionAt(loc);
				if (f.isWilderness()) return player ? mainWilderness : mobWilderness;
				else if (f.isSafeZone()) return player ? mainSafezone : mobSafezone;
				else if (f.isWarZone()) return player ? mainWarzone : mobWarzone;
			} else if (Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("externo6")) {
				//Factions plugin is FactionsOne -- unable to be supported because of package names
			}
		}
		return true;
	}
	
	private boolean isValidDrop_Minigames(Player hunter, LivingEntity target) {
		if(!Utils.isPluginEnabled("Minigames") || hunter == null)
			return true;
		if(target instanceof Player)
			return Settings.isSupport_minigames_dropGame() || !listenerMinigames.isInMinigame((Player) target);
		return MobSettings.isSupport_minigames_dropGame() || !listenerMinigames.isInMinigame(hunter);
	}
	
	private boolean isValidDrop_MobStacker(LivingEntity target) {
		if(!Utils.isPluginEnabled("MobStacker") && !Utils.isPluginEnabled("StackMob"))
			return true;
		if(target instanceof Player)
			return true;
		if(Settings.isSupport_mobstacker_dropEach())
			return true;
		return !MetaUtils.isHunterMarked(target);
	}
	
	private boolean isNoDrop(LivingEntity target) {
		return PlayerUtils.hasExplicitPermission(target, "hunter.nodrop");
	}
	
	private boolean isMaster(Player hunter) {
		return PlayerUtils.hasExplicitPermission(hunter, "hunter.master");
	}
	
	private boolean isAllowedSpawnReason(LivingEntity target) {
		if(target instanceof Player)
			return true;
		CreatureSpawnEvent.SpawnReason reason = MetaUtils.getSpawnReason(target);
		for(CreatureSpawnEvent.SpawnReason preventReason : MobSettings.getDrop_preventSpawnReasons()) {
			if(reason == preventReason)
				return false;
		}
		return true;
	}
	
	private boolean isAllowedDeathReason(LivingEntity target) {
		if(target instanceof Player)
			return true;
		EntityDamageEvent.DamageCause reason = target.getLastDamageCause().getCause();
		for(EntityDamageEvent.DamageCause preventReason : MobSettings.getDrop_preventDeathReasons()) {
			if(reason == preventReason)
				return false;
		}
		return true;
	}
	
	private boolean isValidWeapon(ItemStack weapon) {
		ToolType type = ToolType.getType(weapon);
		switch(type) {
		case SWORD:
			return Settings.isDrop_weapon_swords();
		case BOW:
			return Settings.isDrop_weapon_bows();
		case AXE:
			return Settings.isDrop_weapon_axes();
		case PICKAXE:
			return Settings.isDrop_weapon_pickaxes();
		case SPADE:
			return Settings.isDrop_weapon_spades();
		case HOE:
			return Settings.isDrop_weapon_hoes();
		case ROD:
			return Settings.isDrop_weapon_rods();
		default:
			return Settings.isDrop_weapon_fists();
		}
	}
	
	private double alterByEnchantments(double currentDropRate, LivingEntity target, ItemStack weapon) {
		double lootingMultiplier = rateFactory.getLootingMultiplier(weapon, target);
		boolean hasDecapitation = rateFactory.hasDecapitation(weapon);
		if(target instanceof Player) {
			if(Settings.isEnchant_looting_enabled())
				currentDropRate *= lootingMultiplier;
			if(Settings.isEnchant_custom_enabled()) {
				double decapitationMultiplier = Settings.getEnchant_custom_dropRate();
				if(decapitationMultiplier < 0)
					currentDropRate = hasDecapitation ? 100 : 0;
				else
					currentDropRate *= decapitationMultiplier;
			}
		}
		else {
			if(MobSettings.isEnchant_looting_enabled())
				currentDropRate *= lootingMultiplier;
			if(MobSettings.isEnchant_custom_enabled()) {
				double decapitationMultiplier = MobSettings.getEnchant_custom_dropRate();
				if(decapitationMultiplier < 0)
					currentDropRate = hasDecapitation ? 100 : 0;
				else
					currentDropRate *= decapitationMultiplier;
			}
		}
		return currentDropRate;
	}
}
