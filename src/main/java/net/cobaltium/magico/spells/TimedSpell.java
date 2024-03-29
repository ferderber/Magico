package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public abstract class TimedSpell extends ToggleableSpell {
    private TimeUnit timeUnit;
    private long time;

    public TimedSpell(long time, TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        this.time = time;
    }

    @Override
    public void handle(EventContext e, PluginContainer plugin, Player player) {
        super.handle(e, plugin, player);
        Task.builder()
                .delay(time, timeUnit)
                .execute(() -> super.handle(e, plugin, player))
                .name("Timed Spell")
                .submit(plugin);
    }
}
