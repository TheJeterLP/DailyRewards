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
package de.thejeterlp.dailyrewards.player;

import de.thejeterlp.dailyrewards.DailyRewards;
import de.thejeterlp.dailyrewards.database.Database;
import de.thejeterlp.dailyrewards.database.DatabaseFactory;
import de.thejeterlp.dailyrewards.utils.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerManager {

    private static final HashMap<UUID, SQLPlayer> players = new HashMap<>();
    private static final Database conn = DatabaseFactory.getDatabase();

    public static HashMap<UUID, SQLPlayer> getPlayers() {
        return players;
    }

    public static void init() {
        players.clear();
        try {
            PreparedStatement s = conn.getPreparedStatement("SELECT `uuid` FROM `dr_players`");
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                SQLPlayer p = new SQLPlayer(UUID.fromString(rs.getString("uuid")), conn);
                players.put(p.getUuid(), p);
            }
            DailyRewards.getInstance().getLogger().log(Level.INFO, "Loaded {0} players!", players.size());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void save() {
        int saved = 0;
        for (SQLPlayer p : players.values()) {
            p.update();
            saved++;
        }
        players.clear();
        DailyRewards.getInstance().getLogger().log(Level.INFO, "Saved {0} players!", saved);
    }

    public static SQLPlayer getPlayer(OfflinePlayer p) {
        return players.get(p.getUniqueId());
    }

    public static SQLPlayer getPlayer(int id) {
        for (UUID u : players.keySet()) {
            SQLPlayer p = players.get(u);
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static void unload(SQLPlayer p) {
        p.update();
        if (players.containsKey(p.getUuid())) {
            players.remove(p.getUuid());
        }
    }

    public static void insert(Player p) {

        try {
            PreparedStatement s = conn.getPreparedStatement("INSERT INTO `dr_players` (`uuid`, `seenlast`, `daysinrow`, `playedtoday`) VALUES (?, ?, ?, ?);");
            s.setString(1, p.getUniqueId().toString());
            s.setInt(2, Utils.getDay());   
            s.setInt(3, 1);
            s.setBoolean(4, false);
            s.executeUpdate();
            conn.closeStatement(s);

            SQLPlayer bp = new SQLPlayer(p.getUniqueId(), conn);
            players.put(bp.getUuid(), bp);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean hasPlayedBefore(OfflinePlayer p) {
        return getPlayer(p) != null;
    }

}
