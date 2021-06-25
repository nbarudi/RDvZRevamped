package ca.bungo.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.bungo.cmds.WarpCommand;
import ca.bungo.cmds.admin.AdminCommand;
import ca.bungo.events.EnchantingEventStuff;
import ca.bungo.events.PlayerJoinLeave;
import ca.bungo.modules.CustomItem;
import ca.bungo.modules.events.monster.AntiPickup;
import ca.bungo.modules.items.dwarf.CakeSpawner;
import ca.bungo.modules.items.dwarf.ClassClaimItem;
import ca.bungo.modules.items.dwarf.Classbook;
import ca.bungo.modules.items.dwarf.classes.AlchemistClass;
import ca.bungo.modules.items.dwarf.classes.BakerClass;
import ca.bungo.modules.items.dwarf.classes.BlacksmithClass;
import ca.bungo.modules.items.dwarf.classes.BuilderClass;
import ca.bungo.modules.items.dwarf.classes.TailorClass;
import ca.bungo.modules.items.dwarf.potions.FirePotion;
import ca.bungo.modules.items.dwarf.potions.HealingPotion;
import ca.bungo.modules.items.dwarf.potions.SpeedPotion;
import ca.bungo.modules.items.dwarf.potions.StrengthPotion;
import ca.bungo.modules.items.monster.MonsterclassClaim;
import ca.bungo.util.CommandUtils;
import ca.bungo.util.FileManager;
import ca.bungo.util.backend.cooldown.CooldownManager;
import ca.bungo.util.backend.player.PlayerManager;
import ca.bungo.util.backend.round.RoundData;
import ca.bungo.util.backend.warps.WarpManager;

public class RDvZ extends JavaPlugin {

	public FileManager fm;
    public CooldownManager cm;
    public CommandUtils cu;
    public WarpManager wm;
    
    public static List<CustomItem> customItems = new ArrayList<>();
    public ArrayList<EnchantingInventory> inventories;
    
    public RoundData currentRound;

    @Override
    public void onEnable(){
    	
    	inventories = new ArrayList<EnchantingInventory>();
    	
    	registerItems();
        registerConfigs();
        registerCommands();
        
        currentRound = new RoundData(this);
        
        registerEvents();
        
        if(Bukkit.getWorld(currentRound.worldName) == null) {
        	WorldCreator wc = new WorldCreator(currentRound.worldName);
        	wc.createWorld();
        }
        
		if(Bukkit.getWorld("RDvZ_Lobby") == null) {
			WorldCreator wc = new WorldCreator("RDvZ_Lobby");
			wc.type(WorldType.FLAT);
			wc.generateStructures(false);
			wc.createWorld().setDifficulty(Difficulty.PEACEFUL);;
		}

        //Bukkit.getWorld(currentRound.worldName).setAutoSave(false);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "RDvZ has disabled AutoSaving! If you need to save the world do /save-all!");
        
        cm = new CooldownManager(this);
        cu = new CommandUtils(this);
        wm = new WarpManager(this);
    }

    @Override
    public void onDisable(){
    	wm.saveWarps();
    }

    
    private void registerItems() {
    	//customItems.add(new ExampleItem(this));
    	
    	//Dwarf
    	customItems.add(new ClassClaimItem(this, Material.MAGMA_CREAM));
    	customItems.add(new BuilderClass(this, Material.GOLD_RECORD));
    	customItems.add(new TailorClass(this, Material.GREEN_RECORD));
    	customItems.add(new BlacksmithClass(this, Material.RECORD_4));
    	customItems.add(new BakerClass(this, Material.RECORD_11));
    	customItems.add(new AlchemistClass(this, Material.RECORD_12));
    	customItems.add(new Classbook(this, Material.BOOK));
    	customItems.add(new HealingPotion(this, Material.POTION));
    	customItems.add(new SpeedPotion(this, Material.POTION));
    	customItems.add(new FirePotion(this, Material.POTION));
    	customItems.add(new StrengthPotion(this, Material.POTION));
    	customItems.add(new CakeSpawner(this, Material.EMERALD));
    	
    	//Monster
    	customItems.add(new MonsterclassClaim(this, Material.GOLD_NUGGET));
    	
    	//Dragon
    }

    private void registerCommands(){
    	getCommand("admin").setExecutor(new AdminCommand(this));
    	getCommand("warp").setExecutor(new WarpCommand(this));
    }

    private void registerEvents(){
    	PluginManager pm = Bukkit.getPluginManager();
    	for(CustomItem item : customItems) {
    		pm.registerEvents(item, this);
    		System.out.println("Registered: " + item.name);
    	}
    	pm.registerEvents(new PlayerManager(this), this);
    	pm.registerEvents(new PlayerJoinLeave(this), this);
    	pm.registerEvents(new EnchantingEventStuff(this), this);
    	pm.registerEvents(new AntiPickup(this), this);
    }

    private void registerConfigs(){
    	fm = new FileManager(this);
        fm.getConfig("spawns.yml").saveDefaultConfig();
        fm.getConfig("config.yml").saveDefaultConfig();
        fm.getConfig("punishments.yml").saveDefaultConfig();
    }

}
