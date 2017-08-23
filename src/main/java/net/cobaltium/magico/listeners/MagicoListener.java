package net.cobaltium.magico.listeners;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.tables.StructureLocation;
import net.cobaltium.magico.spells.*;
import net.cobaltium.magico.tasks.ManaRestoreTask;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MagicoListener {

    private PluginContainer plugin;
    private List<StructureLocation> structures;

    public MagicoListener(PluginContainer plugin, List<StructureLocation> structures) {
        this.plugin = plugin;
        this.structures = structures;
    }

    @Listener
    public void rightClick(InteractItemEvent.Secondary.MainHand e) {
        Cause c = e.getCause();
        Optional<Player> player_ = c.first(Player.class);
        if (player_.isPresent() && e.getItemStack().getType() == ItemTypes.STICK) {
            Player player = player_.get();
            MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();

            if (player.get(Keys.IS_SNEAKING).isPresent() && player.get(Keys.IS_SNEAKING).get().booleanValue()) {
                SpellType spellType = getNextSpellName(userData.getCurrentSpellName());
                userData.setCurrentSpell(spellType.getKey());
                player.offer(userData);
                player.sendMessage(Text.builder()
                        .append(Text.of("Current spell changed to "))
                        .append(Text.of(spellType.getName())).color(TextColors.AQUA).build());
                updateScoreboard(player, userData);
            } else {
                Optional<Spell> spell_ = userData.getCurrentSpell();

                Spell spell;
                if (spell_.isPresent()) {
                    spell = spell_.get();
                } else {
                    spell = new Fireball();
                }
                if (userData.getMana() >= spell.getManaCost()) {
                    spell.handle(plugin, player);
                    userData.reduceMana(spell.getManaCost());
                    player.offer(userData);
                    updateScoreboard(player, userData);
                } else {
                    player.sendMessage(Text.of("Not enough mana"));
                }
            }
        }
    }

    @Listener
    public void playerJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        Task.builder()
                .execute(new ManaRestoreTask(player, structures))
                .interval(1, TimeUnit.SECONDS)
                .submit(plugin);
    }

    public void updateScoreboard(Player player, MagicoUserData userData) {
        Scoreboard scoreboard = Scoreboard.builder().build();
        Objective obj = Objective.builder()
                .name("MagicoScoreboard")
                .criterion(Criteria.DUMMY)
                .name("Stats")
                .build();
        //Mana score text
        Score manaScore = obj.getOrCreateScore(Text.of("Mana:"));
        manaScore.setScore(userData.getMana());
        //Current spell score text
        Score spellScore = obj.getOrCreateScore(Text.builder()
                .append(Text.of("Spell: "))
                .append(Text.builder()
                        .append(Text.of(userData.getCurrentSpellName().replace("net.cobaltium.magico.spells.", "")))
                        .color(TextColors.BLUE).build())
                .append(Text.of(", Cost:"))
                .build());
        spellScore.setScore(userData.getCurrentSpell().get().getManaCost());

        scoreboard.addObjective(obj);
        scoreboard.updateDisplaySlot(obj, DisplaySlots.SIDEBAR);
        player.setScoreboard(scoreboard);

        //remove scoreboard after 5 seconds
        if (!userData.getScoreboardClosing()) {
            userData.setScoreboardClosing(true);
            player.offer(userData);
            Task.builder().delay(5, TimeUnit.SECONDS).execute(() -> {
                MagicoUserData user = player.getOrCreate(MagicoUserData.class).get();
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
}
