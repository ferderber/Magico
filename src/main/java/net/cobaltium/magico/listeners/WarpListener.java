package net.cobaltium.magico.listeners;

import net.cobaltium.magico.schematics.Warp;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.List;

public class WarpListener {
    private PluginContainer plugin;

    public WarpListener(PluginContainer plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void placeBlock(ChangeBlockEvent.Place event, @First Player player) {
        Warp warp = new Warp();
        List<Transaction<BlockSnapshot>> blocks = event.getTransactions();
        blocks.forEach((block) -> block.getFinal().getLocation().ifPresent(warp::isStructure));
    }
}
