package net.cobaltium.magico.db.tables;

import com.google.inject.Inject;
import net.cobaltium.magico.db.Database;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SqlTable {

    @Inject
    private Logger logger;

    public SqlTable() {
        Annotation[] annotations = this.getClass().getAnnotations();
        Field[] fields = this.getClass().getDeclaredFields();
        Queue<Field> columns = new LinkedList<Field>();
        for (int i = 0; i < fields.length; i++) {
            Annotation annotation = fields[i].getDeclaredAnnotation(DataColumn.class);
            if (annotation != null) {
                columns.add(fields[i]);
            }
        }
        String str = "CREATE TABLE IF NOT EXISTS " + this.getClass().getSimpleName() + "(";
        while (!columns.isEmpty()) {
            Field field = columns.remove();
            DataColumn dc = (DataColumn) field.getDeclaredAnnotation(DataColumn.class);
            str += " ";
            str += field.getName() + " " + field.getGenericType().getTypeName();
            if (dc.primaryKey()) {
                str += " PRIMARY KEY";
            }
            if (!columns.isEmpty()) {
                str += ",";
            }
        }
        str += ");";

        try {
            createTable(str);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public void createTable(String tableStatement) throws SQLException {
        Database db = new Database();
        Connection con = db.getBaseConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.execute(tableStatement);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } finally {
            con.close();
        }
    }
}
