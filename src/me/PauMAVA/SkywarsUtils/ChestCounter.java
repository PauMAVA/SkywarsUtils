package me.PauMAVA.SkywarsUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChestCounter implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        List<Location> chests = new ArrayList<Location>();
        if(args.length == 0) {
            sendMessage(theSender,  ChatColor.RED + "Argument number mismatch!");
            sendMessage(theSender,  ChatColor.RED + "/getchests <radius>");
            return false;
        }
        Integer radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sendMessage(theSender,  ChatColor.RED + "You must introduce a valid radius!");
            return false;
        }
        for(int x = -radius; x < radius; x++) {
            for(int y = 0; y < 256; y++) {
                for (int z = -radius; z < radius; z++) {
                    Location loc = new Location(Core.getInstance().getWorld(), x, y, z);
                    if(loc.getBlock().getType() == Material.CHEST) {
                        chests.add(loc);
                    }
                }
            }
        }
        sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Chests found: ");
        for (Location l: chests) {
            sendMessage(theSender, (int) l.getX() + ", " + (int) l.getY() + ", " + (int) l.getZ());
        }
        return false;
    }

    private void sendMessage(CommandSender theSender, String msg) {
        if(theSender instanceof Player) {
            ((Player) theSender).sendMessage(msg);
        } else if(theSender instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) theSender).sendMessage(msg);
        }
    }
}
