package net.cobaltium.magico.spells;

import net.cobaltium.magico.spells.listeners.FireballListener;

import java.util.Arrays;
import java.util.List;

public class SpellList {
    public SpellList() {}

    public static final SpellType FIREBALL = new SpellType("Fireball", "fireball", new FireballListener());

    public static List<SpellType> ALL = Arrays.asList(FIREBALL);
}
