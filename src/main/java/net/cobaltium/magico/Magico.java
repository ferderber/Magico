package net.cobaltium.magico;

import net.cobaltium.magico.data.*;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.DatabaseAccessObject;
import net.cobaltium.magico.db.tables.StructureLocation;
import net.cobaltium.magico.db.utils.SQLUtils;
import net.cobaltium.magico.listeners.MagicoListener;
import net.cobaltium.magico.spells.SpellList;
import net.cobaltium.magico.spells.SpellType;
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

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
                .manipulatorId("player-fireball")
                .dataName("Player Fireball")
                .buildAndRegister(plugin);
        DataRegistration.builder().dataClass(MagicoUserData.class)
                .immutableClass(ImmutableMagicoUserData.class)
                .builder(new MagicoUserBuilder())
                .manipulatorId("magico-user")
                .dataName("Magico User")
                .buildAndRegister(plugin);


        Database db = new Database();
        StructureLocation location = new StructureLocation(0, 0, 64, 0);
        Connection con = null;
        List<StructureLocation> structures = null;
        try {
            con = db.getConnection();
            DatabaseAccessObject<StructureLocation> locationDao = new DatabaseAccessObject<>(con, StructureLocation.class);
            locationDao.createTable();
            structures = locationDao.getAll();
        } catch (SQLException ex) {
            logger.error("DB error", ex);
        } catch (IllegalAccessException ex) {
            logger.error("Unable to access properties in data object class", ex);
        } catch (InstantiationException ex) {
            logger.error("Data Objects must have a default no-arg constructor", ex);
        } finally {
            SQLUtils.closeQuietly(con);
        }

        //Listeners
        this.game.getEventManager().registerListeners(this, new MagicoListener(this.plugin, structures));
        SpellType[] spellTypes = SpellList.ALL;
        for (SpellType spellType : spellTypes) {
            if (spellType.getListener() != null) {
                this.game.getEventManager().registerListeners(this, spellType.getListener());
            }
        }

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
