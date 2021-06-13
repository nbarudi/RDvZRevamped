package ca.bungo.util;

import org.bukkit.ChatColor;

public class ChatManager {
	
	public static String formatColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
