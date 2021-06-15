package ca.bungo.modules;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	
	public void onRightclickItem(Player player) {
		player.sendMessage(ChatColor.BLUE + "You are currently using: " + ChatColor.YELLOW + this.name);
	}
	
	public boolean verifyItem(ItemStack item, String name) {
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
			return false;
		return item.getItemMeta().getDisplayName().equals(name);
	}

}
