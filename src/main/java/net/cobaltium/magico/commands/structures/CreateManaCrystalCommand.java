package net.cobaltium.magico.commands.structures;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.types.UuidType;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.support.ConnectionSource;
import net.cobaltium.magico.commands.utils.BaseCommandExecutor;
import net.cobaltium.magico.commands.utils.Command;
import net.cobaltium.magico.commands.utils.ParentCommand;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.StructureLocation;
import net.cobaltium.magico.structures.ManaCrystal;
import net.cobaltium.magico.structures.StructureType;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.SQLException;

@ParentCommand(parent = CreateStructureCommand.class)
@Command(aliases = {"manacrystal", "mana", "mc"}, permission = "magico.commands.user")
public class CreateManaCrystalCommand extends BaseCommandExecutor{

    @Inject
    private Logger logger;

    public CreateManaCrystalCommand() {
    }


    @Override
    public CommandSpec.Builder getCommandSpecBuilder() {
        return super.getCommandSpecBuilder()
                .description(Text.of("Creates a ManaCrystal"));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;
            Location<World> playerLocation = player.getLocation();
            ConnectionSource con = null;
            try {
                con = Database.getConnection();
                Dao<StructureLocation, UuidType> structureDao = DaoManager.createDao(con, StructureLocation.class);
                if (!structureDao.isTableExists()) {
                    TableUtils.createTable(structureDao);
                }
                StructureLocation location = new StructureLocation(StructureType.MANACRYSTAL, playerLocation.getBlockX(), playerLocation.getBlockY(),
                        playerLocation.getBlockZ());
                structureDao.create(location);
            } catch (SQLException ex) {
                player.sendMessage(Text.of("Error adding Mana Crystal"));
                logger.error(ex.getMessage());
            } finally {
                con.closeQuietly();
            }

            ManaCrystal crystal = new ManaCrystal(playerLocation, player);

        } else if (src instanceof ConsoleSource) {}

        return CommandResult.success();
    }
}