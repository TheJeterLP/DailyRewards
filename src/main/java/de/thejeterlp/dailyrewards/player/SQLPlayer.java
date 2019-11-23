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

import de.thejeterlp.dailyrewards.database.Database;
import de.thejeterlp.dailyrewards.utils.Utils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLPlayer {

    private final UUID uuid;
    private int last;
    private int id;
    private int daysinrow;
    private Database db;
    private boolean playedtoday;

    public SQLPlayer(UUID uuid, Database db) {
        this.db = db;
        this.uuid = uuid;
        try {
            PreparedStatement s = db.getPreparedStatement("SELECT * FROM `dr_players` WHERE `uuid` = ?;");
            s.setString(1, uuid.toString());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                int last = rs.getInt("seenlast");
                int row = rs.getInt("daysinrow");
                boolean today = rs.getBoolean("playedtoday");
                this.id = id;
                this.last = last;
                this.daysinrow = row;
                this.playedtoday = today;
            }

            db.closeResultSet(rs);
            db.closeStatement(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void update() {
        try {
            PreparedStatement st = db.getPreparedStatement("UPDATE `dr_players` SET `seenlast` = ?, `daysinrow` = ?, `playedtoday` = ? WHERE `id` = ?;");
            st.setInt(1, this.last);
            st.setInt(2, this.daysinrow);
            st.setInt(3, this.id);
            st.setBoolean(4, this.playedtoday);
            st.executeUpdate();
            db.closeStatement(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLastDayPlayed() {
        return last;
    }

    public void updateDay() {
        last = Utils.getDay();
    }

    public int getDaysInRow() {
        return daysinrow;
    }

    public void setDaysInRow(int num) {
        daysinrow = num;
    }

    public void incrementRow() {
        daysinrow++;
    }

    public void resetRow() {
        daysinrow = 1;
    }

    public boolean hasGrabbedInventory() {
        return playedtoday;
    }
    
    public void setGrabbed(boolean bool) {
        playedtoday = bool;
    }

}
