package net.cobaltium.magico.db;

import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.SQLException;

public final class Database {

    private static SqlService sql;

    private Database() {

    }

    private static javax.sql.DataSource getDataSource() throws SQLException {
        if (sql == null) {
            sql = Sponge.getServiceManager().provide(SqlService.class).get();
        }
        return sql.getDataSource("jdbc:h2:./magico");
    }

    public static DataSourceConnectionSource getConnection() throws SQLException {
        return new DataSourceConnectionSource(getDataSource(), "jdbc:h2:./magico");
    }
}
