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
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;

import static net.cobaltium.magico.utils.ScoreboardUtils.SetScoreboardMinimal;

@ParentCommand(parent = UserCommand.class)
@Command(aliases = {"displaymana", "manadisplay", "md"}, permission = "magico.commands.user.displaymana")
public class UserDisplayManaCommand extends BaseCommandExecutor {

    @Override
    public CommandSpec.Builder getCommandSpecBuilder() {
        return super.getCommandSpecBuilder()
                .description(Text.of("Sets persistent mana display on or off."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("state"))));
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> state_ = args.getOne(Text.of("state"));
        if (state_.isPresent() && src instanceof Player) {
            String state = state_.get().toLowerCase();
            Player player = (Player)src;
            if (state.matches("true") || state.matches("on")) {
                MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
                userData.setDisplayMana(true);
                player.offer(userData);
                SetScoreboardMinimal(player, Optional.empty());
                player.sendMessage(Text.builder()
                        .append(Text.of("Mana display on."))
                        .color(TextColors.GRAY)
                        .style(TextStyles.ITALIC).build());
                return CommandResult.success();
            } else if (state.matches("false") || state.matches("off")) {
                MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
                userData.setDisplayMana(false);
                player.offer(userData);
                Scoreboard sb = Scoreboard.builder().build();
                player.setScoreboard(sb);
                player.sendMessage(Text.builder()
                        .append(Text.of("Mana display off."))
                        .color(TextColors.GRAY)
                        .style(TextStyles.ITALIC).build());
                return CommandResult.success();
            }
        }
        return CommandResult.empty();
    }

}