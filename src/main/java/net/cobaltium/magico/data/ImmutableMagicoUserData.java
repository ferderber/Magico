package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import net.cobaltium.magico.spells.Spell;
import net.cobaltium.magico.spells.SpellFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableMagicoUserData extends AbstractImmutableData<ImmutableMagicoUserData, MagicoUserData>{

    int mana;
    String currentSpellName;

    protected ImmutableMagicoUserData(int mana, String spellName) {
        this.mana = mana;
        this.currentSpellName = spellName;
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MagicoKeys.PLAYER_MANA, () -> this.mana);
        registerFieldGetter(MagicoKeys.CURRENT_SPELL, () -> this.currentSpellName);
        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpell);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);
}

    public ImmutableValue<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana).asImmutable();
    }

    public ImmutableValue<String> currentSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellName).asImmutable();
    }

    public Optional<Spell> getCurrentSpell() {
        SpellFactory factory = new SpellFactory();
        return factory.getSpell(currentSpellName);
    }

    @Override
    public MagicoUserData asMutable() {
        return new MagicoUserData(mana, currentSpellName);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
