package net.cobaltium.magico.schematics;


import org.spongepowered.api.block.BlockTypes;

public class WarpPalette extends BimapPalette {
    public WarpPalette() {
        super(2);
        this.getOrAssign(BlockTypes.OBSIDIAN.getDefaultState());
        this.getOrAssign(BlockTypes.LAPIS_BLOCK.getDefaultState());
    }
}
