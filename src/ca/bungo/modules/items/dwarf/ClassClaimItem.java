package ca.bungo.modules.items.dwarf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
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
		if(!verifyItem(item, this))
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
		player.getInventory().remove(item);
		
		
		Random rnd = new Random();
		
		//25% chance of each class and 100% Builder Class
		ItemStack builder = ItemManager.findCustomItem("Become Builder");
		ItemStack alchem = ItemManager.findCustomItem("Become Alchemist");
		ItemStack smith = ItemManager.findCustomItem("Become Blacksmith");
		ItemStack tailor = ItemManager.findCustomItem("Become Tailor");
		ItemStack baker = ItemManager.findCustomItem("Become Baker");
		
		giveCooldown(player, "BecomeBuilder", 1);
		giveCooldown(player, "BecomeAlchemist", 1);
		giveCooldown(player, "BecomeBlacksmith", 1);
		giveCooldown(player, "BecomeTailor", 1);
		giveCooldown(player, "BecomeBaker", 1);
		
		player.getInventory().addItem(builder);
		
		if(rnd.nextInt() > 25) {
			player.getInventory().addItem(alchem);
		}
		if(rnd.nextInt() > 25) {
			player.getInventory().addItem(smith);
		}
		if(rnd.nextInt() > 25) {
			player.getInventory().addItem(tailor);
		}
		if(rnd.nextInt() > 25) {
			player.getInventory().addItem(baker);
		}
		player.sendMessage(ChatColor.AQUA + "Claimed classes!");
		
		event.setCancelled(true);
	}

}
