package net.cobaltium.magico.spells;

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
        BlockRay<World> blockRay = BlockRay.from(player).stopFilter(BlockRay.onlyAirFilter()).distanceLimit(100).build();
        Optional<BlockRayHit<World>> hit_ = blockRay.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Location<World> location = hit.getLocation();
            location.setPosition(location.getPosition().add(0, 2, 0));
            player.setLocation(location);
        }
    }

    @Override
    public int getManaCost() {
        return 5;
    }
}
