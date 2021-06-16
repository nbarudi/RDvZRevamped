package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class ItemsArgument extends AdminArgument{
	
	private Inventory inv;

	public ItemsArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Open an inventory containing all availible items";
		this.requiresPlayer = true;
		
		inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Item List");
		for(ItemStack item : RDvZ.customItems) {
			inv.addItem(item);
		}
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		player.openInventory(inv);
		player.sendMessage(ChatColor.GREEN + "Opened item list!");
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(event.getInventory() == null)
			return;
		if(event.getInventory().getName() == null)
			return;
		if(event.getInventory().getName() == inv.getName()) {
			event.setCancelled(true);
			if(event.getCurrentItem() != null) {
				Player player = (Player) event.getWhoClicked();
				player.getInventory().addItem(event.getCurrentItem());
				player.sendMessage(ChatColor.BLUE + "Given item: " + event.getCurrentItem().getItemMeta().getDisplayName());
				return;
			}
		}
		return;
	}

}
