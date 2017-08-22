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

        //Listeners
        this.game.getEventManager().registerListeners(this, new MagicoListener(this.plugin));
        SpellType[] spellTypes = SpellList.ALL;
        for (SpellType spellType : spellTypes) {
            if (spellType.getListener() != null) {
                this.game.getEventManager().registerListeners(this, spellType.getListener());
            }
        }

        Database db = new Database();
        StructureLocation location = new StructureLocation(0, 1, 2, 3);
        Connection con = null;
        try {
            con = db.getConnection();
            DatabaseAccessObject<StructureLocation> locationDao = new DatabaseAccessObject<>(con, StructureLocation.class);
            locationDao.createTable();
        } catch (SQLException ex) {
            logger.error("DB error", ex);
        } finally {
            SQLUtils.closeQuietly(con);
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
