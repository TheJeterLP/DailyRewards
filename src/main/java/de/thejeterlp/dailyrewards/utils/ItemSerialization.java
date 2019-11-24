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
package de.thejeterlp.dailyrewards.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemSerialization {

    public static void save(Inventory inventory, int day) {
        ConfigurationSection sec = ItemSettings.getConfig().getConfigurationSection("Rewards." + day);
        if (sec == null) {
            sec = ItemSettings.getConfig().createSection("Rewards." + day);
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item != null && item.getType() != Material.AIR) {
                sec.set(Integer.toString(i), item);
            } else {
                ItemStack stack = new ItemStack(Material.AIR, 1);
                sec.set(Integer.toString(i), stack);
            }
        }
        ItemSettings.save();
    }

    public static List<ItemStack> load(int day) {
        ConfigurationSection sec = ItemSettings.getConfig().getConfigurationSection("Rewards." + day);
        List<ItemStack> stacks = new ArrayList<>();

        if (sec == null) {
            return stacks;
        }

        try {
            // Try to parse this inventory
            for (String key : sec.getKeys(false)) {
                int number = Integer.parseInt(key);

                // Size should always be bigger
                while (stacks.size() <= number) {
                    stacks.add(null);
                }

                stacks.set(number, (ItemStack) sec.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return result
        return stacks;
    }

}
