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
    public static Key<Value<String>> CURRENT_SPELL = KeyFactory.makeSingleKey(TypeTokens.STRING_TOKEN,
            new TypeToken<Value<String>>() {
            },
            DataQuery.of("magico", "spell"),
            "magico:spell",
            "Player's spell");
    public static Key<Value<Boolean>> DOES_BLOCK_DAMAGE = KeyFactory.makeSingleKey(TypeTokens.BOOLEAN_TOKEN,
            new TypeToken<Value<Boolean>>() {
            },
            DataQuery.of("magico", "blockDamage"),
            "magico:block_damage",
            "Block Damage");
    public static Key<Value<Boolean>> SCOREBOARD_CLOSING = KeyFactory.makeSingleKey(TypeTokens.BOOLEAN_TOKEN,
            new TypeToken<Value<Boolean>>() {
            },
            DataQuery.of("magico", "scoreboardClosing"),
            "magico:scoreboard_closing",
            "Scoreboard Closing");
}
