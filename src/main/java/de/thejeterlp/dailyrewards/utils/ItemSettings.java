/*
 * Copyright (C) 2019 joeyp
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
package de.thejeterlp.dailyrewards.utils;

import de.thejeterlp.dailyrewards.DailyRewards;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemSettings {

    private static YamlConfiguration cfg;
    private static final File f = new File(DailyRewards.getInstance().getDataFolder(), "rewards.database");

    public static void load() {
        DailyRewards.getInstance().getDataFolder().mkdirs();
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(f);
    }

    public static List<ItemStack> getReward(int day) {
        List<ItemStack> ret = ItemSerialization.load(day);
        if (ret != null) {
            return ret;
        } else {
            return new ArrayList<>();
        }
    }

    public static void setReward(int day, Inventory inv) {
        ItemSerialization.save(inv, day);
    }

    public static YamlConfiguration getConfig() {
        return cfg;
    }

    public static void save() {
        try {
            cfg.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
