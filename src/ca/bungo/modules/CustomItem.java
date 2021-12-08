package ca.bungo.modules;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
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
	
	@EventHandler
	public abstract void onInteract(PlayerInteractEvent event);
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		if(item == null)
			return;
		if(item.getCustomName() == null)
			return;
		if(item.getCustomName().equals(this.getItemMeta().getDisplayName()))
			event.setCancelled(canDrop);
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
	
	public boolean verifyItem(ItemStack item, String name) {
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
			return false;
		return item.getItemMeta().getDisplayName().equals(name);
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

}
