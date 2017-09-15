package net.cobaltium.magico.spells;

import net.cobaltium.magico.data.MagicoUserData;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;

public class Transmutation implements Spell {

    @Override
    public void handle(EventContext e, PluginContainer plugin, Player player) {
        Optional<BlockSnapshot> block_ = e.get(EventContextKeys.BLOCK_HIT);
        if (block_.isPresent()) {
            BlockSnapshot block = block_.get();
            transmuteRecipe(block, player);
        } else {
            transmuteItems(player);
        }
    }

    private void transmuteRecipe(BlockSnapshot block, Player player) {
        if (block.getState().getType() == BlockTypes.BLACK_SHULKER_BOX) {
            TileEntityCarrier box =  (TileEntityCarrier) block.getLocation().get().getTileEntity().get();
            TileEntityInventory<TileEntityCarrier> inventory = box.getInventory();
            if(inventory.contains(ItemTypes.APPLE)) {
                inventory.query(ItemTypes.APPLE).slots().forEach((inv) -> player.sendMessage(Text.of(inv.poll().get().getQuantity())));
            }
        }
    }

    private void transmuteItems(Player player) {
        BlockRay<World> ray = BlockRay.from(player)
                .stopFilter(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                .distanceLimit(4).build();
        Optional<BlockRayHit<World>> hit_ = ray.end();

        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Extent box = hit.getExtent().getExtentView(hit.getBlockPosition().add(-2, 0, -2), hit.getBlockPosition().add(2, 0, 2));
            MagicoUserData userData = player.get(MagicoUserData.class).get();
            for (Entity entity : box.getEntities()) {
                if (entity.getType() == EntityTypes.ITEM) {
                    Item item = (Item) entity;
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
