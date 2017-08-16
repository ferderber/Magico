package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;

public interface Spell {
    void handle(Player player);
}
