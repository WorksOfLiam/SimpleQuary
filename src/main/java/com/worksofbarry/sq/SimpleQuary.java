package com.worksofbarry.sq;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.worksofbarry.sq.Listeners.BlockListener;
import com.worksofbarry.sq.Listeners.PlayerListener;

public class SimpleQuary extends JavaPlugin {
    private final BlockListener blockListener = new BlockListener();
    private final PlayerListener playerListener = new PlayerListener();
	
	@Override
	public void onEnable() {
		
		//Add seperate listeners here
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);
        
        NamespacedKey key = new NamespacedKey(this, getDescription().getName());
        ShapedRecipe disk = new ShapedRecipe(key, Quary.GetQuaryBlock());
        disk.shape(new String[]{"*A*","BBB","*C*"}).setIngredient('A', Material.REDSTONE).setIngredient('B', Material.OBSIDIAN).setIngredient('C', Material.BIRCH_FENCE); 
        Bukkit.getServer().addRecipe(disk);
        
		if (!new File(getDataFolder(), "config.yml").exists())
			saveDefaultConfig();
		
        LoadQuaries();
       
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Quary block : Quary.List.values()) {
					block.DoWork();
				}
			}
		}, 40, 40);
	}

	@Override
	public void onDisable() {
		SaveQuaries();
	}
	
	public void LoadQuaries() {
		ConfigurationSection currentConfig;
		if (getConfig().isSet("quaries")) {
			for(String key : getConfig().getConfigurationSection("quaries").getKeys(false)) {
				currentConfig = getConfig().getConfigurationSection("quaries." + key);
				Quary.List.put(key, new Quary(LocationFromString(key), currentConfig.getDouble("DrillY")));
			}
		}
	}
	
	public void SaveQuaries() {
		String location;
		ConfigurationSection currentConfig;
		
		getConfig().set("quaries", null);
		for (Quary quary : Quary.List.values()) {
			location = Quary.LocationString(quary.getBlockLocation());
			currentConfig = getConfig().createSection("quaries." + location);
			
			currentConfig.set("DrillY", quary.getDrillY());
		}
		
		saveConfig();
	}
	
	public Location LocationFromString(String loc) {
		String[] data = loc.split(",");
		return new Location(Bukkit.getWorld(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]));
	}
	
}

