package net.cobaltium.magico.schematics;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.world.schematic.BlockPalette;
import org.spongepowered.api.world.schematic.BlockPaletteType;
import org.spongepowered.api.world.schematic.BlockPaletteTypes;

import java.util.BitSet;
import java.util.Collection;
import java.util.Optional;

//From SpongeCommon, using as the base Palette
public class BimapPalette implements BlockPalette {
    private static final int DEFAULT_ALLOCATION_SIZE = 64;

    private final BiMap<Integer, BlockState> ids;
    private final BiMap<BlockState, Integer> idsr;
    private final BitSet allocation = new BitSet(DEFAULT_ALLOCATION_SIZE);
    private int maxId = 0;

    public BimapPalette() {
        this.ids = HashBiMap.create();
        this.idsr = this.ids.inverse();
    }

    public BimapPalette(int expectedSize) {
        this.ids = HashBiMap.create(expectedSize);
        this.idsr = this.ids.inverse();
    }

    @Override
    public BlockPaletteType getType() {
        return BlockPaletteTypes.LOCAL;
    }

    @Override
    public int getHighestId() {
        return this.maxId;
    }

    @Override
    public Optional<Integer> get(BlockState state) {
        return Optional.ofNullable(this.idsr.get(state));
    }
    public boolean isInPalette(BlockState state) {
        return this.idsr.get(state) != null;
    }

    @Override
    public int getOrAssign(BlockState state) {
        Integer id = this.idsr.get(state);
        if (id == null) {
            int next = this.allocation.nextClearBit(0);
            if (this.maxId < next) {
                this.maxId = next;
            }
            this.allocation.set(next);
            this.ids.put(next, state);
            return next;
        }
        return id;
    }

    @Override
    public Optional<BlockState> get(int id) {
        return Optional.ofNullable(this.ids.get(id));
    }

    public void assign(BlockState state, int id) {
        if (this.maxId < id) {
            this.maxId = id;
        }
        this.allocation.set(id);
        this.ids.put(id, state);
    }

    @Override
    public boolean remove(BlockState state) {
        Integer id = this.idsr.get(state);
        if (id == null) {
            return false;
        }
        this.allocation.clear(id);
        if (id == this.maxId) {
            this.maxId = this.allocation.previousSetBit(this.maxId);
        }
        this.ids.remove(id);
        return true;
    }

    @Override
    public Collection<BlockState> getEntries() {
        return this.idsr.keySet();
    }
}
