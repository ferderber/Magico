package net.cobaltium.magico.schematics;

import org.spongepowered.api.world.schematic.BlockPalette;
import org.spongepowered.api.world.schematic.BlockPaletteType;

public class WarpPaletteType implements BlockPaletteType {


    @Override
    public BlockPalette create() {
        return new WarpPalette();
    }

    @Override
    public String getId() {
        return "magico:warppalette";
    }

    @Override
    public String getName() {
        return "Magico Warp Palette";
    }
}
