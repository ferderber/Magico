package net.cobaltium.magico.commands.user;

import net.cobaltium.magico.commands.utils.BaseCommandExecutor;
import net.cobaltium.magico.commands.utils.Command;
import net.cobaltium.magico.commands.utils.ParentCommand;
import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

@ParentCommand(parent = UserCommand.class)
@Command(aliases = {"setmana", "mana"}, permission = "magico.commands.user.mana")
public class UserSetManaCommand extends BaseCommandExecutor {

    @Override
    public CommandSpec.Builder getCommandSpecBuilder() {
        return super.getCommandSpecBuilder()
                .description(Text.of("Set a players mana"))
                .arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("amount"))));
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> amount_ = args.getOne(Text.of("amount"));
        if (amount_.isPresent() && src instanceof Player) {
            Player player = (Player) src;
            int amount = amount_.get();
            MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
            userData.setMana(amount);
            player.offer(userData);
            src.sendMessage(Text.of("Set mana to " + amount));
            return CommandResult.success();
        }
        return CommandResult.empty();
    }
}

