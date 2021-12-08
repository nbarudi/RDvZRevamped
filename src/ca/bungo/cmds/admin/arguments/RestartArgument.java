package ca.bungo.cmds.admin.arguments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class RestartArgument extends AdminArgument{
	

	public RestartArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Restart the world, and round data.";
		this.requiresPlayer = false;
		
	}
	
	private static boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }
	
	@SuppressWarnings("unused")
	private static void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		
		
		World lobby = Bukkit.getWorld("RDvZ_Lobby");
		if(lobby == null) {
			WorldCreator wc = new WorldCreator("RDvZ_Lobby");
			wc.type(WorldType.FLAT);
			wc.generateStructures(false);
			lobby = wc.createWorld();
		}
		
		final World t_lobby = lobby;
		
		lobby.setDifficulty(Difficulty.PEACEFUL);
		
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Sending players to lobby world!");
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(pl.wm.getWarp("spawn").getLocation());
		}
		
		Bukkit.broadcastMessage(ChatColor.RED + "Please wait while we reset the map...");
		
		
		World w = Bukkit.getWorld(pl.currentRound.worldName);
		File f = w.getWorldFolder();
		
		Bukkit.unloadWorld(pl.currentRound.worldName, false);
		deleteWorld(f);
		Bukkit.broadcastMessage(ChatColor.RED + "World unloaded...");
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () ->{
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () ->{
				Bukkit.broadcastMessage(ChatColor.RED + "Reloading server...");
				Bukkit.reload();
				Bukkit.broadcastMessage(ChatColor.GREEN + "Reload completed...");
				Bukkit.broadcastMessage(ChatColor.GREEN + "Reset completed!");
				t_lobby.setDifficulty(Difficulty.PEACEFUL);
			}, 20);
		}, 40);
		
	}

}
