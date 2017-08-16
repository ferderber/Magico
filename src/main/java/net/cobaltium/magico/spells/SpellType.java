package net.cobaltium.magico.spells;

import net.cobaltium.magico.spells.listeners.SpellListener;

public class SpellType {

    private SpellListener spellListener;
    private String name;
    private String key;

    public SpellType(String name, String key, SpellListener listener) {
        this.name = name;
        this.key = key;
        this.spellListener = listener;
    }
    public SpellListener getListener() {
        return spellListener;
    }
}
