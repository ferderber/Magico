package net.cobaltium.magico.schematics;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.BlockPalette;

public interface MultiBlockStructure {
    public int[] getStructureData();
    public BlockPalette getPalette();
    public boolean isStructure(Location<World> location);
}
