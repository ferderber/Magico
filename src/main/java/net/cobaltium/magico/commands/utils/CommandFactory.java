package net.cobaltium.magico.commands.utils;

import org.spongepowered.api.Sponge;
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

    public void registerCommands(Object plugin, BaseCommandExecutor... executors) {
        List<BaseCommandExecutor> parentCommands = new ArrayList<>();
        Map<String, List<BaseCommandExecutor>> childMap = new HashMap<>();
        for (int i = 0; i < executors.length; i++) {
            Class commandClass = executors[i].getClass();
            ParentCommand parentAnnotation = (ParentCommand) commandClass.getAnnotation(ParentCommand.class);
            if (parentAnnotation != null) {
                String parentName = parentAnnotation.parent().getName();
                List<BaseCommandExecutor> childCommands = childMap.get(parentName);
                if (childCommands == null) {
                    childCommands = new ArrayList<>();
                }
                childCommands.add(executors[i]);
                childMap.put(parentName, childCommands);
            } else {
                parentCommands.add(executors[i]);
            }
        }
        for (BaseCommandExecutor parent : parentCommands) {
            CommandSpec.Builder builder = parent.getCommandSpecBuilder();
            Class parentClass = parent.getClass();
            Command commandAnnotation = (Command) parentClass.getAnnotation(Command.class);

            builder.permission(commandAnnotation.permission());

            List<BaseCommandExecutor> children = childMap.get(parentClass.getName());
            if (children != null && !children.isEmpty()) {
                children.forEach(child -> {
                    Class childClass = child.getClass();
                    Command childCommandAnnotation = (Command) childClass.getAnnotation(Command.class);
                    CommandSpec childSpec = child.getCommandSpecBuilder()
                            .permission(childCommandAnnotation.permission()).build();
                    builder.child(childSpec, childCommandAnnotation.aliases());
                });
            }

            Sponge.getCommandManager().register(plugin, builder.build(), commandAnnotation.aliases());

        }

    }

}
