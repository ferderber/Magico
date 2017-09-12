package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataContentUpdater;
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

    private static class MagicoUserUpdater implements DataContentUpdater {

        @Override
        public int getInputVersion() {
            return 1;
        }

        @Override
        public int getOutputVersion() {
            return 2;
        }

        @Override
        public DataView update(DataView content) {
            content.set(MagicoKeys.IS_CASTING_SPELL, false);
            return content;
        }


    }
}
