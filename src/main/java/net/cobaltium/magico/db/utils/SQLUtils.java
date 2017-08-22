package net.cobaltium.magico.db.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLUtils {

    public static void closeQuietly(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {

        }
    }
}
