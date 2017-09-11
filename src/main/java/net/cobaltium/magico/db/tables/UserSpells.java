package net.cobaltium.magico.db.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.cobaltium.magico.spells.SpellType;

import java.util.Optional;
import java.util.UUID;

@DatabaseTable(tableName = "userspells")
public class UserSpells {

    @DatabaseField(uniqueCombo = true)
    private UUID user_id;

    @DatabaseField(uniqueCombo = true)
    private int spell_id;

    @DatabaseField
    private int level;

    public UserSpells() {

    }

    public UserSpells(UUID user_id, int spell_id, int level) {
        this.user_id = user_id;
        this.spell_id = spell_id;
        this.level = level;
    }

    public Optional<SpellType> getSpellType() {
        return SpellType.getById(spell_id);
    }
}
