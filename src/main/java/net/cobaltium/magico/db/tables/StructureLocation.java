package net.cobaltium.magico.db.tables;

import com.flowpowered.math.vector.Vector3i;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.cobaltium.magico.structures.Structure;
import net.cobaltium.magico.structures.StructureType;

import java.util.Optional;

@DatabaseTable(tableName = "structures")
public class StructureLocation {

    @DatabaseField(generatedId = true)
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

    public StructureLocation(StructureType type, int x, int y, int z) {
        this.type = type.getStructureId();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i getBlockLocation() {
        return new Vector3i(x, y, z);
    }

    private Optional<StructureType> getStructureType() {
        return StructureType.getById(type);
    }

}
