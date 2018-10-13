package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableMagicoStructureData extends AbstractImmutableData<ImmutableMagicoStructureData, MagicoStructureData> {
    boolean structureBlock;

    public ImmutableMagicoStructureData(boolean value) {
        structureBlock = value;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MagicoKeys.IS_STRUCTURE_BLOCK, () -> structureBlock);
        registerKeyValue(MagicoKeys.IS_STRUCTURE_BLOCK, this::structureBlock);
    }

    public ImmutableValue<Boolean> structureBlock() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.IS_STRUCTURE_BLOCK, structureBlock).asImmutable();
    }

    public MagicoStructureData asMutable() {
        return new MagicoStructureData(structureBlock);
    }

    @Override
    public DataContainer fillContainer(DataContainer c) {
        return c.set(MagicoKeys.IS_STRUCTURE_BLOCK.getQuery(), structureBlock);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
