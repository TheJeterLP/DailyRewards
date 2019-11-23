/*
 * Copyright (C) 2019 TheJeterLP
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.thejeterlp.dailyrewards;

import de.thejeterlp.dailyrewards.commands.CommandManager;
import de.thejeterlp.dailyrewards.database.Database;
import de.thejeterlp.dailyrewards.database.DatabaseFactory;
import de.thejeterlp.dailyrewards.events.PlayerEvents;
import de.thejeterlp.dailyrewards.player.PlayerManager;
import de.thejeterlp.dailyrewards.utils.Config;
import de.thejeterlp.dailyrewards.utils.ItemSettings;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyRewards extends JavaPlugin {
    
    private static DailyRewards INSTANCE;
    private static Database DB;
    private CommandManager cmanager;
    
    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        ItemSettings.load();
        DatabaseFactory.init();
        DB = DatabaseFactory.getDatabase();
        PlayerManager.init();
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        cmanager = new CommandManager(this);
        cmanager.registerClass(RewardCommands.class);
    }
    
    @Override
    public void onDisable() {
        PlayerManager.save();
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("is now disabled!");
        INSTANCE = null;
        DB = null;
    }
    
    public static DailyRewards getInstance() {
        return INSTANCE;
    }
    
    public static Database getDB() {
        return DB;
    }
    
    public static void debug(String message) {
        if (!Config.DEBUG.getBoolean()) {
            return;
        }
        String output = "[DEBUG] " + message;
        getInstance().getLogger().info(output);
    }
    
}
