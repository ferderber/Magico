package net.cobaltium.magico.spells;

import net.cobaltium.magico.tasks.LevitateTask;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class Levitate extends ToggleableSpell {

    public void toggleOn(PluginContainer plugin, Player player) {
        player.offer(Keys.HAS_GRAVITY, false);
        player.offer(Keys.CAN_FLY, true);
        //negate accumulated fall damage
        player.offer(Keys.FALL_DISTANCE, 0.0f);
        Task.builder().interval(200, TimeUnit.MILLISECONDS)
                .execute(new LevitateTask(player.getUniqueId()))
                .submit(plugin);
    }

    public void toggleOff(PluginContainer plugin, Player player) {
        player.offer(Keys.HAS_GRAVITY, true);
        player.offer(Keys.IS_FLYING, false);
        if (player.gameMode().get() != GameModes.CREATIVE) {
            player.offer(Keys.CAN_FLY, false);
        }
    }

    @Override
    public int getManaCost() {
        return 5;
    }
}
