package ca.bungo.modules.items.monster.abilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class WolfJumpAbility extends CustomItem {

	public WolfJumpAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.canDrop = false;
		this.name = "Leap";
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Leap");
		this.setItemMeta(meta);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		
		if(onCooldown(player, "WolfLeap"))
			return;
		giveCooldown(player, "WolfLeap", this.getConfigurableCooldown("WolfLeapAbility", 15));
		
		
		//Run the Wolf Ability
		Vector dir = player.getLocation().getDirection();
		dir.setX(dir.getX() * 3);
		dir.setY(dir.getY() * 2);
		dir.setZ(dir.getZ() * 3);
		
		player.setVelocity(dir);
		
		player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 5, 1);

	}

}
