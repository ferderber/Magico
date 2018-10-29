package net.cobaltium.magico.tasks;

        import com.flowpowered.math.vector.Vector3d;
        import net.cobaltium.magico.data.MagicoUserData;
        import net.cobaltium.magico.spells.ExpiringSpell;
        import org.spongepowered.api.Sponge;
        import org.spongepowered.api.data.key.Keys;
        import org.spongepowered.api.entity.living.player.Player;
        import org.spongepowered.api.entity.living.player.gamemode.GameModes;
        import org.spongepowered.api.scheduler.Task;
        import org.spongepowered.api.world.Location;
        import org.spongepowered.api.world.World;

        import java.util.Optional;
        import java.util.UUID;
        import java.util.function.Consumer;
        import java.util.function.Function;

public class EffectTask implements Consumer<Task> {

    private UUID playerId;
    private ExpiringSpell spell;

    public EffectTask(UUID playerId, ExpiringSpell spell) {
        this.playerId = playerId;
        this.spell = spell;
    }

    public void accept(Task task) {
        Optional<Player> player_ = Sponge.getServer().getPlayer(playerId);
        if (player_.isPresent()) {
            Player player = player_.get();
            MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
            if (userData.isCastingSpell()) {
                spell.applyEffect(player);

            } else {
                task.cancel();
            }
        }
    }

}
