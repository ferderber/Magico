package net.cobaltium.magico.tasks;

import com.flowpowered.math.vector.Vector3d;
import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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
                player.offer(Keys.IS_FLYING, false);
                //if the player is below 200, levitate
                if (player.getLocation().getY() < 200) {
                    player.setVelocity(player.getVelocity().mul(1, 0, 1).add(0, .15, 0));
                } else {
                    //if they are above 200, end the spell
                    userData.setCastingSpell(false);
                    player.offer(userData);
                    //negate accumulated fall damage
                    player.offer(Keys.FALL_DISTANCE, 0.0f);
                    player.offer(Keys.HAS_GRAVITY, true);
                    if (player.gameMode().get() != GameModes.CREATIVE) {
                        player.offer(Keys.CAN_FLY, false);
                    }
                    task.cancel();
                }
            } else {
                task.cancel();
            }
        }
    }

}
