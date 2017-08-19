package net.cobaltium.magico.db.tables;

public class StructureLocation extends SqlTable {

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


}
