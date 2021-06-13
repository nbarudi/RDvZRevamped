package ca.bungo.util.backend.player;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import ca.bungo.util.backend.player.PlayerData.DwarfClass;

public class DeathData {
	
	private ItemStack[] items;
	private int mana;
	private DwarfClass dclass;
	private Location deathLoc;
	
	
	public ItemStack[] getDeathInv() {
		return items;
	}
	public void setDeathInv(ItemStack[] items) {
		this.items = items;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public DwarfClass getDclass() {
		return dclass;
	}
	public void setDclass(DwarfClass dclass) {
		this.dclass = dclass;
	}
	public Location getDeathLoc() {
		return deathLoc;
	}
	public void setDeathLoc(Location deathLoc) {
		this.deathLoc = deathLoc;
	}

}
