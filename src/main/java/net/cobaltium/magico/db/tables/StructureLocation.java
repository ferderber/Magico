package net.cobaltium.magico.db.tables;

import com.flowpowered.math.vector.Vector3i;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "structures")
public class StructureLocation {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField
    private int type;
    @DatabaseField
    private int x;
    @DatabaseField
    private int y;
    @DatabaseField
    private int z;

    public StructureLocation() {
    }

    public StructureLocation(int type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i getBlockLocation() {
        return new Vector3i(x, y, z);
    }

}
