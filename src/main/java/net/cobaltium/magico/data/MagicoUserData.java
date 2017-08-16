package net.cobaltium.magico.data;

import net.cobaltium.magico.MagicoKeys;
import net.cobaltium.magico.spells.Fireball;
import net.cobaltium.magico.spells.Spell;
import net.cobaltium.magico.spells.SpellFactory;
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
    private String currentSpellName;
    protected MagicoUserData() {
        this.mana = 100;
        this.currentSpellName = Fireball.class.getName();
        registerGettersAndSetters();
    }
    protected MagicoUserData(int mana, String spellName) {
        this.mana = mana;
        this.currentSpellName = spellName;
        registerGettersAndSetters();
    }
    @Override
    protected void registerGettersAndSetters() {
//        registerFieldGetter(MagicoKeys.CURRENT_SPELL, this::getCurrentSpell);
//        registerFieldGetter(MagicoKeys.PLAYER_MANA, this::getMana);
//        registerFieldSetter(MagicoKeys.PLAYER_MANA, this::setMana);
//        registerFieldSetter(MagicoKeys.CURRENT_SPELL, this::setCurrentSpell);

        registerFieldGetter(MagicoKeys.PLAYER_MANA, () -> this.mana);
        registerFieldGetter(MagicoKeys.CURRENT_SPELL, () -> this.currentSpellName);
        registerFieldSetter(MagicoKeys.PLAYER_MANA, mana -> this.mana = mana);
        registerFieldSetter(MagicoKeys.CURRENT_SPELL, spellName -> this.currentSpellName = spellName);

        registerKeyValue(MagicoKeys.CURRENT_SPELL, this::currentSpell);
        registerKeyValue(MagicoKeys.PLAYER_MANA, this::mana);
    }

    public Value<Integer> mana() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.PLAYER_MANA, mana);
    }

    public Value<String> currentSpell() {
        return Sponge.getRegistry().getValueFactory().createValue(MagicoKeys.CURRENT_SPELL, currentSpellName);
    }

    public int getMana() {
        return get(MagicoKeys.PLAYER_MANA).get();
    }

    public Optional<Spell> getCurrentSpell() {
        String currSpell = get(MagicoKeys.CURRENT_SPELL).get();
        SpellFactory factory = new SpellFactory();
        return factory.getSpell(currSpell);
    }

    public void setMana(int i) {
        set(MagicoKeys.PLAYER_MANA, i);
    }

    public void setCurrentSpell(String spell) {
        set(MagicoKeys.CURRENT_SPELL, spell);
    }
    public String getCurrentSpellName() {
        return get(MagicoKeys.CURRENT_SPELL).get();
    }

    @Override
    public Optional<MagicoUserData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MagicoUserData> from(DataView dataView) {
        if(dataView.contains(MagicoKeys.PLAYER_MANA.getQuery())) {
            setMana(dataView.getInt(MagicoKeys.PLAYER_MANA.getQuery()).get());
        }
        if(dataView.contains(MagicoKeys.CURRENT_SPELL.getQuery())) {
            setCurrentSpell(dataView.getString(MagicoKeys.CURRENT_SPELL.getQuery()).get());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MagicoUserData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MagicoUserData> data_ = dataHolder.get(MagicoUserData.class);
        if(data_.isPresent()) {
            MagicoUserData data = data_.get();
            MagicoUserData mergedData = overlap.merge(this, data);
            setMana(mergedData.getMana());
            setCurrentSpell(mergedData.getCurrentSpellName());
        }
        return Optional.of(this);
    }


    @Override
    public MagicoUserData copy() {
        return new MagicoUserData(getMana(), getCurrentSpellName());
    }

    @Override
    public ImmutableMagicoUserData asImmutable() {
        return new ImmutableMagicoUserData(getMana(), getCurrentSpellName());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
