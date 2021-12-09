package ca.bungo.modules.items.dwarf.potions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.player.PlayerData;

public class StrengthPotion extends CustomItem {

	public StrengthPotion(RDvZ pl, Material material) {
		super(pl, material);
		PotionMeta meta = (PotionMeta)this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Strength Potion");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.AQUA + "- Left-Click to give yourself a strength effect");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setColor(Color.PURPLE);
		this.setItemMeta(meta);
		this.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
		this.name = "Strength Potion";
		this.canDrop = true;
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		if(onCooldown(player, "StrengthPotion")) {
			event.setCancelled(true);
			return;
		}
		
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			onRightclickItem(player);
			event.setCancelled(true);
			return;
		}
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		if(!data.useMana(350)) {
			player.sendMessage(ChatManager.formatColor("&cYou do not have enough mana to use this potion! &e" + 350 + " mana required!"));
			event.setCancelled(true);
			return;
		}
		
		giveCooldown(player, "StrengthPotion", this.getConfigurableCooldown("StrengthPotion", 0.4));
		Location loc = player.getLocation();
		loc.getWorld().spawnParticle(Particle.REDSTONE, loc.getX(), loc.getY() - 1, loc.getZ(), 100, 1, 1, 1);
		loc.getWorld().playSound(loc, Sound.ENTITY_SPLASH_POTION_BREAK, 1, 1);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 0));
		event.setCancelled(true);
		return;
	}

}
