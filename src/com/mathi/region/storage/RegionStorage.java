package com.mathi.region.storage;

import com.mathi.region.flags.Flag;
import com.mathi.region.flags.Flags;
import com.mathi.region.objects.Region;
import com.mathi.region.utils.LocationUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zMathi
 */
public class RegionStorage extends Database {

    private final String TABLE = "regions";
    private final String TABLE_FLAG = "regions_flags";

    public RegionStorage(String host, Integer port, String database, String user, String password) {
        super(host, port, database, user, password);

        query("CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + "name VARCHAR(16) PRIMARY KEY NOT NULL,"
                + "owner VARCHAR(32),"
                + "world VARCHAR(16),"
                + "location_min VARCHAR(100),"
                + "location_max VARCHAR(100));");
        query("CREATE TABLE IF NOT EXISTS " + TABLE_FLAG + "("
                + "region VARCHAR(16) PRIMARY KEY NOT NULL,"
                + "flag VARCHAR(16) NOT NULL,"
                + "value VARCHAR(256) NOT NULL);");
    }

    public void addRegion(Region region) {
        query("INSERT INTO " + TABLE + " (name, owner, world, location_min, location_max) VALUES (?,?,?,?,?);",
                region.getName(),
                region.getOwner(),
                region.getWorld(),
                LocationUtils.serialize(region.getLocationMin()),
                LocationUtils.serialize(region.getLocationMax()));
    }

    public void removeRegion(Region region) {
        query("DELETE FROM " + TABLE + " WHERE name = ?;", region.getName());
        query("DELETE FROM " + TABLE_FLAG + " WHERE region = ?;", region.getName());
    }

    public void addFlag(Region region, Flag flag) {
        query("INSERT INTO " + TABLE_FLAG + " (region, flag, value) VALUES (?,?,?);",
                region.getName(),
                region.getName(),
                flag.toObject(region.getFlag(flag)));
    }

    public void removeFlag(Region region, Flag flag) {
        query("DELETE FROM " + TABLE_FLAG + " WHERE region = ? AND flag = ?;",
                region.getName(),
                flag.getName());
    }

    public Map<Flag<?>, Object> loadFlags(String regionName, String worldName) {
        Map<Flag<?>, Object> flags = new HashMap<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + TABLE_FLAG + " WHERE region = ?;");
            statement.setString(1, regionName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Flag flag = Flags.getFlag(result.getString("flag"));
                flags.put(flag, flag.fromObject(result.getObject("value")));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flags;
    }

    public Map<String, Region> loadRegions(String worldName) {
        Map<String, Region> regions = new HashMap<>();
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + TABLE +" WHERE world = ?;");
            statement.setString(1, worldName);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Region region = new Region(result.getString("name"), result.getString("owner"), result.getString("world"), LocationUtils.deserialize(result.getString("location_max")), LocationUtils.deserialize(result.getString("location_min")));
                regions.put(result.getString("name"), region);
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }
}
