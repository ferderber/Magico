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
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlot;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayMode;
import org.spongepowered.api.scoreboard.objective.displaymode.ObjectiveDisplayModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
                updateScoreboard(player);
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
                    updateScoreboard(player);
                } else {
                    player.sendMessage(Text.of("Not enough mana"));
                }
            }
        }
    }

    public void updateScoreboard(Player player) {
        MagicoUserData user = player.getOrCreate(MagicoUserData.class).get();

        Scoreboard sb = Scoreboard.builder().build();
        Objective obj = Objective.builder()
            .name("MagicoScoreboard")
            .criterion(Criteria.DUMMY)
            .name("Stats")
            .build();
        //mana
        Score manaScore = obj.getOrCreateScore(Text.of("Mana:"));
        manaScore.setScore(user.getMana());
        //current spell
        Text spellname = Text.builder(user.getCurrentSpellName().replace("net.cobaltium.magico.spells.", "")).color(TextColors.BLUE).build();
        Text cost = Text.of(", Cost:");
        Score spellScore = obj.getOrCreateScore(Text.of("Spell: ")
                .concat(spellname)
                .concat(cost));
        spellScore.setScore(user.getCurrentSpell().get().getManaCost());

        sb.addObjective(obj);
        sb.updateDisplaySlot(obj, DisplaySlots.SIDEBAR);
        player.setScoreboard(sb);

        //remove scoreboard after 5 seconds
        if(!user.getScoreboardClosing()) {
            user.setScoreboardClosing(true);
            player.offer(user);
            Task.builder().delay(5, TimeUnit.SECONDS).execute(() -> {
                player.setScoreboard(Scoreboard.builder().build());
                user.setScoreboardClosing(false);
                player.offer(user);
            }).submit(plugin);
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
