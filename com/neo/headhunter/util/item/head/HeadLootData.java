package com.neo.headhunter.util.item.head;

import org.bukkit.OfflinePlayer;

public final class HeadLootData {
	private final double balanceValue, bountyValue, withdraw;
	private final OfflinePlayer godfather;
	private final boolean mobHead;
	
	public HeadLootData(double balanceValue, double bountyValue, double withdraw, OfflinePlayer godfather, boolean mobHead) {
		this.balanceValue = balanceValue;
		this.bountyValue = bountyValue;
		this.withdraw = withdraw;
		this.godfather = godfather;
		this.mobHead = mobHead;
	}
	
	public double getBalanceValue() {
		return balanceValue;
	}
	
	public double getBountyValue() {
		return bountyValue;
	}
	
	public double getWithdraw() {
		return withdraw;
	}
	
	public OfflinePlayer getGodfather() {
		return godfather;
	}
	
	public boolean isMobHead() {
		return mobHead;
	}
}
