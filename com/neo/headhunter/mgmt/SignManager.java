package com.neo.headhunter.mgmt;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.database.SignRegister;
import com.neo.headhunter.factory.RateFactory;
import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.general.Duplet;
import com.neo.headhunter.util.general.MappedList;
import com.neo.headhunter.util.item.sign.WantedSign;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SignManager extends BukkitRunnable {
	private BountyRegister bountyRegister;
	private SignRegister signRegister;
	private RateFactory rateFactory;
	
	private Map<Player, Location> signLinks;
	
	public SignManager(HeadHunter plugin) {
		this.bountyRegister = plugin.getHHDB().getBountyRegister();
		this.signRegister = plugin.getHHDB().getSignRegister();
		this.rateFactory = plugin.getRateFactory();
		
		this.signLinks = new HashMap<>();
	}
	
	private void updateSellingSigns() {
		for(Location signLoc : signRegister.getSellingSigns()) {
			if (!(signLoc.getBlock().getState() instanceof Sign))
				return;
			if(signLoc.getChunk().isLoaded()) {
				for (Player p : signLoc.getWorld().getPlayers()) {
					if (p.getLocation().distance(signLoc) < 70) {
						String hunterName = p.getName();
						double rate = rateFactory.getPlayerSellRate(p);
						p.sendSignChange(signLoc, new String[]{
								Utils.f(Settings.getSign_selling_1(), hunterName, "", rate, 1),
								Utils.f(Settings.getSign_selling_2(), hunterName, "", rate, 1),
								Utils.f(Settings.getSign_selling_3(), hunterName, "", rate, 1),
								Utils.f(Settings.getSign_selling_4(), hunterName, "", rate, 1)
						});
					}
				}
			}
		}
	}
	
	private void updateWantedSigns() {
		MappedList<UUID, Double> sortedBounties = bountyRegister.getSortedBounties();
		for(Location signLoc : signRegister.getWantedSigns()) {
			if (!(signLoc.getBlock().getState() instanceof Sign))
				return;
			if(signLoc.getChunk().isLoaded()) {
				WantedSign sign = signRegister.getWantedSign(signLoc);
				if (sign != null) {
					Duplet<UUID, Double> targetBounty = sortedBounties.get(sign.getBountyIndex());
					OfflinePlayer target = targetBounty == null ? null : PlayerUtils.getPlayer(targetBounty.getT());
					String targetName = target == null ? "N/A" : target.getName();
					for (Player p : signLoc.getWorld().getPlayers()) {
						if (p.getLocation().distance(signLoc) < 70) {
							String hunterName = p.getName();
							double value = targetBounty == null ? 0 : targetBounty.getU();
							p.sendSignChange(signLoc, new String[]{
									Utils.f(Settings.getSign_wanted_1(), hunterName, targetName, value, 1),
									Utils.f(Settings.getSign_wanted_2(), hunterName, targetName, value, 1),
									Utils.f(Settings.getSign_wanted_3(), hunterName, targetName, value, 1),
									Utils.f(Settings.getSign_wanted_4(), hunterName, targetName, value, 1)
							});
						}
					}
					if (bountyRegister.isHeadUpdateRequired())
						HeadUtils.updateHead(sign.getHeadLocation(), target == null ? null : targetName);
				}
			}
		}
	}
	
	@Override
	public void run() {
		updateSellingSigns();
		updateWantedSigns();
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
