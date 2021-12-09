package ca.bungo.modules.items.monster.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.TeleportUtil;
import ca.bungo.util.backend.raycast.RayTrace;

public class EndermanBlinkAbility extends CustomItem {

	public EndermanBlinkAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Blink";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.BLACK + "Blink");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Right-Click to Blink forward!");
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
		
		event.setCancelled(true);
		
		if(onCooldown(player, "EndermanBlink"))
			return;
		giveCooldown(player, "CreeperBoom", this.getConfigurableCooldown("EndermanBlink", 15));
		
		int range = (Integer)this.getConfigSetting("EndermanBlinkDistance", 20, Integer.class);
		
		
		Block block = RayTrace.getTargetBlock(player, range);
		if(block == null) {
			player.sendMessage(ChatColor.RED + "You cannot blink there!");
			return;
		}
		
		Location loc = block.getLocation().add(0,2,0);
		
		TeleportUtil.teleport(player, loc);
		player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2, 1);
		player.getLocation().getWorld().playSound(loc, Sound.ENTITY_ENDERMEN_TELEPORT, 2, 1);
		player.getLocation().getWorld().spawnParticle(Particle.CRIT_MAGIC, loc.getX(), loc.getY(), loc.getZ(), 50);
		player.sendMessage(ChatColor.BLACK + "Blink!");
		

	}

}
