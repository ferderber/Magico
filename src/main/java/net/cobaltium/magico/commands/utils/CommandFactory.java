package net.cobaltium.magico.commands.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {

    private static CommandFactory instance = null;

    protected CommandFactory() {
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            instance = new CommandFactory();
        }
        return instance;
    }

    public void registerCommands(Object plugin, CommandExecutor... executors) {
        List<CommandExecutor> parentCommands = new ArrayList<>();
        Map<String, List<CommandExecutor>> childMap = new HashMap<>();
        for (int i = 0; i < executors.length; i++) {
            Class commandClass = executors[i].getClass();
            ParentCommand parentAnnotation = (ParentCommand) commandClass.getAnnotation(ParentCommand.class);
            if (parentAnnotation != null) {
                String parentName = parentAnnotation.parent().getName();
                List<CommandExecutor> childCommands = childMap.get(parentName);
                if (childCommands == null) {
                    childCommands = new ArrayList<>();
                }
                childCommands.add(executors[i]);
                childMap.put(parentName, childCommands);
            } else {
                parentCommands.add(executors[i]);
            }
        }
        for (CommandExecutor parent : parentCommands) {
            CommandSpec.Builder builder = CommandSpec.builder();
            Class parentClass = parent.getClass();
            Command commandAnnotation = (Command) parentClass.getAnnotation(Command.class);

            builder.executor(parent).permission(commandAnnotation.permission());

            List<CommandExecutor> children = childMap.get(parentClass.getName());

            if (!children.isEmpty()) {
                children.forEach(child -> {
                    Class childClass = child.getClass();
                    Command childCommandAnnotation = (Command) childClass.getAnnotation(Command.class);
                    CommandSpec childSpec = CommandSpec.builder().executor(child)
                            .permission(childCommandAnnotation.permission()).build();
                    builder.child(childSpec, childCommandAnnotation.aliases());
                });
            }

            Sponge.getCommandManager().register(plugin, builder.build(), commandAnnotation.aliases());

        }

    }

}
