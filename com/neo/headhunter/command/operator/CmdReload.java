package com.neo.headhunter.command.operator;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.message.Control;
import com.neo.headhunter.util.mob.MobSettings;
import org.bukkit.command.CommandSender;

public class CmdReload {
	public static boolean run(CommandSender sender, HeadHunter plugin) {
		plugin.reload();
		Settings.load(plugin);
		MobSettings.load(plugin);
		sender.sendMessage(Control.RELOADED.info(plugin.getDescription().getVersion()));
		return true;
	}
}
