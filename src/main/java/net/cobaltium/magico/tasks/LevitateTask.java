package net.cobaltium.magico.tasks;

import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class LevitateTask implements Consumer<Task> {

    private UUID playerId;

    public LevitateTask(UUID playerId) {
        this.playerId = playerId;
    }

    public void accept(Task task) {
        Optional<Player> player_ = Sponge.getServer().getPlayer(playerId);
        if (player_.isPresent()) {
            Player player = player_.get();
            MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
            if (userData.isCastingSpell()) {
                player.setVelocity(player.getVelocity().mul(1, 0, 1).add(0, .15, 0));
            } else {
                task.cancel();
            }
        }
    }

}
