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

public class SuicideAbility extends CustomItem {

	public SuicideAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Seppuku";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Seppuku");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Die and reset your class!");
		lore.add(ChatColor.YELLOW + "Note: Hosts can disable this feature should players abuse it");
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
		
		if(onCooldown(player, "Seppuku"))
			return;
		giveCooldown(player, "Seppuku", 1);
		
		
		if((Boolean)this.getConfigSetting("CanSeppuku", true, Boolean.class)){
			player.setHealth(0);
			player.sendMessage(ChatColor.DARK_RED + "You have committed Seppuku");
			return;
		}else {
			player.sendMessage(ChatColor.DARK_RED + "Sorry! The host has disabled this feature for the game!");
			return;
		}
		
		
		

	}

}
