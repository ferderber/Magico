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

public class MagicoProjectileData extends AbstractData<MagicoProjectileData, ImmutableMagicoProjectileData> {

    private boolean blockDamage;

    public MagicoProjectileData() {
        this(false);
    }

    public MagicoProjectileData(boolean value) {
        this.blockDamage = value;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MagicoKeys.DOES_BLOCK_DAMAGE, this::doesBlockDamage);
        registerFieldSetter(MagicoKeys.DOES_BLOCK_DAMAGE, this::setBlockDamage);
        registerKeyValue(MagicoKeys.DOES_BLOCK_DAMAGE, this::blockDamage);
    }

    public Value<Boolean> blockDamage() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.DOES_BLOCK_DAMAGE, blockDamage);
    }

    public boolean doesBlockDamage() {
        return blockDamage;
    }

    public void setBlockDamage(boolean blockDamage) {
        this.blockDamage = blockDamage;
    }

    @Override
    public Optional<MagicoProjectileData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MagicoProjectileData> data_ = dataHolder.get(MagicoProjectileData.class);
        if (data_.isPresent()) {
            MagicoProjectileData data = data_.get();
            MagicoProjectileData mergedData = overlap.merge(this, data);
            this.blockDamage = mergedData.doesBlockDamage();
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MagicoProjectileData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MagicoProjectileData> from(DataView view) {
        if (view.contains(MagicoKeys.DOES_BLOCK_DAMAGE.getQuery())) {
            this.blockDamage = view.getBoolean(MagicoKeys.DOES_BLOCK_DAMAGE.getQuery()).get();
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MagicoProjectileData copy() {
        return new MagicoProjectileData(blockDamage);
    }

    @Override
    public ImmutableMagicoProjectileData asImmutable() {
        return new ImmutableMagicoProjectileData(blockDamage);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }


    @Override
    protected DataContainer fillContainer(DataContainer dataContainer) {
        return dataContainer.set(MagicoKeys.DOES_BLOCK_DAMAGE, blockDamage);
    }

}

