package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;

public interface Spell {
    void handle(EventContext e, PluginContainer plugin, Player player);

    int getManaCost();
}
