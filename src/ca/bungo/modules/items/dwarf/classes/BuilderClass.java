package ca.bungo.modules.items.dwarf.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;

public class BuilderClass extends CustomItem {

	public BuilderClass(RDvZ pl, Material material) {
		super(pl, material);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Become Builder");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Click to become a dwarf!");
		meta.setLore(lore);
		setItemMeta(meta);
		this.canDrop = false;
		this.name = "Become Builder";
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		if(onCooldown(player, "BecomeBuilder")) {
			event.setCancelled(true);
			return;
		}
		giveCooldown(player, "BecomeBuilder", 1);
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			this.onRightclickItem(player);
			event.setCancelled(true);
			return;
		}
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		data.setDwarfClass(DwarfClass.BUILDER);
		player.sendMessage(ChatColor.BLUE + "You have become a Builder!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		//To-Do: Give Items, Teleport to dwarf spawn.
		ItemStack book = ItemManager.findCustomItem("Run Class");

		//Class Spesific
		player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS));
		player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.SMOOTH_BRICK, 128));
				
		//Universal
		player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
		player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
		player.getInventory().addItem(new ItemStack(Material.IRON_SPADE));
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 5));
		
		//Special Items
		player.getInventory().addItem(book);
		
		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " dwarf");
	}

}
