package net.cobaltium.magico.structures;

import net.cobaltium.magico.MagicoKeys;
import net.cobaltium.magico.data.ImmutableMagicoStructureData;
import net.cobaltium.magico.data.MagicoStructureData;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

public class Bookcase extends Structure {
    public Bookcase(Location location) {
        super(location, new BlockState[1][1][1]);
        structure[0][0][0] = BlockState.builder().blockType(BlockTypes.BOOKSHELF).add(MagicoKeys.IS_STRUCTURE_BLOCK, true).build();
    }

}
