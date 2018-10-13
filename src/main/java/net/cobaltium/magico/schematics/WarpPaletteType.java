package net.cobaltium.magico.schematics;

import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.world.schematic.BlockPalette;
import org.spongepowered.api.world.schematic.BlockPaletteType;

public class WarpPaletteType implements BlockPaletteType {


    @Override
    public BlockPalette create() {
        return new WarpPalette();
    }

    @Override
    public CatalogKey getKey() {
        return CatalogKey.builder().namespace("magico").value("warppalette").build();
    }

    @Override
    public String getName() {
        return "Magico Warp Palette";
    }
}
