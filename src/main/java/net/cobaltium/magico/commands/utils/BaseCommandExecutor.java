package net.cobaltium.magico.commands.utils;

import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public abstract class BaseCommandExecutor implements CommandExecutor {

    public CommandSpec.Builder getCommandSpecBuilder() {
        return CommandSpec.builder()
                .executor(this);
    }
}
