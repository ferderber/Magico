package net.cobaltium.magico.db.tables;

public class StructureLocation {

    @DataColumn(primaryKey = true)
    private long id;
    @DataColumn
    private int type;
    @DataColumn
    private int x;
    @DataColumn
    private int y;
    @DataColumn
    private int z;

    public StructureLocation() {
    }

    public StructureLocation(int type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
