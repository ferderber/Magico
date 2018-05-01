package net.cobaltium.magico.tasks;

import com.flowpowered.math.vector.Vector3d;
import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class GrabTask implements Consumer<Task> {

    private UUID playerId;
    private UUID entityId;

    public GrabTask(UUID playerId, UUID entityId) {
        this.playerId = playerId;
        this.entityId = entityId;
    }

    public void accept(Task task) {
        Optional<Player> player_ = Sponge.getServer().getPlayer(playerId);
        if (player_.isPresent()) {
            Player player = player_.get();
            Optional<Entity> entity_ = player.getWorld().getEntity(entityId);
            if (entity_.isPresent()) {
                Entity entity = entity_.get();
                MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
                if (userData.isCastingSpell()) {
                    Optional<BlockRayHit<World>> hit_ = BlockRay.from(player).distanceLimit(5).build().end();
                    if (hit_.isPresent()) {
                        BlockRayHit<World> hit = hit_.get();
                        Location<World> eLocation = entity.getLocation();
                        eLocation.setPosition(eLocation.getPosition().mul(hit.getDirection()));
                        eLocation.setPosition(hit.getPosition());
                        entity.setLocation(hit.getLocation());
                        entity.offer(Keys.FALL_DISTANCE, 0.0f);
                        entity.setVelocity(Vector3d.ZERO);
                    }
                } else {
                    task.cancel();
                }
            } else {
                task.cancel();
            }
        } else {
            task.cancel();
        }
    }

}

