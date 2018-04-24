package com.neo.headhunter.util.message;

import com.neo.headhunter.util.Utils;

public enum Control {
	RELOADED            ("HeadHunter v%% reloaded."),
	PLAYERS_ONLY        ("The '%%' command can only be used by players."),
	WORLD_ADDED         ("HeadHunter world %% added."),
	WORLD_REMOVED       ("HeadHunter world %% removed."),
	WORLD_EXISTS        ("%% is already a HeadHunter world."),
	WORLD_INVALID       ("%% is not a HeadHunter world."),
	WORLD_NULL          ("%% is not a registered world."),
	SIGN_CREATED        ("Selling sign created."),
	SIGN_REMOVED        ("Selling sign removed."),
	WANTED_OPENED       ("Wanted sign link started. Click a normal skull to finish."),
	WANTED_LINKED       ("Wanted sign linked."),
	WANTED_CREATED      ("Wanted sign created."),
	WANTED_REMOVED      ("Wanted sign removed.");
	
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
