package me.PauMAVA.SkywarsUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Undoer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        Integer modifyCount = undo();
        if (modifyCount == -1) {
            sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RED + "Nothing to undo!");
            return false;
        }
        sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
        return true;
    }

    private Integer undo() {
        HashMap<Location, Material> lastState = Core.getInstance().getIslandCreator().getLastState();
        int i = 0;
        if(lastState.isEmpty()) {
            return -1;
        }
        for(Location l: lastState.keySet()) {
            l.getBlock().setType(lastState.get(l));
            i++;
        }
        return i;
    }

    private void sendMessage(CommandSender theSender, String msg) {
        if(theSender instanceof Player) {
            ((Player) theSender).sendMessage(msg);
        } else if(theSender instanceof ConsoleCommandSender) {
            ((ConsoleCommandSender) theSender).sendMessage(msg);
        }
    }
}
