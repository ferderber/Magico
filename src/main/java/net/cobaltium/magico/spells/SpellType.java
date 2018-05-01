package net.cobaltium.magico.spells;

import net.cobaltium.magico.spells.listeners.FireballListener;
import net.cobaltium.magico.spells.listeners.SpellListener;

import java.util.Optional;

public enum SpellType {
    FIREBALL(0, "Fireball", new Fireball(), new FireballListener(), "magico.spells.fireball"),
    ICE_WALL(1, "Ice Wall", new IceWall(), null, "magico.spells.icewall"),
    TELEPORT(2, "Teleport", new Teleport(), null, "magico.spells.teleport"),
    LEVITATE(3, "Levitate", new Levitate(), null, "magico.spells.levitate"),
    TRANSMUTATION(4, "Transmutation", new Transmutation(), null, "magico.spells.transmutation"),
    JAWS(5, "Jaws", new Jaws(), null, "magico.spells.jaws"),
    ENTITYGRAB(6, "Entity Grab", new EntityGrab(), null, "magico.spells.entitygrab");

    private int spellId;
    private String spellName;
    private Spell spell;
    private SpellListener listener;
    private String permission;

    SpellType(int spellId, String spellName, Spell spell, SpellListener listener, String permission) {
        this.spellId = spellId;
        this.spellName = spellName;
        this.spell = spell;
        this.listener = listener;
        this.permission = permission;
    }

    public static Optional<SpellType> getById(int id) {
        for (SpellType spellType : values()) {
            if (spellType.spellId == id) {
                return Optional.of(spellType);
            }
        }
        return Optional.empty();
    }

    public static Optional<SpellType> getByName(String spellName) {
        for (SpellType spellType : values()) {
            if (spellType.spellName.replace(" ", "").equalsIgnoreCase(spellName)) {
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

    public String getSpellKey() {
        return spellName.replace(" ", "");
    }

    public Spell getSpell() {
        return spell;
    }

    public SpellListener getListener() {
        return listener;
    }

    public String getPermission() {
        return permission;
    }
}
