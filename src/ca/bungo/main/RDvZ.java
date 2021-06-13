package ca.bungo.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.bungo.cmds.admin.AdminCommand;
import ca.bungo.events.PlayerJoinLeave;
import ca.bungo.modules.CustomItem;
import ca.bungo.modules.items.ExampleItem;
import ca.bungo.util.FileManager;
import ca.bungo.util.backend.cooldown.CooldownManager;
import ca.bungo.util.backend.player.PlayerManager;
import ca.bungo.util.backend.round.RoundData;

public class RDvZ extends JavaPlugin {

    public FileManager fm;
    public CooldownManager cm;
    
    public static List<CustomItem> customItems = new ArrayList<>();
    
    public RoundData currentRound;

    @Override
    public void onEnable(){
    	
    	
    	registerItems();
        registerConfigs();
        registerCommands();
        
        currentRound = new RoundData(this);
        
        registerEvents();
        
        if(Bukkit.getWorld(currentRound.worldName) == null) {
        	WorldCreator wc = new WorldCreator(currentRound.worldName);
        	wc.copy(Bukkit.getWorld("world"));
        	wc.createWorld();
        }
        
		if(Bukkit.getWorld("RDvZ_Lobby") == null) {
			WorldCreator wc = new WorldCreator("RDvZ_Lobby");
			wc.type(WorldType.FLAT);
			wc.generateStructures(false);
			wc.createWorld().setDifficulty(Difficulty.PEACEFUL);;
		}

        Bukkit.getWorld(currentRound.worldName).setAutoSave(false);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "RDvZ has disabled AutoSaving! If you need to save the world do /save-all!");
        
        cm = new CooldownManager(this);
    }

    @Override
    public void onDisable(){

    }

    
    private void registerItems() {
    	customItems.add(new ExampleItem(this));
    }

    private void registerCommands(){
    	getCommand("admin").setExecutor(new AdminCommand(this));
    }

    private void registerEvents(){
    	PluginManager pm = Bukkit.getPluginManager();
    	for(CustomItem item : customItems) {
    		pm.registerEvents(item, this);
    	}
    	pm.registerEvents(new PlayerManager(this), this);
    	pm.registerEvents(new PlayerJoinLeave(this), this);
    }

    private void registerConfigs(){
    	fm = new FileManager(this);
        fm.getConfig("spawns.yml").saveDefaultConfig();
        fm.getConfig("config.yml").saveDefaultConfig();
        fm.getConfig("punishments.yml").saveDefaultConfig();
    }

}
