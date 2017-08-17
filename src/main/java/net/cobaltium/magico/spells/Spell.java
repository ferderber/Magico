package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

public interface Spell {
    void handle(PluginContainer plugin, Player player);
}
