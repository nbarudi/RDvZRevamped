package ca.bungo.modules.items;

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
import ca.bungo.util.backend.player.PlayerData;

public class ClassClaimItem extends CustomItem {

	public ClassClaimItem(RDvZ pl, Material material) {
		super(pl, material);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Claim Classes");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Click to be given your random dwarf classes!");
		meta.setLore(lore);
		setItemMeta(meta);
		this.canDrop = true;
		this.name = "Dwarf Claim Classes";
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		if((Integer)data.getPunishments().get("currentRoundBans") > 0) {
			player.sendMessage(ChatColor.RED + "You have been round banned! You will not be able to play this round of RDvZ");
			player.getInventory().remove(item);
			event.setCancelled(true);
			return;
		}
		if(data.claimedClasses)
			return;
		
		data.isDwarf = true;
		data.claimedClasses = true;
		event.setCancelled(true);
	}

}
