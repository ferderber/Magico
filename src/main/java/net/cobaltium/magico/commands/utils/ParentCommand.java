package net.cobaltium.magico.commands.utils;

import org.spongepowered.api.command.spec.CommandExecutor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParentCommand {

    Class<? extends CommandExecutor> parent();

}
