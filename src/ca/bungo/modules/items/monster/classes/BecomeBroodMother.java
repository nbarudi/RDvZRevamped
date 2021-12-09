package ca.bungo.modules.items.monster.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;
import ca.bungo.util.backend.player.PlayerData.MonsterClass;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public class BecomeBroodMother extends CustomItem {

	public BecomeBroodMother(RDvZ pl) {
		super(pl, Material.MONSTER_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) this.getItemMeta();
		meta.setSpawnedType(EntityType.SILVERFISH);
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Become Brood Mother");
		this.setItemMeta(meta);
		this.canDrop = false;
		this.name = "Become Brood-Mother";
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
		data.setMonsterClass(MonsterClass.BROODMOTHER);
		player.sendMessage(ChatColor.DARK_PURPLE + "You have become a Brood Mother!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);

		// Give Items:

		// Class Spesific

		// Disugise
		MobDisguise md = new MobDisguise(DisguiseType.SILVERFISH);
		md.setEntity(player);
		md.startDisguise();

		ItemStack sword = new ItemStack(Material.WOOD_SWORD);

		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 16);

		sword.getItemMeta().setUnbreakable(true);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

		// Adding to Inventory
		final PlayerInventory inv = player.getInventory();

		inv.addItem(sword);
		inv.addItem(ItemManager.findCustomItem("Brood Break"));
		inv.addItem(steak);

		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
			if (data.claimedClasses)
				player.getInventory().setItem(35, ItemManager.findCustomItem("Seppuku"));
		}, 20);
		
		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " monster");
	}

}
