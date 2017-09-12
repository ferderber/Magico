package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableMagicoUserData extends AbstractImmutableData<ImmutableMagicoUserData, MagicoUserData> {

    private int mana;
    private int manaRestoreMultiplier;
    private int currentSpellId;
    private boolean scoreboardClosing;
    private boolean isCastingSpell;


    protected ImmutableMagicoUserData(int mana, int spellId, boolean scoreboardClosing, boolean isCastingSpell, int manaRestoreMultiplier) {
        this.mana = mana;
        this.currentSpellId = spellId;
        this.scoreboardClosing = scoreboardClosing;
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

        registerFieldGetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, () -> this.manaRestoreMultiplier);
        registerKeyValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::manaRestoreMultiplier);

        registerFieldGetter(MagicoKeys.IS_CASTING_SPELL, () -> this.isCastingSpell);
        registerKeyValue(MagicoKeys.IS_CASTING_SPELL, this::castingSpell);
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

    private ImmutableValue<Boolean> castingSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.IS_CASTING_SPELL, isCastingSpell).asImmutable();
    }

    @Override
    public MagicoUserData asMutable() {
        return new MagicoUserData(mana, currentSpellId, scoreboardClosing, isCastingSpell, manaRestoreMultiplier);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
