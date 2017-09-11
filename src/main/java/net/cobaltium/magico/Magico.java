package net.cobaltium.magico;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.cobaltium.magico.commands.user.UserCommand;
import net.cobaltium.magico.commands.user.UserGiveSpellCommand;
import net.cobaltium.magico.commands.user.UserSetManaCommand;
import net.cobaltium.magico.commands.utils.CommandFactory;
import net.cobaltium.magico.data.ImmutableMagicoProjectileData;
import net.cobaltium.magico.data.ImmutableMagicoUserData;
import net.cobaltium.magico.data.MagicoProjectileBuilder;
import net.cobaltium.magico.data.MagicoProjectileData;
import net.cobaltium.magico.data.MagicoUserBuilder;
import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.StructureLocation;
import net.cobaltium.magico.db.tables.UserSpells;
import net.cobaltium.magico.listeners.MagicoListener;
import net.cobaltium.magico.spells.SpellType;
import net.cobaltium.magico.tasks.ManaRestoreTask;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

@Plugin(id = "magico", name = "Magico", version = "0.01")
public class Magico {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private PluginContainer plugin;

    @Listener
    public void preInit(GamePreInitializationEvent e) {
        DataRegistration.builder().dataClass(MagicoProjectileData.class)
                .immutableClass(ImmutableMagicoProjectileData.class)
                .builder(new MagicoProjectileBuilder())
                .manipulatorId("projectile-data")
                .dataName("Magico Projectile data")
                .buildAndRegister(plugin);
        DataRegistration.builder().dataClass(MagicoUserData.class)
                .immutableClass(ImmutableMagicoUserData.class)
                .builder(new MagicoUserBuilder())
                .manipulatorId("magico-user")
                .dataName("Magico User")
                .buildAndRegister(plugin);


        StructureLocation location = new StructureLocation(0, 0, 64, 0);
        ConnectionSource con = null;
        List<StructureLocation> structures = null;
        try {
            con = Database.getConnection();
            TableUtils.createTableIfNotExists(con, UserSpells.class);
            TableUtils.createTableIfNotExists(con, StructureLocation.class);
            Dao<StructureLocation, Long> structureDao = DaoManager.createDao(con, StructureLocation.class);
            structures = structureDao.queryForAll();
        } catch (SQLException ex) {
            logger.error("DB error", ex);
        } finally {
            con.closeQuietly();
        }
        Task.builder()
                .execute(new ManaRestoreTask(structures))
                .interval(5, TimeUnit.SECONDS)
                .submit(plugin);

        //Listeners
        this.game.getEventManager().registerListeners(this, new MagicoListener(this.plugin, structures));
        SpellType[] spellTypes = SpellType.values();
        for (SpellType spellType : spellTypes) {
            if (spellType.getListener() != null) {
                this.game.getEventManager().registerListeners(this, spellType.getListener());
            }
        }

        CommandFactory.getInstance().registerCommands(this,
                new UserCommand(), new UserGiveSpellCommand(), new UserSetManaCommand());

    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("started");
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        logger.info("reloaded");
    }

}
