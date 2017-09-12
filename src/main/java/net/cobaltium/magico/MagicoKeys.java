package net.cobaltium.magico;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class MagicoKeys {

    private MagicoKeys() {
    }

    public static Key<Value<Integer>> PLAYER_MANA = KeyFactory.makeSingleKey(TypeTokens.INTEGER_TOKEN,
            new TypeToken<Value<Integer>>() {
            },
            DataQuery.of("magico", "mana"),
            "magico:mana",
            "Player's mana");
    public static Key<Value<Integer>> MANA_RESTORE_MULTIPLIER = KeyFactory.makeSingleKey(TypeTokens.INTEGER_TOKEN,
            new TypeToken<Value<Integer>>() {
            },
            DataQuery.of("magico", "manarestore"),
            "magico:manarestore",
            "Player's mana restoration rate");
    public static Key<Value<Integer>> CURRENT_SPELL = KeyFactory.makeSingleKey(TypeTokens.INTEGER_TOKEN,
            new TypeToken<Value<Integer>>() {
            },
            DataQuery.of("magico", "spell"),
            "magico:spell",
            "Player's current spell ID");
    public static Key<Value<Boolean>> DOES_BLOCK_DAMAGE = KeyFactory.makeSingleKey(TypeTokens.BOOLEAN_TOKEN,
            new TypeToken<Value<Boolean>>() {
            },
            DataQuery.of("magico", "blockDamage"),
            "magico:block_damage",
            "Block Damage");
    public static Key<Value<Boolean>> IS_CASTING_SPELL = KeyFactory.makeSingleKey(TypeTokens.BOOLEAN_TOKEN,
            new TypeToken<Value<Boolean>>() {
            },
            DataQuery.of("magico", "isCastingSpell"),
            "magico:is_casting_spell",
            "Casting Spell");
    public static Key<Value<Boolean>> SCOREBOARD_CLOSING = KeyFactory.makeSingleKey(TypeTokens.BOOLEAN_TOKEN,
            new TypeToken<Value<Boolean>>() {
            },
            DataQuery.of("magico", "scoreboardClosing"),
            "magico:scoreboard_closing",
            "Scoreboard Closing");
}
