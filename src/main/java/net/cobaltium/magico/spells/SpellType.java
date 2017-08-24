package net.cobaltium.magico.spells;

import net.cobaltium.magico.spells.listeners.FireballListener;
import net.cobaltium.magico.spells.listeners.SpellListener;

import java.util.Optional;

public enum SpellType {
    FIREBALL(0, "Fireball", new Fireball(), new FireballListener()),
    ICE_WALL(1, "Ice Wall", new IceWall(), null),
    TELEPORT(2, "Teleport", new Teleport(), null);

    private int spellId;
    private String spellName;
    private Spell spell;
    private SpellListener listener;

    SpellType(int spellId, String spellName, Spell spell, SpellListener listener) {
        this.spellId = spellId;
        this.spellName = spellName;
        this.spell = spell;
        this.listener = listener;
    }

    public static Optional<SpellType> getById(int id) {
        for (SpellType spellType : values()) {
            if (spellType.spellId == id) {
                return Optional.of(spellType);
            }
        }
        return Optional.empty();
    }

    public int getSpellId() {
        return spellId;
    }

    public String getSpellName() {
        return spellName;
    }

    public Spell getSpell() {
        return spell;
    }

    public SpellListener getListener() {
        return listener;
    }
}
