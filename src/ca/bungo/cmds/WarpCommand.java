package ca.bungo.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.warps.Warp;

public class WarpCommand implements CommandExecutor {
	
	RDvZ pl;
	
	public WarpCommand(RDvZ pl) {
		this.pl = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player))
			return false;
		
		if(args.length < 1) {
			sender.sendMessage(ChatManager.formatColor("&cInvalid Usage!"));
			return true;
		}
		
		Player player = (Player) sender;
		
		if(!(player.hasPermission("rdvz.admin"))) {
			if(!pl.cu.verifyPlayerCommand(player, args[0])) {
				player.sendMessage(ChatManager.formatColor("&cNow Now... &aNo need to try and abuse permissions!"));
				return true;
			}
			Warp warp = pl.wm.getWarp(args[1]);
			if(warp == null) {
				//Only I am stupid enough to HARD CODE an 'Error Code' that means literally nothing.... Bungo Moment
				player.sendMessage(ChatManager.formatColor("&cSeems something went wrong.. &eContact nbarudi on discord and report this issue: &bnbarudi#0001\n &cError Code: 224"));
				return true;
			}
			
			player.sendMessage(ChatManager.formatColor("&eWarping to: &a" + warp.getName()));
			player.teleport(warp.getLocation());
			
			return true;
		}
		
		if(args[0].equalsIgnoreCase("set") && args.length == 2) {
			Warp warp = new Warp();
			warp.setName(args[1]);
			warp.setLocation(player.getLocation());
			pl.wm.addWarp(warp);
			player.sendMessage(ChatManager.formatColor("&eCreated warp: &5" + warp.getName()));
			return true;
		}
		else if(args[0].equalsIgnoreCase("delete") && args.length == 2) {
			player.sendMessage(ChatManager.formatColor("&eAttempting to remove: &5" + args[1]));
			pl.wm.removeWarp(args[1]);
			return true;
		}
		else if(args.length == 2) {
			if(!pl.cu.verifyPlayerCommand(player, args[0])) {
				player.sendMessage(ChatManager.formatColor("&cNow Now... &aNo need to try and abuse permissions!"));
				return true;
			}
			Warp warp = pl.wm.getWarp(args[1]);
			if(warp == null) {
				//Only I am stupid enough to HARD CODE an 'Error Code' that means literally nothing.... Bungo Moment
				player.sendMessage(ChatManager.formatColor("&cSeems something went wrong.. &eContact nbarudi on discord and report this issue: &bnbarudi#0001\n &cError Code: 224"));
				return true;
			}
			
			player.sendMessage(ChatManager.formatColor("&eWarping to: &a" + warp.getName()));
			player.teleport(warp.getLocation());
			
			return true;
		}
		else {
			Warp warp = pl.wm.getWarp(args[0]);
			if(warp == null) {
				//Only I am stupid enough to HARD CODE an 'Error Code' that means literally nothing.... Bungo Moment
				player.sendMessage(ChatManager.formatColor("&cSeems something went wrong.. &eContact nbarudi on discord and report this issue: &bnbarudi#0001\n &cError Code: 224"));
				return true;
			}
			
			player.sendMessage(ChatManager.formatColor("&eWarping to: &a" + warp.getName()));
			player.teleport(warp.getLocation());
			
		}
		
		return true;
	}

}
