package net.cobaltium.magico.listeners;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.spells.*;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class MagicoListener {

    private PluginContainer plugin;

    public MagicoListener(PluginContainer plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void rightClick(InteractItemEvent.Secondary.MainHand e) {
        Cause c = e.getCause();
        Optional<Player> player_ = c.first(Player.class);
        if(player_.isPresent() && e.getItemStack().getType() == ItemTypes.STICK) {
            Player player = player_.get();
            MagicoUserData user = player.getOrCreate(MagicoUserData.class).get();

            if (player.get(Keys.IS_SNEAKING).isPresent() && player.get(Keys.IS_SNEAKING).get().booleanValue()) {
                SpellType spellType = getNextSpellName(user.getCurrentSpellName());
                user.setCurrentSpell(spellType.getKey());
                player.offer(user);
                player.sendMessage(Text.builder().append(Text.of("Current spell changed to ")).append(Text.of(spellType.getName())).color(TextColors.AQUA).build());
            } else {
                Optional<Spell> spell_ = user.getCurrentSpell();

                Spell spell;
                if (spell_.isPresent()) {
                    spell = spell_.get();
                } else {
                    spell = new Fireball();
                }
                if (user.getMana() >= spell.getManaCost()) {
                    spell.handle(plugin, player);
                    user.reduceMana(spell.getManaCost());
                    player.offer(user);
                } else {
                    player.sendMessage(Text.of("Not enough mana"));
                }
            }
        }
    }

    public SpellType getNextSpellName(String spellName) {
        SpellType[] spells = SpellList.ALL;
        for (int i = 0; i < spells.length; i++) {
            if (spells[i].getKey() == spellName) {
                if (i < spells.length - 1) {
                    return spells[i + 1];
                } else {
                    return spells[0];
                }
            }
        }
        return spells[0];
    }

    public Optional<Spell> getSpell(String spellName) {
        SpellFactory spellFactory = new SpellFactory();
        return spellFactory.getSpell(spellName);
    }

}
