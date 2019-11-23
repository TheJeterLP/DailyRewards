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
package de.thejeterlp.dailyrewards.events;

import de.thejeterlp.dailyrewards.player.PlayerManager;
import de.thejeterlp.dailyrewards.player.SQLPlayer;
import de.thejeterlp.dailyrewards.utils.Config;
import de.thejeterlp.dailyrewards.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!PlayerManager.hasPlayedBefore(e.getPlayer())) {
            PlayerManager.insert(e.getPlayer());
        }
        
        SQLPlayer p = PlayerManager.getPlayer(e.getPlayer());
        
        int daydiff = Utils.getDay() - p.getLastDayPlayed();

        //player logged in over a year ago, day stored is in the future, reset days
        if (daydiff < 0) {
            e.getPlayer().sendMessage(ChatColor.RED + "Your last login was over a year ago, resetting the days...");
            p.updateDay();
        }
        
        if (daydiff > 1 || p.getDaysInRow() == Config.RESET.getInteger()) {
            p.resetRow();
            e.getPlayer().sendMessage(ChatColor.GREEN + "Your days in row have been reset.");
        } else if (daydiff == 1) {
            p.setGrabbed(false);
            p.incrementRow();
            e.getPlayer().sendMessage(ChatColor.GREEN + "You are now playing " + ChatColor.GOLD + p.getDaysInRow() + ChatColor.GREEN + " days in row.");
        }
        
        if (p.getLastDayPlayed() != Utils.getDay()) {
            p.updateDay();
        }
        
        if (!p.hasGrabbedInventory()) {
            e.getPlayer().sendMessage("§aYou havent played already today, grab your reward with /rewards grab. Be careful, you can only execute this command §4ONCE§a, so be prepared to have enough space in your inventory!");
        } else {
            e.getPlayer().sendMessage("§aAlready grabbed reward today, the next one will be available tomorrow!");
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SQLPlayer p = PlayerManager.getPlayer(e.getPlayer());
        if (p.getLastDayPlayed() != Utils.getDay()) {
            p.updateDay();
        }
    }
    
}
