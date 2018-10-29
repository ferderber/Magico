package net.cobaltium.magico.spells;

import com.google.inject.Inject;
import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.tasks.EffectTask;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public abstract class ExpiringSpell implements Spell {

    private int time;
    private TimeUnit unit;

    @Inject
    private Logger logger;

    public ExpiringSpell(int time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public void handle(EventContext e, PluginContainer plugin, Player player) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        if (userData.isCastingSpell()) {
            logger.debug("Spell cast overlap");
        } else {
            userData.setCastingSpell(true);
            player.offer(userData);
            Task.builder()
                    .interval(time, unit)
                    .execute(new EffectTask(player.getUniqueId(), this))
                    .submit(plugin);
        }
    }

    public abstract void applyEffect(Player p);

    @Override
    public int getManaCost() {
        return 0;
    }
}
