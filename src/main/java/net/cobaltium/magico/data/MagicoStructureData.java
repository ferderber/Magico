package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class MagicoStructureData extends AbstractData<MagicoStructureData, ImmutableMagicoStructureData> {

    private boolean structureBlock;

    public MagicoStructureData() {
        this(false);
    }

    public MagicoStructureData(boolean value) {
        this.structureBlock = value;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MagicoKeys.IS_STRUCTURE_BLOCK, this::isStructureBlock);
        registerFieldSetter(MagicoKeys.IS_STRUCTURE_BLOCK, this::setStructureBlock);
        registerKeyValue(MagicoKeys.IS_STRUCTURE_BLOCK, this::structureBlock);
    }

    public Value<Boolean> structureBlock() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.IS_STRUCTURE_BLOCK, structureBlock);
    }

    public boolean isStructureBlock() {
        return structureBlock;
    }

    public void setStructureBlock(boolean structureBlock) {
        this.structureBlock = structureBlock;
    }

    @Override
    public Optional<MagicoStructureData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MagicoStructureData> data_ = dataHolder.get(MagicoStructureData.class);
        if (data_.isPresent()) {
            MagicoStructureData data = data_.get();
            MagicoStructureData mergedData = overlap.merge(this, data);
            this.structureBlock = mergedData.isStructureBlock();
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MagicoStructureData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MagicoStructureData> from(DataView view) {
        if (view.contains(MagicoKeys.IS_STRUCTURE_BLOCK.getQuery())) {
            this.structureBlock = view.getBoolean(MagicoKeys.IS_STRUCTURE_BLOCK.getQuery()).get();
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MagicoStructureData copy() {
        return new MagicoStructureData(structureBlock);
    }

    @Override
    public ImmutableMagicoStructureData asImmutable() {
        return new ImmutableMagicoStructureData(structureBlock);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }


    @Override
    protected DataContainer fillContainer(DataContainer dataContainer) {
        return dataContainer.set(MagicoKeys.IS_STRUCTURE_BLOCK, structureBlock);
    }
}
