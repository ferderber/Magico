package net.cobaltium.magico.db;

import net.cobaltium.magico.db.tables.DataColumn;
import net.cobaltium.magico.db.utils.SQLUtils;
import org.spongepowered.api.util.Tuple;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessObject<T> {

    private List<Tuple<Field, DataColumn>> fields;
    private Field primaryField;
    private String insertString;
    private Class<? extends T> tableClass;
    private Connection con;

    public DatabaseAccessObject(Connection con, Class<T> tableClass) {
        this.tableClass = tableClass;
        this.con = con;
        Field[] classFields = tableClass.getDeclaredFields();
        List<Tuple<Field, DataColumn>> columns = new ArrayList<>();
        for (Field field : classFields) {
            DataColumn annotation = field.getDeclaredAnnotation(DataColumn.class);
            if (annotation != null) {
                columns.add(Tuple.of(field, annotation));
            }
            if (annotation.primaryKey()) {
                this.primaryField = field;
            }
        }
        this.fields = columns;
        this.insertString = getInsertString();
    }

    private String getInsertString() {
        if (insertString == null || insertString.length() == 0) {
            StringBuilder insertBuilder = new StringBuilder();
            StringBuilder valuesBuilder = new StringBuilder();
            insertBuilder.append("INSERT INTO ").append(tableClass.getSimpleName()).append(" (");
            valuesBuilder.append(") VALUES (");
            for (int i = 0; i < fields.size(); i++) {
                DataColumn dc = fields.get(i).getSecond();
                if (!dc.primaryKey() || !dc.autoIncrement()) {
                    insertBuilder.append(fields.get(i).getFirst().getName());
                    if (i != fields.size() - 1) {
                        insertBuilder.append(", ");
                    }
                    String type = SQLUtils.getDatabaseType(fields.get(i).getFirst().getGenericType());
                    if (type == "varchar" || type == "uuid") {
                        valuesBuilder.append("'%s'");
                    } else {
                        valuesBuilder.append("%s");
                    }
                    if (i != fields.size() - 1) {
                        valuesBuilder.append(", ");
                    }
                }
            }
            valuesBuilder.append(");");
            insertBuilder.append(valuesBuilder);
            insertString = insertBuilder.toString();
        }
        return insertString;
    }

    public void insert(T sqlObject) throws SQLException, IllegalAccessException {
        List<Object> objects = new ArrayList<>();
        for (Tuple<Field, DataColumn> f : fields) {
            if (!f.getSecond().primaryKey() || !f.getSecond().autoIncrement()) {
                f.getFirst().setAccessible(true);
                objects.add(f.getFirst().get(sqlObject));
            }
        }
        Statement stmt = con.createStatement();
        stmt.execute(String.format(insertString, objects.toArray()));
    }

    private T createObject(ResultSet rs) throws IllegalAccessException, SQLException, InstantiationException {
        T obj = tableClass.newInstance();
        for (Tuple<Field, DataColumn> f : fields) {
            f.getFirst().setAccessible(true);
            f.getFirst().set(obj, rs.getObject(f.getFirst().getName()));
        }
        return obj;
    }

    public List<T> getAll() throws IllegalAccessException, SQLException, InstantiationException {
        List<T> objs = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableClass.getSimpleName() + ";");
        while (rs.next()) {
            objs.add(createObject(rs));
        }
        return objs;
    }

    public List<T> getAllById(String s) throws IllegalAccessException, SQLException, InstantiationException {
        List<T> objs = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + tableClass.getSimpleName() + " where " + primaryField.getName() + " = " + s + ";");
        while (rs.next()) {
            objs.add(createObject(rs));
        }
        return objs;
    }

    public void createTable() throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableClass.getSimpleName()).append(" (");
        for (int i = 0; i < fields.size(); i++) {
            DataColumn dc = fields.get(i).getSecond();
            sqlBuilder.append(fields.get(i).getFirst().getName())
                    .append(" ")
                    .append(SQLUtils.getDatabaseType(fields.get(i).getFirst().getGenericType()));
            if (dc.autoIncrement()) {
                sqlBuilder.append((" auto_increment"));
            }
            if (dc.primaryKey()) {
                sqlBuilder.append(" PRIMARY KEY");
            }
            if (i != fields.size() - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(");");

        Statement stmt = con.createStatement();
        stmt.execute(sqlBuilder.toString());
    }
}
