package ca.bungo.modules.items.monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;

public class MonsterclassClaim extends CustomItem {

	public MonsterclassClaim(RDvZ pl, Material material) {
		super(pl, material);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Claim Classes");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Click to be given your random monster classes!");
		meta.setLore(lore);
		setItemMeta(meta);
		this.canDrop = true;
		this.name = "Monster Claim Classes";
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

		if(data.claimedClasses || !pl.currentRound.isGameStarted())
			return;
		
		if(!pl.currentRound.isMonstersReleased()) {
			this.onRightclickItem(player);
			return;
		}
		
		if (onCooldown(player, "BecomeMonster"))
			return;
		giveCooldown(player, "BecomeMonster", 1);
		
		data.isDwarf = false;
		
		data.claimedClasses = true;
		player.getInventory().remove(item);
		
		player.sendMessage(ChatColor.AQUA + "Claimed classes!");
		
		PlayerInventory inv = player.getInventory();
		
		inv.addItem(ItemManager.findCustomItem("Become Zombie"));
		Random rnd = new Random();
		if(rnd.nextInt(100) > 35)
			inv.addItem(ItemManager.findCustomItem("Become Skeleton"));
		if(rnd.nextInt(100) > 65)
			inv.addItem(ItemManager.findCustomItem("Become Creeper"));
		if(rnd.nextInt(100) > 75)
			inv.addItem(ItemManager.findCustomItem("Become Spider"));
		if(rnd.nextInt(100) > 80)
			inv.addItem(ItemManager.findCustomItem("Become Wolf"));
		if(rnd.nextInt(100) > 90)
			inv.addItem(ItemManager.findCustomItem("Become IronGolem"));
		if(rnd.nextInt(100) > 95)
			inv.addItem(ItemManager.findCustomItem("Become Brood-Mother"));
		if(rnd.nextInt(100) > 97)
			inv.addItem(ItemManager.findCustomItem("Become Enderman"));
		
		event.setCancelled(true);
	}

}
