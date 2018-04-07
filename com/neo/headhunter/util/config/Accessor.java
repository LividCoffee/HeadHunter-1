package com.neo.headhunter.util.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public final class Accessor {
    private String fileName;
    private JavaPlugin plugin;
    private File configFile;
    private FileConfiguration configuration;
    
    public Accessor(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {
            throw new IllegalStateException();
        }
        this.configFile = new File(dataFolder + File.separator + this.fileName);
        try {
            if (!configFile.exists()) {
                if (!configFile.createNewFile())
                    System.out.println("Error occurred while accessing " + fileName + "!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void reloadConfig() {
        try {
            this.configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.configFile), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(configFile);
            this.configuration.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getConfig() {
        if (this.configuration == null) {
            reloadConfig();
        }
        return this.configuration;
    }
    
    public void saveConfig() {
        if ((this.configuration == null) || (this.configFile == null)) {
            return;
        }
        try {
            getConfig().save(this.configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not saveRecords config to " + this.configFile, ex);
        }
    }
    
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }
    
    public void forceDefaultConfig() {
        this.plugin.saveResource(this.fileName, true);
    }
    
    public void resetConfig() {
        for(String key : getConfig().getKeys(false))
            getConfig().set(key, null);
        saveConfig();
    }
    
    public void deleteFile() {
        if(this.configFile != null && this.configFile.exists())
            if(!this.configFile.delete())
                System.out.println("Error occurred while deleting " + this.fileName + "!");
    }
}
