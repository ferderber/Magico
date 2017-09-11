package net.cobaltium.magico.spells;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GravityData;
import org.spongepowered.api.data.manipulator.mutable.entity.VelocityData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

public class Levitate implements Spell {

    @Override public void handle(PluginContainer plugin, Player player) {
        GravityData data = player.getOrCreate(GravityData.class).get();
        data.set(Keys.HAS_GRAVITY, !data.gravity().get());
        VelocityData velocityData = player.getOrCreate(VelocityData.class).get();
        velocityData.set(Keys.VELOCITY, new Vector3d(0, .2, 0));
        player.offer(data);
        player.offer(velocityData);
    }

    @Override public int getManaCost() {
        return 5;
    }
}
