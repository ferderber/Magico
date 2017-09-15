package net.cobaltium.magico.spells;

import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class ToggleableSpell implements Spell {

    @Override
    public void handle(EventContext e, PluginContainer plugin, Player player) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        if (userData.isCastingSpell()) {
            userData.setCastingSpell(false);
            DataTransactionResult result = player.offer(userData);
            toggleOff(plugin, player);
        } else {
            userData.setCastingSpell(true);
            DataTransactionResult result = player.offer(userData);
            toggleOn(plugin, player);
        }
    }

    public abstract void toggleOn(PluginContainer plugin, Player player);

    public abstract void toggleOff(PluginContainer plugin, Player player);

    @Override
    public int getManaCost() {
        return 0;
    }
}
