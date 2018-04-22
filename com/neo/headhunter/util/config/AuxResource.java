package com.neo.headhunter.util.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class AuxResource {
	private final Plugin plugin;
	private final String fileName;
	private File file;
	private FileConfiguration config;
	
	public AuxResource(Plugin plugin, String fileName) {
		if(plugin == null)
			throw new IllegalArgumentException("plugin cannot be null");
		if(plugin.getDataFolder() == null)
			throw new IllegalStateException("plugin data directory has not been created yet");
		
		this.plugin = plugin;
		this.fileName = fileName;
		this.file = new File(plugin.getDataFolder() + File.separator + fileName);
		
		reloadConfig();
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	@SuppressWarnings("deprecation")
	public void reloadConfig() {
		try {
			boolean overwrite = false;
			if (!file.exists()) {
				if(!file.createNewFile())
					plugin.getLogger().log(Level.SEVERE, "auxiliary " + fileName + " could not be created");
				else
					overwrite = true;
			}
			
			InputStream resource = plugin.getResource(fileName);
			FileConfiguration defaults = null;
			if(resource != null) {
				//if the plugin JAR contains a resource by the same name
				plugin.saveResource(fileName, overwrite);
				defaults = YamlConfiguration.loadConfiguration(resource);
			}
			
			config = YamlConfiguration.loadConfiguration(file);
			if(defaults != null)
				config.setDefaults(defaults);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		if(file == null || config == null)
			return;
		try {
			config.save(file);
		} catch(IOException e) {
			plugin.getLogger().log(Level.SEVERE, "could not save " + fileName + " config");
		}
	}
	
	public void saveDefaultConfig() {
		if(!file.exists())
			plugin.saveResource(fileName, true);
	}
	
	public void resetConfig() {
		if(!file.delete())
			plugin.getLogger().log(Level.SEVERE, "could not delete " + fileName + " config");
		reloadConfig();
	}
}
