package net.cobaltium.magico.structures;


import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.Location;

public abstract class Structure {

    protected Location location;
    protected BlockState[][][] structure;

    /**
     * Initializes a structure.
     * @param location - Center of the structure
     * @param structure - The blocks of the structure (currently only cubes are accepted)
     */
    public Structure(Location location, BlockState[][][] structure) {
        this.location = location;
        this.structure = structure;
    }

    /**
     * Renders the structure centered around the location property.
     */
    public void render() {
        int half = structure.length / 2;

        for(int x = -half; x <= half; x++) {
            for(int y = -half; y <= half; y++) {
                for(int z = -half; z <= half; z++) {
                    Location l = location.add(x,y,z);
                    l.setBlock(structure[x][y][z]);
                }
            }
        }
    }

}
