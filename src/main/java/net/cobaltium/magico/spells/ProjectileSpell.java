package net.cobaltium.magico.spells;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import net.cobaltium.magico.data.MagicoProjectileData;
import net.cobaltium.magico.tasks.ProjectileTask;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;

public abstract class ProjectileSpell implements Spell {
    private EntityType entityType;

    public ProjectileSpell(EntityType type) {
        this.entityType = type;
    }

    public void handle(PluginContainer plugin, Player player) {
        World world = player.getWorld();
        Vector3d rotation = player.getHeadRotation();
        final Vector3d direction = Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()).getDirection();
        Vector3d launchSpot = player.getLocation().getPosition().add(0, 1, 0).add(direction.mul(1.5));

        Entity entity = world.createEntity(entityType, launchSpot);
        MagicoProjectileData data = entity.getOrCreate(MagicoProjectileData.class).get();
        data.setBlockDamage(false);
        entity.offer(data);
        world.spawnEntity(entity);
        Task.builder()
                .interval(200, TimeUnit.MILLISECONDS)
                .execute(new ProjectileTask(entity, direction.mul(2)))
                .submit(plugin);
    }
}
