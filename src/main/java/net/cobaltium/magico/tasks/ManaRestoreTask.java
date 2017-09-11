package net.cobaltium.magico.tasks;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.StructureLocation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ManaRestoreTask implements Consumer<Task> {


    public ManaRestoreTask() {
    }

    public void accept(Task task) {

        DataSourceConnectionSource con = null;
        try {
            con = Database.getConnection();
            Dao<StructureLocation, Long> structureLocationDao = DaoManager.createDao(con, StructureLocation.class);
            List<StructureLocation> structures = structureLocationDao.queryForAll();

            Collection<Player> players = Sponge.getServer().getOnlinePlayers();
            players.forEach((player) -> {
                for (StructureLocation structure : structures) {
                    double distance = player.getLocation().getPosition().distance(structure.getBlockLocation().toDouble());
                    if (distance <= 100) {
                        MagicoUserData data = player.getOrCreate(MagicoUserData.class).get();
                        if (data.getMana() < 200) {
                            data.modifyMana(data.getManaRestoreMultiplier() * 5);
                            player.offer(data);
                        }
                        break;
                    }
                }
            });

        } catch(SQLException ex) {

        } finally {
            con.closeQuietly();
        }

    }
}
