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
package de.thejeterlp.dailyrewards.database;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends Database {

    private final File dbFile;

    /**
     * Creates a new instance for SQLite databases.
     *
     * @param dbFile Database file
     */
    public SQLite(File dbFile) { 
        super("org.sqlite.JDBC", Type.SQLITE);
        dbFile.getParentFile().mkdirs();
        this.dbFile = dbFile;
    }

    @Override
    public void reactivateConnection() throws SQLException {
        setConnection(DriverManager.getConnection("jdbc:sqlite://" + dbFile.getAbsolutePath()));
    }

}
