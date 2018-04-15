package com.neo.headhunter.command.operator;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.mob.MobSettings;
import org.bukkit.command.CommandSender;

public class CmdReload {
	public static boolean run(CommandSender sender, HeadHunter plugin) {
		plugin.reloadConfig();
		Settings.load(plugin);
		MobSettings.load(plugin);
		return true;
	}
}
