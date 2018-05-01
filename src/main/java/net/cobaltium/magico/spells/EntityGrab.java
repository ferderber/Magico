package net.cobaltium.magico.spells;

import com.flowpowered.math.vector.Vector3d;
import net.cobaltium.magico.tasks.GrabTask;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class EntityGrab extends ToggleableSpell {

    Task t;

    @Override
    public void toggleOn(PluginContainer plugin, Player player) {
        Optional<BlockRayHit<World>> hit_ = BlockRay.from(player)
                .stopFilter(BlockRay.continueAfterFilter(h -> hasEntity(h, player), 1))
                .distanceLimit(10).build().end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Vector3d lookPos = hit.getPosition();
            Collection<Entity> entities =
                    hit.getExtent().getEntities(entity -> entity != player && entity.getBoundingBox().get().expand(0, 1, 0).contains(lookPos));
            Optional<Entity> first_ = entities.stream().findFirst();
            if (first_.isPresent()) {
                t = Task.builder().interval(100, TimeUnit.MILLISECONDS)
                        .execute(new GrabTask(player.getUniqueId(), first_.get().getUniqueId()))
                        .submit(plugin);
            }
        }
    }

    @Override
    public void toggleOff(PluginContainer plugin, Player player) {
        if (t != null) {
            t.cancel();
        }

    }

    private boolean hasEntity(BlockRayHit<World> hit, Player player) {
        Vector3d lookPos = hit.getPosition();
        Collection<Entity> entities = hit.getExtent()
                .getEntities(entity -> entity != player && (entity.getBoundingBox().get().expand(0, 1, 0).contains(lookPos)));
        return entities.isEmpty() && hit.getLocation().getBlock().getType() == BlockTypes.AIR;
    }
}
