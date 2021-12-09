package ca.bungo.modules.items.monster.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;
import ca.bungo.util.backend.player.PlayerData.MonsterClass;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

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
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!verifyItem(item, this))
			return;
		if (onCooldown(player, "BecomeMonster"))
			return;
		giveCooldown(player, "BecomeMonster", 1);

		event.setCancelled(true);
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			this.onRightclickItem(player);
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

		// Give Items:

		// Class Spesific
		MobDisguise md = new MobDisguise(DisguiseType.CAVE_SPIDER);
		md.setEntity(player);
		md.startDisguise();

		ItemStack sword = new ItemStack(Material.STONE_SWORD);

		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 16);

		sword.getItemMeta().setUnbreakable(true);

		// Adding to Inventory
		final PlayerInventory inv = player.getInventory();

		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000000, 3));

		inv.addItem(sword);
		inv.addItem(ItemManager.findCustomItem("Spider Poison"));
		inv.addItem(steak);

		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
			if (data.claimedClasses)
				player.getInventory().setItem(35, ItemManager.findCustomItem("Seppuku"));
		}, 20);

		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " monster");
	}

}
