package net.cobaltium.magico.commands.user;

import net.cobaltium.magico.commands.utils.BaseCommandExecutor;
import net.cobaltium.magico.commands.utils.Command;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

@Command(aliases = {"muser"}, permission = "magico.commands.user")
public class UserCommand extends BaseCommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.empty();
    }
}
