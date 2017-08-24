package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import net.cobaltium.magico.spells.SpellType;
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

    protected MagicoUserData() {
        this.mana = 100;
        this.currentSpellId = SpellType.FIREBALL.getSpellId();
        this.scoreboardClosing = false;
        this.manaRestoreMultiplier = 1;
        registerGettersAndSetters();
    }

    protected MagicoUserData(int mana, int spellId, boolean scoreboardClosing, int manaRestoreMultiplier) {
        this.mana = mana;
        this.currentSpellId = spellId;
        this.scoreboardClosing = scoreboardClosing;
        this.manaRestoreMultiplier = manaRestoreMultiplier;
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MagicoKeys.PLAYER_MANA, () -> this.mana);
        registerFieldSetter(MagicoKeys.PLAYER_MANA, mana -> this.mana = mana);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);

        registerFieldGetter(MagicoKeys.CURRENT_SPELL, () -> this.currentSpellId);
        registerFieldSetter(MagicoKeys.CURRENT_SPELL, spellId -> this.currentSpellId = spellId);
        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpellId);

        registerFieldGetter(MagicoKeys.SCOREBOARD_CLOSING, () -> this.scoreboardClosing);
        registerFieldSetter(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing -> this.scoreboardClosing = scoreboardClosing);
        registerKeyValue(MagicoKeys.SCOREBOARD_CLOSING, this::scoreboardClosing);

        registerFieldGetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, () -> this.manaRestoreMultiplier);
        registerFieldSetter(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier -> this.manaRestoreMultiplier = manaRestoreMultiplier);
        registerKeyValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, this::manaRestoreMultiplier);

    }

    private Value<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana);
    }

    private Value<Integer> manaRestoreMultiplier() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier);
    }

    private Value<Integer> currentSpellId() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellId);
    }

    private Value<Boolean> scoreboardClosing() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.SCOREBOARD_CLOSING, scoreboardClosing);
    }

    public int getMana() {
        return mana;
    }

    public boolean getScoreboardClosing() {
        return scoreboardClosing;
    }

    public void setMana(int i) {
        mana = i;
        set(MagicoKeys.PLAYER_MANA, i);
    }

    public void setScoreboardClosing(boolean b) {
        scoreboardClosing = b;
        set(MagicoKeys.SCOREBOARD_CLOSING, b);
    }

    public void setCurrentSpellId(int spellId) {
        this.currentSpellId = spellId;
        set(MagicoKeys.CURRENT_SPELL, spellId);
    }

    public int getCurrentSpellId() {
        return currentSpellId;
    }

    public void setManaRestoreMultiplier(int restoreRate) {
        manaRestoreMultiplier = restoreRate;
        set(MagicoKeys.MANA_RESTORE_MULTIPLIER, manaRestoreMultiplier);
    }

    public int getManaRestoreMultiplier() {
        return manaRestoreMultiplier;
    }


    public int reduceMana(int amount) {
        mana = mana - amount;
        set(MagicoKeys.PLAYER_MANA, mana);
        return mana;
    }

    public int modifyMana(int amount) {
        mana = mana + amount;
        set(MagicoKeys.PLAYER_MANA, mana);
        return mana;
    }

    @Override
    public Optional<MagicoUserData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MagicoUserData> from(DataView dataView) {
        if (dataView.contains(MagicoKeys.PLAYER_MANA.getQuery())) {
            setMana(dataView.getInt(MagicoKeys.PLAYER_MANA.getQuery()).get());
        }
        if (dataView.contains(MagicoKeys.CURRENT_SPELL.getQuery())) {
            setCurrentSpellId(dataView.getInt(MagicoKeys.CURRENT_SPELL.getQuery()).get());
        }
        if (dataView.contains(MagicoKeys.MANA_RESTORE_MULTIPLIER.getQuery())) {
            setManaRestoreMultiplier(dataView.getInt(MagicoKeys.MANA_RESTORE_MULTIPLIER.getQuery()).get());
        }
        if (dataView.contains(MagicoKeys.SCOREBOARD_CLOSING.getQuery())) {
            setScoreboardClosing(dataView.getBoolean(MagicoKeys.SCOREBOARD_CLOSING.getQuery()).get());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MagicoUserData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MagicoUserData> data_ = dataHolder.get(MagicoUserData.class);
        if (data_.isPresent()) {
            MagicoUserData data = data_.get();
            MagicoUserData mergedData = overlap.merge(this, data);
            setMana(mergedData.getMana());
            setCurrentSpellId(mergedData.getCurrentSpellId());
        }
        return Optional.of(this);
    }


    @Override
    public MagicoUserData copy() {
        return new MagicoUserData(mana, currentSpellId, scoreboardClosing, manaRestoreMultiplier);
    }

    @Override
    public ImmutableMagicoUserData asImmutable() {
        return new ImmutableMagicoUserData(mana, currentSpellId, scoreboardClosing, manaRestoreMultiplier);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
