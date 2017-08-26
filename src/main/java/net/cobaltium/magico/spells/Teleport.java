package net.cobaltium.magico.spells;

import org.spongepowered.api.data.property.block.PassableProperty;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Teleport implements Spell {

    @Override
    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> ray = BlockRay.from(player)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .distanceLimit(100).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Location<World> location = hit.getLocation();
            try {
                player.setLocation(getEmptySpot(location, 5));
            } catch (Exception ex) {
                player.sendMessage(Text.of("Teleport auto-climb position exceeds limit of 5 blocks"));
            }
        }
    }

    //Find spot the player fits in
    private Location<World> getEmptySpot(Location<World> location) {
        Location<World> locationHead = location.add(0, 1, 0);
        Optional<PassableProperty> passableHead = locationHead.getProperty(PassableProperty.class);
        Optional<PassableProperty> passable = location.getProperty(PassableProperty.class);
        if (passableHead.isPresent() && passableHead.get().getValue() && passable.isPresent() && passable.get().getValue()) {
            return location;
        } else {
            return getEmptySpot(locationHead);
        }
    }

    private Location<World> getEmptySpot(Location<World> location, int limit) throws Exception {
        if (limit > 0) {
            Location<World> locationHead = location.add(0, 1, 0);
            Optional<PassableProperty> passableHead = locationHead.getProperty(PassableProperty.class);
            Optional<PassableProperty> passable = location.getProperty(PassableProperty.class);
            if (passableHead.isPresent() && passableHead.get().getValue() && passable.isPresent() && passable.get().getValue()) {
                return location;
            } else {
                return getEmptySpot(locationHead, --limit);
            }
        } else {
            throw new Exception(); //TODO: Create custom exception for spell runtime problems
        }
    }

    @Override
    public int getManaCost() {
        return 1;
    }
}

