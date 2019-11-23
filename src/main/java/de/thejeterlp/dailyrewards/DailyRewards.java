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

import de.thejeterlp.dailyrewards.database.Database;
import de.thejeterlp.dailyrewards.database.DatabaseFactory;
import de.thejeterlp.dailyrewards.utils.Config;
import org.bukkit.plugin.java.JavaPlugin;

public class DailyRewards extends JavaPlugin {

    private static DailyRewards INSTANCE;
    private static Database DB;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Config.load();
        DatabaseFactory.init();
        DB = DatabaseFactory.getDatabase();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("is now disabled!");
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
