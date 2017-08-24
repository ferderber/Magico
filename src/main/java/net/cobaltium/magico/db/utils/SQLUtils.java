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

        }
    }

    public static String getDatabaseType(Type t) {
        switch (t.getTypeName()) {
            case "String":
                return "varchar";
            case "int":
            case "integer":
            case "Integer":
                return "int";
            case "Double":
                return "double";
            case "Boolean":
                return "boolean";
            case "UUID":
                return "UUID";
            default:
                return "varchar";
        }
    }
}
