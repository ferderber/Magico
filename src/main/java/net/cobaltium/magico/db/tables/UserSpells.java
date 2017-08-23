package net.cobaltium.magico.db.tables;

import java.util.UUID;

public class UserSpells {

    @DataColumn(primaryKey = true)
    private UUID user_id;

    @DataColumn
    private int spell_id;

    public UserSpells() {

    }

}
