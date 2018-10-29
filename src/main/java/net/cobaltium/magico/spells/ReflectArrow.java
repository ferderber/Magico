package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.living.player.Player;

import java.util.concurrent.TimeUnit;

public class ReflectArrow extends ExpiringSpell {
    public ReflectArrow() {
        super(10, TimeUnit.SECONDS);
    }
    public void applyEffect(Player p) {

    }
}
