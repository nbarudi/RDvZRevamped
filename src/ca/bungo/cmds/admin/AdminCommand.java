package ca.bungo.cmds.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.arguments.DataArgument;
import ca.bungo.cmds.admin.arguments.ItemsArgument;
import ca.bungo.cmds.admin.arguments.MoreArgument;
import ca.bungo.cmds.admin.arguments.PunishArgument;
import ca.bungo.cmds.admin.arguments.ReleaseMonstersArgument;
import ca.bungo.cmds.admin.arguments.ReloadArgument;
import ca.bungo.cmds.admin.arguments.RestartArgument;
import ca.bungo.cmds.admin.arguments.StartArgument;
import ca.bungo.cmds.admin.arguments.VanishArgument;
import ca.bungo.cmds.admin.arguments.WorldArgument;
import ca.bungo.main.RDvZ;

public class AdminCommand implements CommandExecutor {

    
    private List<AdminArgument> arguments = new ArrayList<>();
    

    public AdminCommand(RDvZ plugin){
    	
    	//If a command requires an event This is how it needs to be done!
    	AdminArgument aa = new ItemsArgument(plugin, "items");
    	arguments.add(aa);
    	plugin.getServer().getPluginManager().registerEvents(aa, plugin);
    	
    	arguments.add(new RestartArgument(plugin, "restart"));
    	arguments.add(new WorldArgument(plugin, "world"));
    	arguments.add(new MoreArgument(plugin, "more"));
    	arguments.add(new ReloadArgument(plugin, "reload"));
    	arguments.add(new StartArgument(plugin, "start"));
    	arguments.add(new DataArgument(plugin, "data"));
    	arguments.add(new ReleaseMonstersArgument(plugin, "release"));
    	arguments.add(new VanishArgument(plugin, "vanish"));
    	
    	aa = new PunishArgument(plugin, "manage");
    	arguments.add(aa);
    	plugin.getServer().getPluginManager().registerEvents(aa, plugin);
    }

   
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    	if(!commandSender.hasPermission("rdvz.admin")) {
    		commandSender.sendMessage(ChatColor.DARK_RED + "Invalid permissions!");
    		return true;
    	}
    	if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(ChatColor.AQUA + "-------------------\n");
    		//Help message
    		sb.append(ChatColor.GREEN + "- /admin help - Get a list of command arguments.\n");
    		sb.append(ChatColor.AQUA + "-------------------\n");
    		for(AdminArgument arg : arguments) {
    			sb.append(ChatColor.GREEN + "- /admin " + arg.name + " - " + arg.desc + "\n");
    			sb.append(ChatColor.GREEN + "- Usage: " + arg.usage + "\n");
    			sb.append(ChatColor.AQUA + "-------------------\n");
    		}
    		commandSender.sendMessage(sb.toString());
    		return true;
    	}
    	
    	String name = args[0];
    	for(AdminArgument arg : arguments) {
    		if(arg.name.equalsIgnoreCase(name)) {
    			if(arg.requiresPlayer) {
    				if(commandSender instanceof Player) {
    					arg.runArgument(commandSender, args);
    	    			return true;
    				}else {
    					commandSender.sendMessage(ChatColor.RED + "You are required to be a player to run this command!");
    					return true;
    				}
    			}else {
    				arg.runArgument(commandSender, args);
        			return true;
    			}
    		}
    	}
    	
        return true;
    }
}
