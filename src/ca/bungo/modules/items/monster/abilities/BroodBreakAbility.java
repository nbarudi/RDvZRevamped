package ca.bungo.modules.items.monster.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class BroodBreakAbility extends CustomItem {

	public BroodBreakAbility(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Brood Break";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Burrow");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED + "Right-Click a block to 'burrow' into it.");
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

		if(onCooldown(player, "BroodBreak"))
			return;
		
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			this.onRightclickItem(player);
			giveCooldown(player, "BroodBreak", 1);
			return;
		}

		giveCooldown(player, "BroodBreak", this.getConfigurableCooldown("BroodBurrow", 15));
		
		Block block = event.getClickedBlock();
		if(block == null || block.getType() == Material.AIR)
			return;
		
		if(block.getType().equals(Material.ENCHANTMENT_TABLE));
			//ShrineManager.breakShrine();
		
		if(block.getType().equals(Material.BEDROCK)) {
			player.sendMessage("§4Metas are fun... but I like to change things up!");
			player.sendMessage("§eBreaking Bedrock has been patched!");
			return;
		}
		
		block.setType(Material.WOOL);
		block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_CLOTH_PLACE, 2, 1);
		block.getLocation().getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ(), 50);
		
		
		

	}

}
