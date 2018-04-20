package com.neo.headhunter;

import com.neo.headhunter.command.MainExecutor;
import com.neo.headhunter.database.HHDB;
import com.neo.headhunter.factory.DropFactory;
import com.neo.headhunter.factory.HeadFactory;
import com.neo.headhunter.factory.RateFactory;
import com.neo.headhunter.listener.*;
import com.neo.headhunter.listener.support.ListenerMinigames;
import com.neo.headhunter.listener.support.ListenerMobStacker;
import com.neo.headhunter.mgmt.CooldownManager;
import com.neo.headhunter.mgmt.SignManager;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Accessor;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.mob.MobLibrary;
import com.neo.headhunter.util.mob.MobSettings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class HeadHunter extends JavaPlugin {
	private static final String VERSION = "2.0.0";
	
	private Economy economy;
	
	private HHDB hhdb;
	
	private ListenerMinigames listenerMinigames;
	private ListenerCombustion listenerCombustion;
	
	private CooldownManager cooldownManager;
	private MobLibrary mobLibrary;
	private RateFactory rateFactory;
	private SignManager signManager;
	private DropFactory dropFactory;
	private HeadFactory headFactory;
	
	@Override
    public void onEnable() {
		prepareFiles();
		
	    //Hard dependencies
	    try {
		    economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
	    } catch (Exception e) {
		    Bukkit.getConsoleSender().sendMessage("§cHeadHunter could not find a Vault-economy plugin! Disabling...");
		    setEnabled(false);
		    return;
	    }
	    
	    //Create database
		this.hhdb = new HHDB(this);
	
	    //Force default configuration files
	    access(Utils.MDB).forceDefaultConfig();
	
	    //Load settings
	    Settings.load(this);
	    MobSettings.load(this);
	    Message.load(this);
	    Message.save(this);
	    
	    this.cooldownManager = new CooldownManager();
	    this.mobLibrary = new MobLibrary(this);
	    this.rateFactory = new RateFactory(this);
	    this.signManager = new SignManager(this);
	    this.dropFactory = new DropFactory(this);
		this.headFactory = new HeadFactory(this);
	
		registerListeners();
		
	    //Register commands
	    MainExecutor mainExecutor = new MainExecutor(this);
	    getCommand("hunter").setExecutor(mainExecutor);
	    getCommand("sellhead").setExecutor(mainExecutor);
	    getCommand("bounty").setExecutor(mainExecutor);
	    
	    //Start timers
		this.cooldownManager.runTaskTimer(this, 0L, 20L);
		this.signManager.runTaskTimer(this, 0L, 40L);
        
        Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§a" + getTag() + " has been Disabled!");
    }
    
    private void prepareFiles() {
		try {
			if (getDataFolder().mkdir())
				getLogger().log(Level.INFO, "New HeadHunter plugin directory successfully created");
			
			File configFile = new File(getDataFolder() + File.separator + Utils.CFG);
			if (!configFile.exists())
				saveResource(Utils.CFG, true);
			else {
				FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
				String currentVersion = null;
				if(config.contains(Utils.VERSION_PATH))
					currentVersion = config.getString(Utils.VERSION_PATH);
				if(currentVersion == null || !currentVersion.equals(VERSION)) {
					//current version is incorrect
					Map<String, Object> oldValues = new HashMap<>();
					for(Map.Entry<String, Object> entry : config.getValues(true).entrySet()) {
						if(!(entry.getValue() instanceof MemorySection))
							oldValues.put(entry.getKey(), entry.getValue());
					}
					saveResource(Utils.CFG, true);
					config = YamlConfiguration.loadConfiguration(configFile);
					for(String key : config.getKeys(true)) {
						if(oldValues.containsKey(key))
							config.set(key, oldValues.get(key));
					}
					config.set(Utils.VERSION_PATH, VERSION);
					saveConfig();
				}
				//ignore config file with correct version
			}
			
			//always replace
			saveResource(Utils.MDB, true);
			
			//create empty 'mobhunter.yml' file
			File mobFile = new File(getDataFolder() + File.separator + Utils.MOB);
			if(!mobFile.exists()) {
				if(mobFile.createNewFile())
					getLogger().log(Level.INFO, "New HeadHunter file " + Utils.MOB + " successfully created");
				else
					getLogger().log(Level.SEVERE, "New HeadHunter file " + Utils.MOB + " could not be created");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    private void registerListeners() {
		Listener[] toRegister = {
				this.listenerMinigames = new ListenerMinigames(),
				new ListenerMobStacker(this),
				new ListenerHead(this),
				this.listenerCombustion = new ListenerCombustion(),
				new ListenerHelper(this),
				new ListenerDeath(this),
				new ListenerSign(this)
		};
		for(Listener l : toRegister)
			Bukkit.getPluginManager().registerEvents(l, this);
    }
	
	public Economy getEconomy() {
		return economy;
	}
	
	public HHDB getHHDB() {
		return hhdb;
	}
	
	public ListenerMinigames getListenerMinigames() {
		return listenerMinigames;
	}
	
	public ListenerCombustion getListenerCombustion() {
		return listenerCombustion;
	}
	
	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}
	
	public MobLibrary getMobLibrary() {
		return mobLibrary;
	}
	
	public RateFactory getRateFactory() {
		return rateFactory;
	}
	
	public SignManager getSignManager() {
		return signManager;
	}
	
	public DropFactory getDropFactory() {
		return dropFactory;
	}
	
	public HeadFactory getHeadFactory() {
		return headFactory;
	}
	
	public Accessor access(String fileName) {
        return new Accessor(this, fileName);
    }
    
    private String getTag() {
        return getDescription().getName() + " v" + getDescription().getVersion();
    }
}
