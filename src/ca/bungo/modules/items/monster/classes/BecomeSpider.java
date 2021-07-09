package ca.bungo.modules.items.monster.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;
import ca.bungo.util.backend.player.PlayerData.MonsterClass;

public class BecomeSpider extends CustomItem {

	public BecomeSpider(RDvZ pl) {
		super(pl, Material.MONSTER_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) this.getItemMeta();
		meta.setSpawnedType(EntityType.CAVE_SPIDER);
		meta.setDisplayName(ChatColor.RED + "Become Spider");
		this.setItemMeta(meta);
		this.canDrop = false;
		this.name = "Become Spider";
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		if(onCooldown(player, "BecomeMonster"))
			return;
		giveCooldown(player, "BecomeMonster", 1);
		
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			this.onRightclickItem(player);
			event.setCancelled(true);
			return;
		}
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		data.setDwarfClass(DwarfClass.NONE);
		data.isDwarf = false;
		data.setMonsterClass(MonsterClass.SPIDER);
		player.sendMessage(ChatColor.RED + "You have become a Spider!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
	}
	
	

}
