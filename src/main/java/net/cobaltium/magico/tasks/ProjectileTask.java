package net.cobaltium.magico.tasks;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public class ProjectileTask implements Consumer<Task> {

    private Entity projectile;
    private Vector3d vector;
    private Vector3d startVector;

    public ProjectileTask(Entity entity, Vector3d vector) {
        this.projectile = projectile;
        this.startVector = projectile.getLocation().getPosition();
        this.vector = vector;
    }

    @Override
    public void accept(Task task) {
        projectile.setVelocity(vector);
        if (vector.distance(startVector) >= 100) {
            projectile.remove();
            task.cancel();
        }
    }
}
