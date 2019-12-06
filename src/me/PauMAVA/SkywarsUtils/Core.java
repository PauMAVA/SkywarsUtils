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

import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private static Core instance;
    private IslandCreator islandCreator;

    @Override
    public void onEnable() {
        instance = this;
        islandCreator = new IslandCreator();
        this.getCommand("islands").setExecutor(islandCreator);
        this.getCommand("undo").setExecutor(new Undoer());
        this.getCommand("getchests").setExecutor(new ChestCounter());
    }

    @Override
    public void onDisable() {

    }

    public static Core getInstance() {
        return instance;
    }

    public World getWorld() {
        return this.getServer().getWorlds().get(0);
    }

    IslandCreator getIslandCreator() {
        return this.islandCreator;
    }
}
