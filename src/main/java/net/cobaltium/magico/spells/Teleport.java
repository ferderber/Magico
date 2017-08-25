package net.cobaltium.magico.spells;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Teleport implements Spell {

    @Override
    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> ray = BlockRay.from(player).stopFilter(worldBlockRayHit ->
                worldBlockRayHit.getLocation().getBlockType() == BlockTypes.AIR)
                .distanceLimit(100).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Location<World> location = hit.getLocation();
            location = location.setPosition(location.getPosition().add(0, 1, 0));
            player.setLocation(location);
        }
    }

    @Override
    public int getManaCost() {
        return 1;
    }
}

