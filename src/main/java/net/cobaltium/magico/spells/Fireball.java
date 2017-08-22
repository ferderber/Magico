package net.cobaltium.magico.spells;

import org.spongepowered.api.entity.EntityTypes;

public class Fireball extends ProjectileSpell {

    private final int MANA_COST = 10;

    public Fireball() {
        super(EntityTypes.FIREBALL);
    }

    @Override
    public int getManaCost() {
        return MANA_COST;
    }
}
