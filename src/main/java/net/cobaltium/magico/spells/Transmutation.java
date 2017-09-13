package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Transmutation implements Spell {

    @Override
    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> ray = BlockRay.from(player)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .distanceLimit(100).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();

    }


    @Override
    public int getManaCost() {
        return 5;
    }
}
