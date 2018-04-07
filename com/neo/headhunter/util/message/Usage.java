package com.neo.headhunter.util.message;

public enum Usage {
	CMD_SELLHEAD        ("/hunter sellhead"),
	CMD_ADD             ("/hunter addworld [world]"),
	CMD_REMOVE          ("/hunter removeworld [world]"),
	CMD_BOUNTY          ("/hunter bounty <add/remove/check/list>"),
	CMD_BOUNTY_ADD      ("/hunter bounty add <target> <amount>"),
	CMD_BOUNTY_REMOVE   ("/hunter bounty remove <target> [amount]"),
	CMD_BOUNTY_CHECK    ("/hunter bounty check <target>"),
	CMD_BOUNTY_LIST     ("/hunter bounty list [page]"),
	HELP_SELLHEAD       ("Sell the factory stack in your hand"),
	HELP_ADD            ("Register a world as a HeadHunter world"),
	HELP_REMOVE         ("Unregister a world as a HeadHunter world"),
	HELP_BOUNTY         ("Edit or view existing bounties");
	
	private String s;
	
	Usage(String s) {
		this.s = s;
	}
	
	public String usage() {
		String result = "§8Usage: §6" + s;
		result = result.replace("<", "§6<§e");
		result = result.replace(">", "§6>§e");
		result = result.replace("[", "§6[§e");
		result = result.replace("]", "§6]§e");
		result = result.replace(",", "§6,§e");
		result = result.replace("/", "§6/§e");
		return result;
	}
	
	public String subtext() {
		return "§7§o - " + s;
	}
}
