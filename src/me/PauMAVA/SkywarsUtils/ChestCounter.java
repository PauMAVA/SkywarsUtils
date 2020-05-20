package me.PauMAVA.SkywarsUtils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChestCounter implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        File file = createFile();
        FileWriter writer = null;
        if (file != null) {
            try {
                 writer = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            Core.getInstance().getLogger().info((int) l.getX() + ", " + (int) l.getY() + ", " + (int) l.getZ());
            if (writer != null) {
                try {
                    writer.write((int) l.getX() + "," + (int) l.getY() + "," + (int) l.getZ() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private File createFile() {
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd");
            File file = new File(Core.getInstance().getDataFolder().getPath() + "/chests" + dateFormat.format(date) + ".csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
