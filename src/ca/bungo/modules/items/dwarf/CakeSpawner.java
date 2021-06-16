package ca.bungo.modules.items.dwarf;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class CakeSpawner extends CustomItem{

	public CakeSpawner(RDvZ pl, Material material) {
		super(pl, material);
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Cake Summoner");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.AQUA + "- Right-Click to summon a cake!");
		meta.setLore(lore);
		setItemMeta(meta);
		this.canDrop = true;
		this.name = "Cake Spawner";
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		if(onCooldown(player, "CakeSummon")) {
			event.setCancelled(true);
			return;
		}
		
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			onRightclickItem(player);
			event.setCancelled(true);
			return;
		}
		
		giveCooldown(player, "CakeSummon", 1);
		
		Location location = event.getClickedBlock().getLocation();
		location.setY(location.getY() + 1);
		
		if(location.getBlock().getType() != Material.AIR) {
			event.setCancelled(true);
			return;
		}
		
		location.getBlock().setType(Material.CAKE_BLOCK);
		player.sendMessage(ChatColor.GREEN + "Summoned Cake!");
	}
	
	

}
