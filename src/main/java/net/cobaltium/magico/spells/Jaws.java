package net.cobaltium.magico.spells;

import com.flowpowered.math.imaginary.Quaterniond;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Jaws implements Spell {

    private final int MANA_COST = 15;

    @Override
    public void handle(EventContext e, PluginContainer plugin, Player player) {
        Vector3d rotation = player.getHeadRotation();
        final Vector3d direction = Quaterniond.fromAxesAnglesDeg(0, -rotation.getY(), -rotation.getZ()).getDirection();

        for(int i=1; i<=5; i++) {
            Location<World> spawnStart = player.getLocation().sub(direction.mul(-(i*2)));
            spawnFangsAtGround(spawnStart.add(0f, 5f, 0f));
        }
    }

    private void spawnFangsAtGround(Location<World> start) {
        BlockRay<World> ray = BlockRay.from(start)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .direction(new Vector3d(0f, -1f, 0f))
                .distanceLimit(12).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();

        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            final BlockType hitType = hit.getLocation().getBlockType();

            if (hitType != BlockTypes.AIR && hitType != BlockTypes.WATER && hitType != BlockTypes.LAVA) {
                World world = hit.getExtent();
                Vector3d position = hit.getPosition();
                Entity fangs = world.createEntity(EntityTypes.EVOCATION_FANGS, position);
                world.spawnEntity(fangs);
            }
        }
    }

    @Override
    public int getManaCost() { return MANA_COST; }
}
