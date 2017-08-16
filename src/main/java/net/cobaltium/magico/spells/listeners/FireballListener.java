package net.cobaltium.magico.spells.listeners;

import net.cobaltium.magico.data.MagicoProjectileData;
import org.spongepowered.api.entity.projectile.explosive.fireball.LargeFireball;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;

public class FireballListener implements SpellListener {
    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event) {
        Cause cause = event.getCause();
        Optional<LargeFireball> fireball_ = cause.first(LargeFireball.class);
        if(fireball_.isPresent()) {
            LargeFireball fireball = fireball_.get();
            Optional<MagicoProjectileData> data = fireball.get(MagicoProjectileData.class);
            if(data.isPresent() && !data.get().doesBlockDamage()) {
                event.setCancelled(true);
            }
        }
    }
}
