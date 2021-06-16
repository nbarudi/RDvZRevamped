package ca.bungo.modules.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.backend.cooldown.Cooldown;

public class ExampleItem extends CustomItem {
	
	public ExampleItem(RDvZ pl) {
		super(pl, Material.BARRIER);
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bExample Item") + "");
		this.setItemMeta(im);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		// TODO Auto-generated method stub
		if(event.getItem() == null || event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null)
			return;
		if(!event.getItem().getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
			return;
		if(!onCooldown(event.getPlayer(), "Example")) {
			Cooldown cd = pl.cm.giveCooldown(event.getPlayer(), 200);
			pl.currentRound.getPlayerData(event.getPlayer().getName()).addCooldown("Example", cd);
		}else
			return;
	}

	

}
