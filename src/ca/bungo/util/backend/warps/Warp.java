package ca.bungo.util.backend.warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Warp {
	
	private String name = "";
	
	private double x = 0;
	private double y = 0;
	private double z = 0;
	private float yaw = 0;
	private float pitch = 0;
	
	private String world = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}
	
	public void setLocation(Location loc) {
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
		this.world = loc.getWorld().getName();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}
	
	

}
