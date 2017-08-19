package net.cobaltium.magico.spells;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class IceWall implements Spell {

    private int wallSize;

    private final int MANA_COST = 10;

    public IceWall() {
        wallSize = 3;
    }

    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> blockRay = BlockRay.from(player).stopFilter(BlockRay.onlyAirFilter()).distanceLimit(3).build();
        Optional<BlockRayHit<World>> hit_ = blockRay.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Location<World> location = hit.getLocation();
            hit.getFaces()[0].getOpposite();
            Vector3d perpLookVector = hit.getDirection().cross(0, 1, 0); //get wall direction vector
            Vector3i wallVector = Direction.getClosest(perpLookVector, Direction.Division.CARDINAL).asBlockOffset();
            Location<World>[][] wall = getWall(location, wallVector);

            Queue<BlockSnapshot> wallSnapshot = getWallSnapshot(wall);
            for (int i = 0; i < wall.length; i++) {
                for (int j = 0; j < wall[i].length; j++) {
                    if(wall[i][j] != null) {
                        wall[i][j].setBlockType(BlockTypes.PACKED_ICE, BlockChangeFlag.NONE, Cause.source(plugin).build());
                    }
                }
            }

            Task.builder().delay(2, TimeUnit.SECONDS).execute(() -> {
                while(!wallSnapshot.isEmpty()) {
                    wallSnapshot.remove().restore(true, BlockChangeFlag.NONE);
                }
            }).submit(plugin);
        }
    }

    public int getManaCost() {
        return MANA_COST;
    }

    public Location<World>[][] getWall(Location<World> center, Vector3i vec) {
        Location<World>[][] wall = new Location[wallSize][wallSize];
        int middle = wallSize / 2;
        if(wallSize % 2 == 0) {
            //two middles hopefully we don't use this
        } else {
            if(vec.getX() == 0 && vec.getY() == 1 && vec.getZ() == 0) {
                vec = vec.add(1, 0, 1);
                wall[0][0] = center.add(vec.mul(-middle, middle, -middle)); //set first position by offset from center
                for (int i = 0; i < wall.length; i++) {
                    for (int j = 0; j < wall.length; j++) {
                        wall[i][j] = wall[0][0].add(vec.mul(i, 0, -j));
                        if (wall[i][j].getBlockType() != BlockTypes.AIR) {
                            wall[i][j] = null; //Only give location of blocks that are safe to replace
                        }
                    }
                }
            }
            else {
                vec = vec.add(0, 1, 0);
                wall[0][0] = center.add(vec.mul(-middle, middle, -middle)); //set first position by offset from center
                for (int i = 0; i < wall.length; i++) {
                    for (int j = 0; j < wall.length; j++) {
                        wall[i][j] = wall[0][0].add(vec.mul(i, -j, i));
                        if (wall[i][j].getBlockType() != BlockTypes.AIR) {
                            wall[i][j] = null; //Only give location of blocks that are safe to replace
                        }
                    }
                }
            }
        }
        return wall;
    }


    public Queue<BlockSnapshot> getWallSnapshot(Location<World>[][] wall) {
        Queue<BlockSnapshot> wallSnapshot = new LinkedList();
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++){
                if(wall[i][j] != null)
                    wallSnapshot.add(wall[i][j].createSnapshot());
            }
        }
        return wallSnapshot;
    }
}
