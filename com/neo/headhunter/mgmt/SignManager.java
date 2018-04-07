package com.neo.headhunter.mgmt;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.BountyDB;
import com.neo.headhunter.factory.RateFactory;
import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.sign.SellingSign;
import com.neo.headhunter.util.item.sign.WantedSign;
import com.neo.headhunter.util.config.Accessor;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.general.Duplet;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class SignManager extends BukkitRunnable {
	private HeadHunter plugin;
	private BountyDB bountyDB;
	private RateFactory rateFactory;
	
	private Map<Location, SellingSign> sellingSigns;
	private Map<Location, WantedSign> wantedSigns;
	private Map<Player, Location> signLinks;
	
	public SignManager(HeadHunter plugin) {
		this.plugin = plugin;
		this.bountyDB = plugin.getBountyDB();
		this.rateFactory = plugin.getRateFactory();
		
		this.sellingSigns = new HashMap<>();
		this.wantedSigns = new HashMap<>();
		this.signLinks = new HashMap<>();
		loadSigns();
	}
	
	private void updateSellingSigns() {
		for(Map.Entry<Location, SellingSign> entry : sellingSigns.entrySet()) {
			Location signLoc = entry.getKey();
			if (!(signLoc.getBlock().getState() instanceof Sign))
				return;
			for (Player p : signLoc.getWorld().getPlayers()) {
				if (p.getLocation().distance(signLoc) < 100) {
					String hunterName = p.getName();
					double rate = rateFactory.getPlayerSellRate(p);
					p.sendSignChange(signLoc, new String[] {
							Utils.f(Settings.getSign_selling_1(), hunterName, "", rate, 1),
							Utils.f(Settings.getSign_selling_2(), hunterName, "", rate, 1),
							Utils.f(Settings.getSign_selling_3(), hunterName, "", rate, 1),
							Utils.f(Settings.getSign_selling_4(), hunterName, "", rate, 1)
					});
				}
			}
		}
	}
	
	private void updateWantedSigns() {
		int minIndex = -1, maxIndex = -1;
		for(WantedSign sign : wantedSigns.values()) {
			if(minIndex == -1 || maxIndex == -1) {
				minIndex = sign.getBountyIndex();
				maxIndex = sign.getBountyIndex();
			}
			else {
				minIndex = Math.min(minIndex, sign.getBountyIndex());
				maxIndex = Math.max(maxIndex, sign.getBountyIndex());
			}
		}
		List<Duplet<UUID, Double>> topBounties = bountyDB.getTopBounties(minIndex + 1, maxIndex + 1);
		for(Map.Entry<Location, WantedSign> entry : wantedSigns.entrySet()) {
			Location signLoc = entry.getKey();
			if (!(signLoc.getBlock().getState() instanceof Sign))
				return;
			WantedSign sign = entry.getValue();
			Duplet<UUID, Double> targetBounty = topBounties.get(sign.getBountyIndex());
			OfflinePlayer target = PlayerUtils.getPlayer(targetBounty.getT());
			String targetName = target == null ? "N/A" : target.getName();
			for (Player p : signLoc.getWorld().getPlayers()) {
				if (p.getLocation().distance(signLoc) < 100) {
					String hunterName = p.getName();
					double value = targetBounty.getU();
					p.sendSignChange(signLoc, new String[] {
							Utils.f(Settings.getSign_wanted_1(), hunterName, targetName, value, 0),
							Utils.f(Settings.getSign_wanted_2(), hunterName, targetName, value, 0),
							Utils.f(Settings.getSign_wanted_3(), hunterName, targetName, value, 0),
							Utils.f(Settings.getSign_wanted_4(), hunterName, targetName, value, 0)
					});
				}
			}
			if(bountyDB.isHeadUpdateRequired())
				HeadUtils.updateHead(sign.getHeadLocation(), target == null ? null : targetName);
		}
	}
	
	@Override
	public void run() {
		updateSellingSigns();
		updateWantedSigns();
	}
	
	public void registerSellingSign(Player owner, Location signLoc) {
		if(signLoc.getBlock().getState() instanceof Sign) {
			sellingSigns.put(signLoc, new SellingSign(owner.getUniqueId()));
			saveSellingSigns();
		}
	}
	
	public void unregisterSellingSign(Location signLoc) {
		sellingSigns.remove(signLoc);
		saveSellingSigns();
	}
	
	public boolean isSellingSign(Location signLoc) {
		return signLoc != null && signLoc.getBlock().getState() instanceof Sign && sellingSigns.containsKey(signLoc);
	}
	
	public SellingSign getSellingSign(Location signLoc) {
		return sellingSigns.get(signLoc);
	}
	
	public void registerWantedSign(Player owner, Location signLoc, int index) {
		if(signLoc.getBlock().getState() instanceof Sign) {
			UUID ownerID = owner.getUniqueId();
			WantedSign wantedSign = new WantedSign(ownerID);
			wantedSign.setBountyIndex(Math.max(index, 0));
			wantedSigns.put(signLoc, wantedSign);
			saveWantedSigns();
		}
	}
	
	public void unregisterWantedSign(Location signLoc) {
		wantedSigns.remove(signLoc);
		saveWantedSigns();
	}
	
	public boolean isWantedSign(Location signLoc) {
		return signLoc != null && signLoc.getBlock().getState() instanceof Sign && wantedSigns.containsKey(signLoc);
	}
	
	public WantedSign getWantedSign(Location signLoc) {
		return wantedSigns.get(signLoc);
	}
	
	public void saveSigns() {
		saveSellingSigns();
		saveWantedSigns();
	}
	
	public void saveSellingSigns() {
		Accessor access = plugin.access(Utils.SGN);
		access.getConfig().set("selling", null);
		for(Map.Entry<Location, SellingSign> entry : sellingSigns.entrySet()) {
			String locTag = Utils.parseLocation(entry.getKey());
			SellingSign sellingSign = entry.getValue();
			access.getConfig().set("selling." + locTag + ".owner", sellingSign.getOwner().toString());
		}
		access.saveConfig();
	}
	
	public void saveWantedSigns() {
		Accessor access = plugin.access(Utils.SGN);
		access.getConfig().set("wanted", null);
		for(Map.Entry<Location, WantedSign> entry : wantedSigns.entrySet()) {
			String locTag = Utils.parseLocation(entry.getKey());
			WantedSign wantedSign = entry.getValue();
			access.getConfig().set("wanted." + locTag + ".owner", wantedSign.getOwner().toString());
			access.getConfig().set("wanted." + locTag + ".head-location", Utils.parseLocation(wantedSign.getHeadLocation()));
			access.getConfig().set("wanted." + locTag + ".list-index", wantedSign.getBountyIndex());
		}
		access.saveConfig();
	}
	
	public void loadSigns() {
		sellingSigns.clear();
		wantedSigns.clear();
		Accessor access = plugin.access(Utils.SGN);
		ConfigurationSection sellingSection = access.getConfig().getConfigurationSection("selling");
		if(sellingSection != null) {
			for(String locTag : sellingSection.getKeys(false)) {
				Location loc = Utils.readLocation(locTag);
				UUID owner = UUID.fromString(sellingSection.getString(locTag + ".owner"));
				SellingSign sellingSign = new SellingSign(owner);
				sellingSigns.put(loc, sellingSign);
			}
		}
		ConfigurationSection wantedSection = access.getConfig().getConfigurationSection("wanted");
		if(wantedSection != null) {
			for(String locTag : wantedSection.getKeys(false)) {
				Location loc = Utils.readLocation(locTag);
				UUID owner = UUID.fromString(wantedSection.getString(locTag + ".owner"));
				Location headLoc = Utils.readLocation(wantedSection.getString(locTag + ".head-location"));
				int index = wantedSection.getInt(locTag + ".list-index");
				WantedSign wantedSign = new WantedSign(owner);
				wantedSign.setHeadLocation(headLoc);
				wantedSign.setBountyIndex(index);
				wantedSigns.put(loc, wantedSign);
			}
		}
	}
	
	public void putSignLink(Player p, Location loc) {
		signLinks.put(p, loc);
	}
	
	public Location getSignLink(Player p) {
		return signLinks.get(p);
	}
	
	public void removeSignLink(Player p) {
		signLinks.remove(p);
	}
}
