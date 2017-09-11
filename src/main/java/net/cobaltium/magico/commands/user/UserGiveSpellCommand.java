package net.cobaltium.magico.commands.user;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.types.UuidType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.cobaltium.magico.commands.utils.BaseCommandExecutor;
import net.cobaltium.magico.commands.utils.Command;
import net.cobaltium.magico.commands.utils.ParentCommand;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.UserSpells;
import net.cobaltium.magico.spells.SpellType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.sql.SQLException;
import java.util.Optional;

@ParentCommand(parent = UserCommand.class)
@Command(aliases = {"givespell", "spell", "addspell"}, permission = "magico.commands.user.givespell")
public class UserGiveSpellCommand extends BaseCommandExecutor {

    @Override
    public CommandSpec.Builder getCommandSpecBuilder() {
        return super.getCommandSpecBuilder()
                .description(Text.of("Set a players mana"))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("spell"))));
    }

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> spell_ = args.getOne(Text.of("spell"));
        if (spell_.isPresent() && src instanceof Player) {
            Player player = (Player) src;
            String spell = spell_.get();
            Optional<SpellType> spellType_ = SpellType.getByName(spell);
            if (spellType_.isPresent()) {
                SpellType spellType = spellType_.get();
                ConnectionSource con = null;
                try {
                    con = Database.getConnection();
                    Dao<UserSpells, UuidType> userDao = DaoManager.createDao(con, UserSpells.class);
                    if (!userDao.isTableExists()) {
                        TableUtils.createTable(userDao);
                    }
                    UserSpells userSpell = new UserSpells(player.getUniqueId(), spellType.getSpellId(), 1);
                    userDao.create(userSpell);
                } catch (SQLException ex) {
                    player.sendMessage(Text.of("Error adding spell"));
                } finally {
                    con.closeQuietly();
                }
                src.sendMessage(Text.of("" + spellType.getSpellName()));
                return CommandResult.success();
            } else {
                Text.Builder spellList = Text.builder()
                        .append(Text.of("Unknown spell"))
                        .append(Text.NEW_LINE)
                        .append(Text.of("Available Spells: "));
                SpellType[] spellTypes = SpellType.values();
                for (int i = 0; i < spellTypes.length; i++) {
                    spellList.append(Text.NEW_LINE).append(Text.of(spellTypes[i].getSpellKey()));
                }
                player.sendMessage(spellList.build());
            }
        }
        return CommandResult.empty();
    }
}
