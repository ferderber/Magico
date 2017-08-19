package net.cobaltium.magico.db;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.Connection;
import java.sql.SQLException;


public class Database {

    @Inject
    private Logger logger;

    private SqlService sql;

    public javax.sql.DataSource getDataSource(String jdbcUrl) throws SQLException {
        if (sql == null) {
            sql = Sponge.getServiceManager().provide(SqlService.class).get();
        }
        return sql.getDataSource(jdbcUrl);
    }

    public Connection getBaseConnection() throws SQLException {
        return getDataSource("jdbc:h2:./magico.db").getConnection();
    }
}
