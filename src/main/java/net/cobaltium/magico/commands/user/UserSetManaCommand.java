package net.cobaltium.magico.commands.user;

import net.cobaltium.magico.commands.utils.Command;
import net.cobaltium.magico.commands.utils.ParentCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

@ParentCommand(parent = UserCommand.class)
@Command(aliases = {"setmana", "mana"}, permission = "magico.commands.user.mana")
public class UserSetManaCommand implements CommandExecutor {

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return CommandResult.empty();
    }
}

