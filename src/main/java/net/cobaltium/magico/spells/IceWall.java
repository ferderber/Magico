package net.cobaltium.magico.spells;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.BlockChangeFlag;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class IceWall implements Spell {
    public void handle(PluginContainer plugin, Player player) {
        BlockRay<World> blockRay = BlockRay.from(player).stopFilter(BlockRay.onlyAirFilter()).distanceLimit(3).build();
        Optional<BlockRayHit<World>> hit_ = blockRay.end();
        if (hit_.isPresent()) {
            BlockRayHit<World> hit = hit_.get();
            Location<World> location = hit.getLocation();
            hit.getFaces()[0].getOpposite();
            Vector3d vec = hit.getDirection().cross(0, 1, 0);
            Location<World>[][] wall = getWall(location, findNearest(vec));

            BlockSnapshot[][] wallSnapshot = getWallSnapshot(wall);
            for (int i = 0; i < wall.length; i++) {
                for (int j = 0; j < wall[i].length; j++) {
                    wall[i][j].setBlockType(BlockTypes.PACKED_ICE, Cause.source(plugin).build());
                }
            }

            Task.builder().delay(2, TimeUnit.SECONDS).execute(() -> {
                for (int i = 0; i < wall.length; i++) {
                    for (int j = 0; j < wall[i].length; j++) {
                        wall[i][j].restoreSnapshot(wallSnapshot[i][j], true, BlockChangeFlag.ALL, Cause.source(plugin).build());
                    }
                }
            }).submit(plugin);
        }
    }

    public Vector3i findNearest(Vector3d vec) {
        Vector3d vec2 = vec.abs();
        if (vec2.getX() > vec2.getY()) {
            if (vec2.getX() > vec2.getZ()) {
                if (vec.getX() > 0) {
                    return new Vector3i(1, 0, 0);
                } else {
                    return new Vector3i(-1, 0, 0);
                }
            } else {
                if (vec.getZ() > 0) {
                    return new Vector3i(0, 0, 1);
                } else {
                    return new Vector3i(0, 0, -1);
                }
            }
        }
        if (vec2.getY() > vec2.getZ()) {
            if (vec.getY() > 0) {
                return new Vector3i(0, 1, 0);
            } else {
                return new Vector3i(0, -1, 0);
            }
        } else {
            if (vec.getZ() > 0) {
                return new Vector3i(0, 0, 1);
            } else {
                return new Vector3i(0, 0, -1);
            }
        }
    }

    public Location<World>[][] getWall(Location<World> center, Vector3i vec) {
        vec = vec.add(0, 1, 0);
        Location<World>[][] wall = new Location[3][3];
        wall[1][1] = center;
        wall[0][0] = center.add(vec.mul(-1, 1, -1));
        wall[0][1] = wall[0][0].add(vec.mul(1, 0, 1));
        wall[0][2] = wall[0][1].add(vec.mul(1, 0, 1));
        wall[1][0] = center.add(vec.mul(-1, 0, -1));
        wall[1][2] = center.add(vec.mul(1, 0, 1));
        wall[2][0] = wall[1][0].add(vec.mul(0, -1, 0));
        wall[2][1] = wall[2][0].add(vec.mul(1, 0, 1));
        wall[2][2] = wall[2][1].add(vec.mul(1, 0, 1));
        return wall;
    }


    public BlockSnapshot[][] getWallSnapshot(Location<World>[][] wall) {
        BlockSnapshot[][] wallSnapshot = new BlockSnapshot[3][3];
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                wallSnapshot[i][j] = wall[i][j].createSnapshot();
            }
        }
        return wallSnapshot;
    }
}
