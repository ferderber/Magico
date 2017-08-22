package net.cobaltium.magico.db;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.Connection;
import java.sql.SQLException;


public class Database {

    private SqlService sql;

    private javax.sql.DataSource getDataSource(String jdbcUrl) throws SQLException {
        if (sql == null) {
            sql = Sponge.getServiceManager().provide(SqlService.class).get();
        }
        return sql.getDataSource(jdbcUrl);
    }

    public Connection getConnection() throws SQLException {
        return getDataSource("jdbc:h2:./magico.db").getConnection();
    }
}
