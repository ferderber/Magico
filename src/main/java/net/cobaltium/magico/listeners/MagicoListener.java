package net.cobaltium.magico.listeners;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.spells.Spell;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;

import java.util.Optional;

public class MagicoListener {

    @Listener
    public void rightClick(InteractItemEvent.Secondary.MainHand e) {
        Cause c = e.getCause();
        Optional<Player> player_ = c.first(Player.class);
        if(player_.isPresent() && e.getItemStack().getType() == ItemTypes.STICK) {
            Player player = player_.get();
            MagicoUserData user = player.getOrCreate(MagicoUserData.class).get();
            Optional<Spell> spell_ = user.getCurrentSpell();
            if(spell_.isPresent()) {
                Spell spell = spell_.get();
                spell.handle(player);
            }
        }
    }
}
