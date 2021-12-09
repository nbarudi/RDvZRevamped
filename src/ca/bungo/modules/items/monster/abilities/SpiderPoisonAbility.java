package ca.bungo.modules.items.monster.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class SpiderPoisonAbility extends CustomItem {

	public SpiderPoisonAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Spider Poison";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Spider Fangs");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED + "Right-Click a dwarf to bite and poison them!");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	}
	
	
	@EventHandler
	public void onRightClickEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(onCooldown(player, "SpiderPoison"))
			return;
		giveCooldown(player, "SpiderPoison", this.getConfigurableCooldown("SpiderPoison", 0.2));
		
		if(!(event.getRightClicked() instanceof Player))
			return;
		event.setCancelled(true);
		
		Player target = (Player)event.getRightClicked();
		
		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
		target.sendMessage(ChatColor.DARK_RED + "You've been bit by a Spiders Fang!");
		player.sendMessage(ChatColor.YELLOW + "You have bitten " + target.getName());
		return;
	}

}
