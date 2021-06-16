package ca.bungo.util.backend.warps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import ca.bungo.main.RDvZ;
import ca.bungo.util.FileManager.Config;

public class WarpManager {
	
	RDvZ pl;
	
	private ArrayList<Warp> warps;
	
	public WarpManager(RDvZ pl) {
		this.pl = pl;
		warps = new ArrayList<Warp>();
		
		initWarps();
	}
	
	public boolean warpExists(String name) {
		for(Warp warp : warps) {
			if(warp.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public Warp getWarp(String name) {
		Warp warp = null;
		if(!warpExists(name))
			return null;
		for(Warp w : warps) {
			if(w.getName().equalsIgnoreCase(name))
				warp = w;
		}
		return warp;
	}
	
	public void addWarp(Warp warp) {
		this.warps.add(warp);
	}
	
	public void removeWarp(String name) {
		if(!warpExists(name))
			return;
		int index = 0;
		for(int i = 0; i < warps.size(); i++) {
			if(warps.get(i).getName().equalsIgnoreCase(name)) {
				index = i;
				break;
			}
		}
		warps.remove(index);
	}
	
	private void initWarps() {
		Config cfg = pl.fm.getConfig("spawns.yml");
		
		if(cfg.get().getConfigurationSection("Warps") == null) {
			cfg.get().createSection("Warps");
		}
		cfg.save();
		
		for(String key : cfg.get().getConfigurationSection("Warps").getKeys(false)) {
			if(warpExists(key))
				continue;
			System.out.println("Creating Warp: " + key);
			Warp warp = new Warp();
			warp.setName(key);
			
			double x = cfg.get().getDouble("Warps." + key + ".x");
			double y = cfg.get().getDouble("Warps." + key + ".y");
			double z = cfg.get().getDouble("Warps." + key + ".z");
			float yaw = (float)cfg.get().getDouble("Warps." + key + ".yaw");
			float pitch = (float)cfg.get().getDouble("Warps." + key + ".pitch");
			String w = cfg.get().getString("Warps." + key + ".world");
			World world = Bukkit.getWorld(w);
			
			Location loc = new Location(world, x,y,z,yaw,pitch);
			
			warp.setLocation(loc);
			addWarp(warp);
		}
	}
	
	public void saveWarps() {
		Config cfg = pl.fm.getConfig("spawns.yml");
		for(Warp warp : warps) {
			cfg.set("Warps." + warp.getName() + ".x", warp.getX());
			cfg.set("Warps." + warp.getName() + ".y", warp.getY());
			cfg.set("Warps." + warp.getName() + ".z", warp.getZ());
			cfg.set("Warps." + warp.getName() + ".yaw", warp.getYaw());
			cfg.set("Warps." + warp.getName() + ".pitch", warp.getPitch());
			cfg.set("Warps." + warp.getName() + ".world", warp.getWorld());
		}
		cfg.save();
		for(String key : cfg.get().getConfigurationSection("Warps").getKeys(false)) {
			if(warpExists(key))
				continue;
			cfg.get().set("Warps." + key, null);
		}
		cfg.save();
	}

}
