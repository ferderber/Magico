package net.cobaltium.magico.schematics;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.BlockPalette;

import java.util.Optional;

public class Warp implements MultiBlockStructure {
    private static final int WIDTH = 4;
    private static final int HEIGHT = 1;
    private static final int LENGTH = 5;
    private static final BlockPalette palette = new WarpPaletteType().create();
    private static final int[] warp = new int[]{
            1, 1, 1, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 1, 1, 1
    };

    public boolean isStructure(Location<World> location) {
        Location<World> start = location.add(-WIDTH + 1, 0, -LENGTH + 1);
        Location<World> curr;
        Vector3i startPosition = location.getBlockPosition();
        boolean isValid = false;
        for (int x = 0; x < WIDTH * 2; x++) {
            isValid = false;
            for (int z = 0; z < LENGTH * 2; z++) {
                curr = start.add(x, 0, z);
                Vector3i corner = curr.getBlockPosition().add(WIDTH, 0, LENGTH);
                if (startPosition.getX() <= corner.getX() && startPosition.getX() >= curr.getBlockPosition().getX() && startPosition.getZ() <= corner.getZ() && startPosition.getZ() >= curr.getBlockPosition().getZ()) {
                    for (int k = 0; k < 2; k++) {
                        Optional<Integer> id_;
                        Location<World> loc = curr;

                        for (int j = 0; j < warp.length; j++) {
                            id_ = palette.get(loc.getBlock());
                            if ((id_.isPresent() && warp[j] == id_.get()) || (warp[j] == 0 && !id_.isPresent())) {
                                isValid = true;
                            } else {
                                isValid = false;
                                break;
                            }
                            if (k == 0) {
                                loc = curr.add((j + 1) % WIDTH, 0 % HEIGHT, (j + 1) / WIDTH);
                            } else {
                                loc = curr.add((j + 1) / WIDTH, 0 % HEIGHT, (j + 1) % WIDTH);
                            }
                        }
                    }
                }
                if (isValid) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int[] getStructureData() {
        return warp;
    }

    @Override
    public BlockPalette getPalette() {
        return palette;
    }

}
