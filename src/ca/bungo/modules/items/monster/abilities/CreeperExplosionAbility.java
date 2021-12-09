package ca.bungo.modules.items.monster.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class CreeperExplosionAbility extends CustomItem {

	public CreeperExplosionAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Creeper Explosion";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Explosion");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED + "Left click to explode doing massive damage to walls and dwarves");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(onCooldown(player, "CreeperBoom"))
			return;
		giveCooldown(player, "CreeperBoom", 1);
		
		player.getWorld().createExplosion(player.getLocation(), 4f);
		player.sendMessage(ChatColor.DARK_RED + "You have exploded!");
		player.setHealth(0);

	}

}
