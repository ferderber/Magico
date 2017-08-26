package net.cobaltium.magico.data;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class MagicoUserBuilder extends AbstractDataBuilder<MagicoUserData>
        implements DataManipulatorBuilder<MagicoUserData, ImmutableMagicoUserData> {

    public MagicoUserBuilder() {
        super(MagicoUserData.class, 1);
    }

    @Override
    public MagicoUserData create() {
        return new MagicoUserData();
    }

    @Override
    protected Optional<MagicoUserData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }

    @Override
    public Optional<MagicoUserData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }
}
