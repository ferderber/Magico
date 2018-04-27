package net.cobaltium.magico.data;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class MagicoStructureBuilder extends AbstractDataBuilder<MagicoStructureData>
        implements DataManipulatorBuilder<MagicoStructureData, ImmutableMagicoStructureData> {

    public MagicoStructureBuilder() {
        super(MagicoStructureData.class, 1);
    }

    @Override
    public MagicoStructureData create() {
        return new MagicoStructureData();
    }

    @Override
    public Optional<MagicoStructureData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<MagicoStructureData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }
}
