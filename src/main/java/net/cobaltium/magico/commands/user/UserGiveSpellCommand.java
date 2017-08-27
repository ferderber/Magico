package net.cobaltium.magico.commands.user;

import net.cobaltium.magico.commands.utils.BaseCommandExecutor;
import net.cobaltium.magico.commands.utils.Command;
import net.cobaltium.magico.commands.utils.ParentCommand;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.DatabaseAccessObject;
import net.cobaltium.magico.db.tables.UserSpells;
import net.cobaltium.magico.db.utils.SQLUtils;
import net.cobaltium.magico.spells.SpellType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.sql.Connection;
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
                Database db = new Database();
                Connection con = null;
                try {
                    con = db.getConnection();
                    DatabaseAccessObject<UserSpells> userDao = new DatabaseAccessObject<>(con, UserSpells.class);
                    userDao.createTable();
                    UserSpells userSpell = new UserSpells(player.getUniqueId(), spellType.getSpellId(), 1);
                    userDao.insert(userSpell);
                } catch (SQLException ex) {
                    player.sendMessage(Text.of("Error adding spell"));
                } catch (IllegalAccessException ex) {

                } finally {
                    SQLUtils.closeQuietly(con);
                }
                src.sendMessage(Text.of("" + spellType.getSpellName()));
                return CommandResult.success();
            }
        }
        return CommandResult.empty();
    }
}
