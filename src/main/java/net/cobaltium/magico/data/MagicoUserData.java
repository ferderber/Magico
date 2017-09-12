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

public class MagicoUserData extends AbstractData<MagicoUserData, ImmutableMagicoUserData> {

    private int mana;
    private int currentSpellId;
    private boolean scoreboardClosing;
    private int manaRestoreMultiplier;
    private boolean isCastingSpell;

    protected MagicoUserData() {
        this(100, -1, false, false, 1);
    }

    protected MagicoUserData(int mana, int spellId, boolean scoreboardClosing, boolean isCastingSpell, int manaRestoreMultiplier) {
        this.mana = mana;
        this.currentSpellId = spellId;
        this.scoreboardClosing = scoreboardClosing;
        this.manaRestoreMultiplier = manaRestoreMultiplier;
        this.isCastingSpell = isCastingSpell;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MagicoKeys.PLAYER_MANA, this::getMana);
        registerFieldSetter(MagicoKeys.PLAYER_MANA, this::setMana);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);

        registerFieldGetter(MagicoKeys.CURRENT_SPELL, this::getCurrentSpellId);
        registerFieldSetter(MagicoKeys.CURRENT_SPELL, this::setCurrentSpellId);
        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpellId);

        registerFieldGetter(MagicoKeys.SCOREBOARD_CLOSING, this::getScoreboardClosing);
        registerFieldSetter(MagicoKeys.SCOREBOARD_CLOSING, this::setScoreboardClosing);
        registerKeyValue(MagicoKeys.SCOREBOARD_CLOSING, this::scoreboardClosing);

        registerFieldGetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::getManaRestoreMultiplier);
        registerFieldSetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::setManaRestoreMultiplier);
        registerKeyValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::manaRestoreMultiplier);

        registerFieldGetter(MagicoKeys.IS_CASTING_SPELL, this::isCastingSpell);
        registerFieldSetter(MagicoKeys.IS_CASTING_SPELL, this::setCastingSpell);
        registerKeyValue(MagicoKeys.IS_CASTING_SPELL, this::castingSpell);
    }

    public Value<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana);
    }

    public Value<Integer> manaRestoreMultiplier() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier);
    }

    public Value<Integer> currentSpellId() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellId);
    }

    public Value<Boolean> scoreboardClosing() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing);
    }

    public Value<Boolean> castingSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.IS_CASTING_SPELL, isCastingSpell);
    }

    public int getMana() {
        return mana;
    }

    public boolean getScoreboardClosing() {
        return scoreboardClosing;
    }

    public int getCurrentSpellId() {
        return currentSpellId;
    }

    public int getManaRestoreMultiplier() {
        return manaRestoreMultiplier;
    }

    public boolean isCastingSpell() {
        return isCastingSpell;
    }

    public void setCastingSpell(boolean isCastingSpell) {
        this.isCastingSpell = isCastingSpell;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setScoreboardClosing(boolean scoreboardClosing) {
        this.scoreboardClosing = scoreboardClosing;
    }

    public void setCurrentSpellId(int spellId) {
        this.currentSpellId = spellId;
    }

    public void setManaRestoreMultiplier(int restoreRate) {
        this.manaRestoreMultiplier = restoreRate;
    }

    public int modifyMana(int amount) {
        this.mana = mana + amount;
        return mana;
    }

    @Override
    public Optional<MagicoUserData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MagicoUserData> from(DataView dataView) {
        if (dataView.contains(MagicoKeys.PLAYER_MANA.getQuery())) {
            this.mana = dataView.getInt(MagicoKeys.PLAYER_MANA.getQuery()).get();
        }
        if (dataView.contains(MagicoKeys.CURRENT_SPELL.getQuery())) {
            this.currentSpellId = dataView.getInt(MagicoKeys.CURRENT_SPELL.getQuery()).get();
        }
        if (dataView.contains(MagicoKeys.MANA_RESTORE_MULTIPLIER.getQuery())) {
            this.manaRestoreMultiplier = dataView.getInt(MagicoKeys.MANA_RESTORE_MULTIPLIER.getQuery()).get();
        }
        if (dataView.contains(MagicoKeys.SCOREBOARD_CLOSING.getQuery())) {
            this.scoreboardClosing = dataView.getBoolean(MagicoKeys.SCOREBOARD_CLOSING.getQuery()).get();
        }
        if (dataView.contains(MagicoKeys.IS_CASTING_SPELL.getQuery())) {
            this.isCastingSpell = dataView.getBoolean(MagicoKeys.IS_CASTING_SPELL.getQuery()).get();
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MagicoUserData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MagicoUserData> data_ = dataHolder.get(MagicoUserData.class);
        if (data_.isPresent()) {
            MagicoUserData data = data_.get();
            MagicoUserData mergedData = overlap.merge(this, data);
            this.mana = mergedData.mana;
            this.currentSpellId = mergedData.currentSpellId;
            this.scoreboardClosing = mergedData.scoreboardClosing;
            this.manaRestoreMultiplier = mergedData.manaRestoreMultiplier;
            this.isCastingSpell = mergedData.isCastingSpell;
        }
        return Optional.of(this);
    }


    @Override
    public MagicoUserData copy() {
        return new MagicoUserData(mana, currentSpellId, scoreboardClosing, isCastingSpell, manaRestoreMultiplier);
    }

    @Override
    public ImmutableMagicoUserData asImmutable() {
        return new ImmutableMagicoUserData(mana, currentSpellId, scoreboardClosing, isCastingSpell, manaRestoreMultiplier);
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(MagicoKeys.CURRENT_SPELL, currentSpellId)
                .set(MagicoKeys.PLAYER_MANA, mana)
                .set(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing)
                .set(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier)
                .set(MagicoKeys.IS_CASTING_SPELL, isCastingSpell);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
