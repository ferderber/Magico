package net.cobaltium.magico.db.utils;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLUtils {

    public static void closeQuietly(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            ex.toString();

        }
    }

    public static String getDatabaseType(Type t) {
        switch (t.getTypeName().toLowerCase()) {
            case "string":
                return "varchar";
            case "int":
            case "integer":
                return "int";
            case "long":
                return "bigint";
            case "double":
                return "double";
            case "boolean":
                return "boolean";
            case "uuid":
                return "UUID";
            default:
                return "varchar";
        }
    }
}
