package net.cobaltium.magico.data;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class MagicoProjectileBuilder extends AbstractDataBuilder<MagicoProjectileData>
        implements DataManipulatorBuilder<MagicoProjectileData, ImmutableMagicoProjectileData> {

    public MagicoProjectileBuilder() {
        super(MagicoProjectileData.class, 1);
    }

    @Override
    public MagicoProjectileData create() {
        return new MagicoProjectileData();
    }

    @Override
    public Optional<MagicoProjectileData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<MagicoProjectileData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }
}
