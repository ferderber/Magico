package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;


public class ImmutableMagicoProjectileData extends AbstractImmutableData<ImmutableMagicoProjectileData, MagicoProjectileData> {

    boolean blockDamage;

    protected ImmutableMagicoProjectileData(boolean value) {
        blockDamage = value;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MagicoKeys.DOES_BLOCK_DAMAGE, () -> blockDamage);
        registerKeyValue(MagicoKeys.DOES_BLOCK_DAMAGE, this::blockDamage);
    }

    public ImmutableValue<Boolean> blockDamage() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.DOES_BLOCK_DAMAGE, blockDamage).asImmutable();
    }

    public MagicoProjectileData asMutable() {
        return new MagicoProjectileData(blockDamage);
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(MagicoKeys.DOES_BLOCK_DAMAGE.getQuery(), blockDamage);
    }
    @Override
    public int getContentVersion() {
        return 0;
    }
}
