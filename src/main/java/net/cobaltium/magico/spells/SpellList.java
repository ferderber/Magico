package net.cobaltium.magico.spells;

import net.cobaltium.magico.spells.listeners.FireballListener;

public class SpellList {
    public SpellList() {}

    public static final SpellType FIREBALL = new SpellType("Fireball", Fireball.class.getName(), new FireballListener());
    public static final SpellType ICE_WALL = new SpellType("Ice Wall", IceWall.class.getName(), new FireballListener());

    public static final SpellType[] ALL = new SpellType[]{FIREBALL, ICE_WALL};
}
