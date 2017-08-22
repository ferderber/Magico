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
    boolean scoreboardClosing;

    protected ImmutableMagicoUserData(int mana, String spellName, boolean scoreboardClosing) {
        this.mana = mana;
        this.currentSpellName = spellName;
        this.scoreboardClosing = scoreboardClosing;
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MagicoKeys.PLAYER_MANA, () -> this.mana);
        registerFieldGetter(MagicoKeys.CURRENT_SPELL, () -> this.currentSpellName);
        registerFieldGetter(MagicoKeys.SCOREBOARD_CLOSING, () -> this.scoreboardClosing);
        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpell);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);
        registerKeyValue(MagicoKeys.SCOREBOARD_CLOSING, this::scoreboardClosing);
}

    private ImmutableValue<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana).asImmutable();
    }

    private ImmutableValue<String> currentSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellName).asImmutable();
    }

    private ImmutableValue<Boolean> scoreboardClosing() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing).asImmutable();
    }

    @Override
    public MagicoUserData asMutable() {
        return new MagicoUserData(mana, currentSpellName, scoreboardClosing);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
