package net.cobaltium.magico.db.tables;

import java.util.UUID;

public class UserSpells {

    @DataColumn(primaryKey = true)
    private UUID user_id;

    @DataColumn
    private int spell_id;

    public UserSpells() {

    }

    public UserSpells(UUID user_id, int spell_id) {
        this.user_id = user_id;
        this.spell_id = spell_id;
    }

}
