package net.cobaltium.magico.structures;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class ManaCrystal {

//    private final int crystalBlockAmount = 1029;
//    private final int maxDisCenter = 3;

    public ManaCrystal(Location<World> location, Player player) {

        Location<World> start = location.add(0, 23, 0);

        for (int i = 0; i < 21; i++) {
            for (int j = -3; j <= 3; j++ ) {
                for (int k = -3; k <= 3; k++) {
                    Location<World> currentBlock = start.add(j, -i, k);

                    int absValJ = Math.abs(j);
                    int absValK = Math.abs(k);

                    if (i == 0 || i == 20) {

                        if((absValJ + absValK) == 0) {
                            currentBlock.setBlock(BlockState.builder().blockType(BlockTypes.STAINED_GLASS).add(Keys.DYE_COLOR, DyeColors.CYAN).build());
                        }
                    } else if(i == 1 || i == 19) {

                        if ((absValJ + absValK) == 1) {
                            currentBlock.setBlock(BlockState.builder().blockType(BlockTypes.STAINED_GLASS).add(Keys.DYE_COLOR, DyeColors.CYAN).build());
                        } else if((absValJ + absValK) == 0) {
                            currentBlock.setBlockType(BlockTypes.PACKED_ICE, BlockChangeFlag.NONE);
                        }
                    } else if(i == 2 || i == 18) {

                        if ((absValJ + absValK) == 2) {
                            currentBlock.setBlock(BlockState.builder().blockType(BlockTypes.STAINED_GLASS).add(Keys.DYE_COLOR, DyeColors.CYAN).build());
                        } else if((absValJ + absValK) < 2) {
                            currentBlock.setBlockType(BlockTypes.PACKED_ICE, BlockChangeFlag.NONE);
                        }
                    } else if(i == 3 || i == 17) {

                        if ((absValJ + absValK) == 3  || ((absValJ + absValK == 2) && (j == 0 || k == 0))) {
                            currentBlock.setBlock(BlockState.builder().blockType(BlockTypes.STAINED_GLASS).add(Keys.DYE_COLOR, DyeColors.CYAN).build());
                        } else if ((absValJ + absValK) < 3) {
                            currentBlock.setBlockType(BlockTypes.PACKED_ICE, BlockChangeFlag.NONE);
                        }
                    } else {

                        if ((absValJ + absValK) == 4 || ((absValJ + absValK == 3) && (j == 0 || k == 0))) {
                            currentBlock.setBlock(BlockState.builder().blockType(BlockTypes.STAINED_GLASS).add(Keys.DYE_COLOR, DyeColors.CYAN).build());
                        } else if ((absValJ + absValK) < 4) {
                            currentBlock.setBlockType(BlockTypes.PACKED_ICE, BlockChangeFlag.NONE);
                        }
                    }
                }
            }
        }
    }
}