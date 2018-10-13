package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableMagicoUserData extends AbstractImmutableData<ImmutableMagicoUserData, MagicoUserData> {

    private int mana;
    private int manaRestoreMultiplier;
    private int currentSpellId;
    private boolean scoreboardClosing;
    private boolean displayMana;
    private boolean isCastingSpell;


    protected ImmutableMagicoUserData(int mana, int spellId, boolean scoreboardClosing, boolean displayMana, boolean isCastingSpell, int manaRestoreMultiplier) {
        this.mana = mana;
        this.currentSpellId = spellId;
        this.scoreboardClosing = scoreboardClosing;
        this.displayMana = displayMana;
        this.manaRestoreMultiplier = manaRestoreMultiplier;
        this.isCastingSpell = isCastingSpell;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MagicoKeys.PLAYER_MANA, () -> this.mana);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);

        registerFieldGetter(MagicoKeys.CURRENT_SPELL, () -> this.currentSpellId);
        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpellId);

        registerFieldGetter(MagicoKeys.SCOREBOARD_CLOSING, () -> this.scoreboardClosing);
        registerKeyValue(MagicoKeys.SCOREBOARD_CLOSING, this::scoreboardClosing);

        registerFieldGetter(MagicoKeys.DISPLAY_MANA, () -> this.displayMana);
        registerKeyValue(MagicoKeys.DISPLAY_MANA, this::displayMana);

        registerFieldGetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, () -> this.manaRestoreMultiplier);
        registerKeyValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::manaRestoreMultiplier);

        registerFieldGetter(MagicoKeys.IS_CASTING_SPELL, () -> this.isCastingSpell);
        registerKeyValue(MagicoKeys.IS_CASTING_SPELL, this::castingSpell);
    }

    @Override
    protected DataContainer fillContainer(DataContainer dataContainer) {
        dataContainer.set(MagicoKeys.PLAYER_MANA.getQuery(), mana);
        dataContainer.set(MagicoKeys.CURRENT_SPELL.getQuery(), currentSpellId);
        dataContainer.set(MagicoKeys.SCOREBOARD_CLOSING.getQuery(), scoreboardClosing);
        dataContainer.set(MagicoKeys.DISPLAY_MANA.getQuery(), displayMana);
        dataContainer.set(MagicoKeys.MANA_RESTORE_MULTIPLIER.getQuery(), manaRestoreMultiplier);
        dataContainer.set(MagicoKeys.IS_CASTING_SPELL.getQuery(), isCastingSpell);
        return dataContainer;
    }

    private ImmutableValue<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana).asImmutable();
    }

    private ImmutableValue<Integer> manaRestoreMultiplier() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier).asImmutable();
    }

    private ImmutableValue<Integer> currentSpellId() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellId).asImmutable();
    }

    private ImmutableValue<Boolean> scoreboardClosing() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing).asImmutable();
    }

    private ImmutableValue<Boolean> displayMana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.DISPLAY_MANA, displayMana).asImmutable();
    }

    private ImmutableValue<Boolean> castingSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.IS_CASTING_SPELL, isCastingSpell).asImmutable();
    }

    @Override
    public MagicoUserData asMutable() {
        return new MagicoUserData(mana, currentSpellId, scoreboardClosing, displayMana, isCastingSpell, manaRestoreMultiplier);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
