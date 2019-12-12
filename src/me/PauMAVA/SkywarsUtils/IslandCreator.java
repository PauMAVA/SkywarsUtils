/* SkywarsUtils is a Bukkit/Spigot plugin for Minecraft 1.14.4
 * Copyright (C) 2019  Pau Machetti Vallverdú
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */

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
import java.util.HashMap;
import java.util.List;

public class IslandCreator implements CommandExecutor {

    private HashMap<Location, Material> lastState = new HashMap<Location, Material>();

    /* COMMAND USAGE: /islands <count> <radius> <type> <size> [material]
    * String args:
    *  - [0] = Integer count
    *  - [1] = Integer radius of the island circle
    *  - [2] = String type: sphere, cube, hsphere, hcube
    *  - [3] = Integer size in block of each island
    *  - [4] = Material material: the material of the islands
    * METHODOLOGY:
    *  1. Get a List<Location> form circleRadius and islandCount that refers to the center of each island
    *  2. Create a sphere/cube of the desired material from List<Location>, type and material */

    @Override
    public boolean onCommand(CommandSender theSender, Command command, String label, String[] args) {
        Integer islandCount = 0, circleRadius  = 0, islandSize = 0;
        String type = "";
        if (!(theSender instanceof Player)) {
            sendMessage(theSender, ChatColor.RED + "You must be a player to execute this command!");
            return false;
        }
        if(args == null || args.length < 3 || args.length > 5) {
            sendMessage(theSender, ChatColor.RED + "Argument number mismatch!");
            sendMessage(theSender, ChatColor.RED + "/islands <count> <radius> <type> <size> [material]");
            return false;
        }
        Material material = Material.STONE;
        if(args.length == 5) {
            material = Material.getMaterial(args[4].toUpperCase());
        }
        try {
            islandCount = Integer.parseInt(args[0]);
            circleRadius = Integer.parseInt(args[1]);
            type = args[2];
            islandSize = Integer.parseInt(args[3]);
            List<Location> islandCenters = getLocations(((Player) theSender).getLocation(), islandCount, circleRadius);
            Integer modifyCount = 0;
            switch (type) {
                case "cube": {
                    for(Location origin: islandCenters) {
                       modifyCount += spawnCube(origin, material, islandSize);
                    }
                    sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
                    break;
                }
                case "sphere": {
                    for(Location origin: islandCenters) {
                        modifyCount += spawnSphere(origin, material, islandSize);
                    }
                    sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
                    break;
                }
                case "hcube": {
                    for(Location origin: islandCenters) {
                        modifyCount += spawnHollowCube(origin, material, islandSize);
                    }
                    sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
                    break;
                }
                case "islandbottom": {
                    for(Location origin: islandCenters) {
                        modifyCount += spawnIslandBottom(origin, material, islandSize);
                    }
                    sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
                    break;
                }
                case "hislandbottom": {
                    for(Location origin: islandCenters) {
                        modifyCount += spawnHollowIslandBottom(origin, material, islandSize);
                    }
                    sendMessage(theSender, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.DARK_PURPLE + "SkywarsUtils" + ChatColor.GRAY + "] " + ChatColor.RESET + ChatColor.AQUA + "Modified " + modifyCount + " blocks.");
                    break;
                }
                default: {
                    sendMessage(theSender, ChatColor.RED + "The option \'" + type + "\' is not supported. Supported options: cube, sphere, hcube, islandbottom, hislandbottom");
                    break;
                }
            }
        } catch (NumberFormatException e) {
            sendMessage(theSender, ChatColor.RED + "");
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

    private Integer spawnCube(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        /* Iterate over each plane */
        for (int i = -radius; i < radius; i++) {
            /* Iterate over each row */
            for (int j = -radius; j < radius; j++) {
                /* Iterate over each block */
                for (int k = -radius; k < radius; k++) {
                    Location block = new Location(Core.getInstance().getWorld(), origin.getX() + k, origin.getY() + i, origin.getZ() + j);
                    lastState.put(block, block.getBlock().getType());
                    block.getBlock().setType(material);
                    modifiedBlocks++;
                }
            }
        }
        return modifiedBlocks;
    }

    private Integer spawnSphere(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        for (int x = (int)origin.getX() - radius; x <= origin.getX() + radius; x++) {
            for (int y = (int)origin.getY() - radius; y <= origin.getY() + radius; y++) {
                for (int z = (int)origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                    if ((origin.getX() - x) * (origin.getX() - x) + (origin.getY() - y) * (origin.getY() - y) + (origin.getZ() - z) * (origin.getZ() - z) <= (radius * radius)) {
                        Location block = new Location(Core.getInstance().getWorld(), x, y, z);
                        lastState.put(block, block.getBlock().getType());
                        block.getBlock().setType(material);
                        modifiedBlocks++;
                    }
                }
            }
        }
        return modifiedBlocks;
    }

    private Integer spawnHollowCube(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        /* Iterate over each plane */
        for (int i = -radius; i < radius; i++) {
            /* Check if the actual plane is the first or the last */
            if(i == -radius || i == radius - 1) {
                /* Iterate over each row */
                for (int j = -radius; j < radius; j++) {
                    /* Iterate over each block */
                    for (int k = -radius; k < radius; k++) {
                        Location block = new Location(Core.getInstance().getWorld(), origin.getX() + k, origin.getY() + i, origin.getZ() + j);
                        lastState.put(block, block.getBlock().getType());
                        block.getBlock().setType(material);
                        modifiedBlocks++;
                    }
                }
            } else {
                /* Iterate over each row */
                for (int j = -radius; j < radius; j++) {
                    /* Iterate over each block */
                    for (int k = -radius; k < radius; k++) {
                        if (k == -radius || k == radius -1 || j == radius - 1 ||j == -radius) {
                            Location block = new Location(Core.getInstance().getWorld(), origin.getX() + k, origin.getY() + i, origin.getZ() + j);
                            lastState.put(block, block.getBlock().getType());
                            block.getBlock().setType(material);
                            modifiedBlocks++;
                        }
                    }
                }
            }
        }
        return modifiedBlocks;
    }

    private Integer spawnIslandBottom(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        for(int i = 0; i < radius; radius--) {
            origin.setY(origin.getY() - 1);
            modifiedBlocks += makeCircle(origin,material, radius);
        }
        return modifiedBlocks;
    }

    private Integer spawnHollowIslandBottom(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        for(int i = 0; i < radius; radius--) {
            origin.setY(origin.getY() - 1);
            modifiedBlocks += makeHollowCircle(origin,material, radius);
        }
        return modifiedBlocks;
    }

    private Integer makeHollowCircle(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        for (double i = 0.0; i < 360.0; i += 0.1) {
            double angle = i * Math.PI / 180;
            Location block = new Location(Core.getInstance().getWorld(), (int)(origin.getX() + radius * Math.cos(angle)), origin.getY(), (int)(origin.getZ() + radius * Math.sin(angle)));
            lastState.put(block, block.getBlock().getType());
            block.getBlock().setType(material);
            modifiedBlocks++;
        }
        return modifiedBlocks;
    }

    private Integer makeCircle(Location origin, Material material, Integer radius) {
        Integer modifiedBlocks = 0;
        for (int j = 0; j < radius; radius--) {
            for (double i = 0.0; i < 360.0; i += 0.1) {
                double angle = i * Math.PI / 180;
                Location block = new Location(Core.getInstance().getWorld(), (int)(origin.getX() + radius * Math.cos(angle)), origin.getY(), (int)(origin.getZ() + radius * Math.sin(angle)));
                lastState.put(block, block.getBlock().getType());
                block.getBlock().setType(material);
                modifiedBlocks++;
            }
        }
        return modifiedBlocks;
    }

    private List<Location> getLocations(Location playerLoc, Integer islandCount, Integer radius) {
        /* TODO create function that returns the locations of all the centers of each island based on the island count, the original location and the island count */
        List<Location> locations = new ArrayList<Location>();
        Double angle = 0D;
        for(int i = 0; i < islandCount; i++) {
            Location point = new Location(Core.getInstance().getWorld(), playerLoc.getX() + radius * Math.cos(Math.toRadians(angle)), playerLoc.getY(), playerLoc.getZ() + radius * Math.sin(Math.toRadians(angle)));
            locations.add(point);
            angle += 360D/islandCount;
        }
        return locations;
    }

    HashMap<Location, Material> getLastState() {
        return this.lastState;
    }

    void resetLastState() {
        this.lastState = new HashMap<Location, Material>();
    }
}
