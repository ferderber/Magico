package net.cobaltium.magico.spells;

import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;

public class Transmutation implements Spell {

    @Override
    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> ray = BlockRay.from(player)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .distanceLimit(4).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Extent box = hit.getExtent().getExtentView(hit.getBlockPosition().add(-2, 0, -2), hit.getBlockPosition().add(2, 0, 2));
            MagicoUserData userData = player.get(MagicoUserData.class).get();
            for (Entity e : box.getEntities()) {
                if (e.getType() == EntityTypes.ITEM) {
                    Item item = (Item) e;
                    if (item.getItemType() == ItemTypes.DIAMOND) {
                        item.remove();
                        userData.modifyMana(100 * item.item().get().getQuantity());
                        player.offer(userData);
                        ParticleEffect effect = ParticleEffect.builder()
                                .type(ParticleTypes.SPELL)
                                .quantity(50).build();
                        player.playSound(SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP, player.getLocation().getPosition(), 1);
                        player.spawnParticles(effect, player.getLocation().getPosition());
                    }
                }
            }
        }
    }

    @Override
    public int getManaCost() {
        return 5;
    }
}
