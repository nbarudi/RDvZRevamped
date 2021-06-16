package ca.bungo.util.backend.cooldown;

import org.bukkit.Bukkit;

import ca.bungo.main.RDvZ;

public class Cooldown {
	
	private double remainingTime;
	private int task;
	private boolean isActive;
	
	public Cooldown(RDvZ pl, double ticks) {
		remainingTime = ticks;
		isActive = true;
		task = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, () ->{
			if(!isActive)
				return;
			if(remainingTime <= 0) {
				isActive = false;
				return;
			}
			remainingTime--;
		}, 1, 1);
	}
	
	public double getRemainingTime() {
		if(isActive) {
			//1 tick = 50 milliseconds
			//1 second = 1000 milliseconds
			/*
			 * 20 ticks
			 * 20 * 50 = 1000
			 * 1000 / 1000 = 1
			 * 1 second
			 * 
			 * 15 ticks
			 * 15 * 50 = 750
			 * 750 / 1000 = 0.75
			 * 0.75 seconds
			 * */
			
			double ms = remainingTime * 50;
			double seconds = ms / 1000;
			return seconds;
		}
		else {
			Bukkit.getScheduler().cancelTask(task);
			return 0;
		}
	}

}
