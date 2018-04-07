package com.neo.headhunter.util.message;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Accessor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Message {
	NO_PERMS            ("&cYou don't have permission!"),
	SELL_NOTIFY         ("&6{HUNTER} &6sold {TARGET}&6 x{AMOUNT} for ${VALUE}!"),
	SELL_INVENTORY      ("&6{HUNTER} &6sold {AMOUNT} heads(s) for ${VALUE}!"),
	SELL_WORTHLESS      ("&cThis skull is worthless! It has been removed from your inventory."),
	SELL_SIGN_ONLY      ("&cHeads must be sold through signs!"),
	NO_HEADS            ("&cYou are not holding any heads!"),
	NO_BOUNTIES         ("&cThere are no bounties!"),
	BOUNTY_REMOVE_ERR   ("&cYou have not added that much on that target!"),
	BOUNTY_ADD_ERR      ("&cYou do not have enough money!"),
	BOUNTY_LOW          ("&cThat bounty is too low!"),
	BOUNTY_COMPLETE     ("&aYour bounty target has been killed!"),
	BOUNTY_ADDED        ("&e{HUNTER}&6 has added a bounty of&e ${VALUE}&6 on&e {TARGET}&6!"),
	BOUNTY_REMOVED      ("&e{HUNTER}&6 has removed&e ${VALUE}&6 from&e {TARGET}&6!"),
	BOUNTIES_NO_PAGE    ("&cThat is an invalid page!"),
	BOUNTIES_HEADER     ("&6{PREV} &eBounty List &6(&8{PAGE}&6/&8{TOTAL}&6) {NEXT}"),
	BOUNTY_TOTAL        ("&6Total Bounty on &e{TARGET}&6:&e ${VALUE}"),
	BOUNTY_PERSONAL     ("&6Your Bounty on &e{TARGET}&6:&e ${VALUE}"),
	AMOUNT_INVALID      ("&cThat is an invalid amount!"),
	AMOUNT_HIGH         ("&cYou don't have enough money!"),
	SELF_TARGET         ("&cYou cannot target yourself!"),
	PLAYER_INVALID      ("&c{TARGET} is not a valid player!"),
	HOARD_MODE          ("&cYou cannot do that in Hoard Mode!");
	
	private String s;
	
	Message(String s) {
		this.s = s;
	}
	
	public static void save(HeadHunter plugin) {
		Accessor access = plugin.access(Utils.MSG);
		access.resetConfig();
		FileConfiguration config = access.getConfig();
		for(Message msg : Message.values())
			config.set(msg.toString(), msg.s);
		access.saveConfig();
	}
	
	public static void load(HeadHunter plugin) {
		Accessor access = plugin.access(Utils.MSG);
		FileConfiguration config = access.getConfig();
		for(String key : config.getKeys(false)) {
			try {
				Message.valueOf(key).s = config.getString(key);
			} catch(Exception e) {
				//Ignore invalid options
			}
		}
		access.saveConfig();
	}
	
	public String f() {
		return Utils.color(s);
	}
	
	public String f(String hunter, String target, double value, int amount) {
		String result = s;
		result = result.replace("{HUNTER}", hunter);
		result = result.replace("{TARGET}", target);
		result = result.replace("{VALUE}", Utils.toMoney(value));
		result = result.replace("{AMOUNT}", String.valueOf(amount));
		return Utils.color(result);
	}
	
	public String f(int page, int total) {
		String result = s;
		result = result.replace("{PAGE}", String.valueOf(page));
		result = result.replace("{TOTAL}", String.valueOf(total));
		return Utils.color(result);
	}
}
