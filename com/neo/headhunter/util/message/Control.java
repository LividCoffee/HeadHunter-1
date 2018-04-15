package com.neo.headhunter.util.message;

import com.neo.headhunter.util.Utils;

public enum Control {
	RELOADED            ("&cHeadHunter v%% reloaded."),
	PLAYERS_ONLY        ("&cThe '%%' command can only be used by players."),
	WORLD_ADDED         ("&aHeadHunter world %% added."),
	WORLD_REMOVED       ("&aHeadHunter world %% removed."),
	WORLD_EXISTS        ("&c%% is already a HeadHunter world."),
	WORLD_INVALID       ("&c%% is not a HeadHunter world."),
	WORLD_NULL          ("&c%% is not a registered world."),
	SIGN_CREATED        ("&aSelling sign created."),
	SIGN_REMOVED        ("&aSelling sign removed."),
	WANTED_OPENED       ("&eWanted sign link started. Click a normal skull to finish."),
	WANTED_LINKED       ("&aWanted sign linked."),
	WANTED_CREATED      ("&aWanted sign created."),
	WANTED_REMOVED      ("&aWanted sign removed.");
	
	private String s;
	
	Control(String s) {
		this.s = s;
	}
	
	public String success(Object... args) {
		String result = "§2" + s;
		for(Object o : args) {
			if(o == null)
				result = result.replaceFirst("%%", "§a null §2");
			else
				result = result.replaceFirst("%%", "§a" + o.toString() + "§2");
		}
		return Utils.color(result);
	}
	
	public String info(Object... args) {
		String result = "§6" + s;
		for(Object o : args) {
			if(o == null)
				result = result.replaceFirst("%%", "§e null §6");
			else
				result = result.replaceFirst("%%", "§e" + o.toString() + "§6");
		}
		return Utils.color(result);
	}
	
	public String error(Object... args) {
		String result = "§c" + s;
		for(Object o : args) {
			if(o == null)
				result = result.replaceFirst("%%", "§4 null §c");
			else
				result = result.replaceFirst("%%", "§4" + o.toString() + "§c");
		}
		return Utils.color(result);
	}
}
