package ca.bungo.modules;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.cooldown.Cooldown;
import ca.bungo.util.backend.player.PlayerData;

public abstract class CustomItem extends ItemStack implements Listener {
	
	public boolean canDrop = false;
	public RDvZ pl;
	public String name = "Example";
	
	public CustomItem(RDvZ pl, Material material) {
		super(material);
		this.pl = pl;
	}
	
	public CustomItem(RDvZ pl, Material material, short data) {
		super(material, 1, data);
		this.pl = pl;
	}
	
	@EventHandler
	public abstract void onInteract(PlayerInteractEvent event);
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		ItemStack dropped = event.getItemDrop().getItemStack();
		if(!verifyItem(this, dropped))
			return;
		event.setCancelled(true);
	}
	
	public boolean onCooldown(Player player, String name) {
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		Cooldown cd = data.getCooldown(name);
		if(cd == null)
			return false;
		if(cd.getRemainingTime() > 0) {
			player.sendMessage(ChatColor.YELLOW + "" + cd.getRemainingTime() + ChatColor.BLUE + " seconds remaining until you can use this again...");
			return true;
		}
		data.removeCooldown(name);
		return false;
	}
	
	public void giveCooldown(Player player, String name, double seconds) {
		if(!onCooldown(player, name)) {
			Cooldown cd = pl.cm.giveCooldown(player, seconds*20);
			pl.currentRound.getPlayerData(player.getName()).addCooldown(name, cd);
		}
	}
	
	public void onRightclickItem(Player player) {
		player.sendMessage(ChatColor.BLUE + "You are currently using: " + ChatColor.YELLOW + this.name);
	}
	
	public boolean verifyItem(ItemStack item, ItemStack toComp) {
		if(item == null || item.getData() == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
			return false;
		return (item.getType().equals(toComp.getType()) && item.getItemMeta().getDisplayName().equals(toComp.getItemMeta().getDisplayName()));
	}
	
	public double getConfigurableCooldown(String name, double def) {
		pl.fm.reloadConfig("config.yml");
		YamlConfiguration cfg = pl.fm.getConfig("config.yml").get();
		
		ConfigurationSection mSec = cfg.getConfigurationSection("Settings.Cooldowns");
		if(mSec == null)
			mSec = cfg.createSection("Settings.Cooldowns");
		
		if(mSec.getDouble(name) == 0.0) {
			mSec.set(name, def);
			pl.fm.saveConfig("config.yml");
			return def;
		}else {
			return mSec.getDouble(name);
		}
	}
	
	/**
	 * Get a configuration setting casted to the object supplied
	 * @implNote This is only really intended to be used with Strings / Booleans / Other Base Data Types since other class types like Locations are not formatted correctly to be used here.
	 * @parma name -> String value stating the Config Path to your setting
	 * @param def -> Default value if the path doesn't already exist
	 * @param type -> Class type of the setting you are obtaining
	 * @return Object obtained from config
	 * */
	public Object getConfigSetting(String name, Object def, Class<?> type) {
		pl.fm.reloadConfig("config.yml");
		YamlConfiguration cfg = pl.fm.getConfig("config.yml").get();
		ConfigurationSection mSec = cfg.getConfigurationSection("Settings");
		if(mSec == null)
			mSec = cfg.createSection("Settings");
		
		if(mSec.get(name) == null) {
			mSec.set(name, type.cast(def));
			pl.fm.saveConfig("config.yml");
			return type.cast(def);
		}else {
			return mSec.get(name);
		}
	}

}
