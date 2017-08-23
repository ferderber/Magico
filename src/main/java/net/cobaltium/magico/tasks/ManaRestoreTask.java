package net.cobaltium.magico.tasks;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.tables.StructureLocation;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class ManaRestoreTask implements Consumer<Task> {

    private Player player;
    private List<StructureLocation> structures;

    public ManaRestoreTask(Player player, List<StructureLocation> structures) {
        this.player = player;
        this.structures = structures;
    }

    public void accept(Task task) {
        for (StructureLocation structure : structures) {
            double distance = player.getLocation().getPosition().distance(structure.getBlockLocation().toDouble());
            if (distance <= 100) {
                MagicoUserData data = player.getOrCreate(MagicoUserData.class).get();
                if (data.getMana() < 200) {
                    data.modifyMana(data.getManaRestoreMultiplier() * 5);
                    player.offer(data);
                }
                break;
            }
        }
    }
}
