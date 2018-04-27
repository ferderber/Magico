package net.cobaltium.magico;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class MagicoKeys {

    private MagicoKeys() {
    }

    public static Key<Value<Integer>> PLAYER_MANA = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .name("Mana")
            .query(DataQuery.of("mana"))
            .id("mana")
            .build();

    public static Key<Value<Integer>> MANA_RESTORE_MULTIPLIER = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .name("ManaMultiplier")
            .query(DataQuery.of("manamultiplier"))
            .id("mana_mulitplier")
            .build();

    public static Key<Value<Integer>> CURRENT_SPELL = Key.builder()
            .type(new TypeToken<Value<Integer>>() {
            })
            .name("CurrentSpell")
            .query(DataQuery.of("currentspell"))
            .id("current_spell")
            .build();
    public static Key<Value<Boolean>> DOES_BLOCK_DAMAGE = Key.builder()
            .type(new TypeToken<Value<Boolean>>() {
            })
            .name("DoesBlockDamage")
            .query(DataQuery.of("doesblockdamage"))
            .id("does_block_damage")
            .build();

    public static Key<Value<Boolean>> IS_CASTING_SPELL = Key.builder()
            .type(new TypeToken<Value<Boolean>>() {
            })
            .name("IsCastingSpell")
            .query(DataQuery.of("iscastingspell"))
            .id("is_casting_spell")
            .build();
    public static Key<Value<Boolean>> SCOREBOARD_CLOSING = Key.builder()
            .type(new TypeToken<Value<Boolean>>() {
            })
            .name("ScoreboardClosing")
            .query(DataQuery.of("scoreboardclosing"))
            .id("scoreboard_closing")
            .build();
    public static Key<Value<Boolean>> DISPLAY_MANA = Key.builder()
            .type(new TypeToken<Value<Boolean>>() {
            })
            .name("DisplayMana")
            .query(DataQuery.of("displaymana"))
            .id("display_mana")
            .build();
    public static Key<Value<Boolean>> IS_STRUCTURE_BLOCK = Key.builder()
            .type(new TypeToken<Value<Boolean>>() {
            })
            .name("IsStructureBlock")
            .query(DataQuery.of("isstructureblock"))
            .id("is_structure_block")
            .build();
}
