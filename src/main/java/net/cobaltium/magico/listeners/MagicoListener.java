package net.cobaltium.magico.listeners;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.tables.StructureLocation;
import net.cobaltium.magico.spells.SpellType;
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

            if (player.get(Keys.IS_SNEAKING).isPresent() && player.get(Keys.IS_SNEAKING).get()) {
                SpellType spellType = getNextSpellType(userData.getCurrentSpellId());
                userData.setCurrentSpellId(spellType.getSpellId());
                player.offer(userData);
                player.sendMessage(Text.builder()
                        .append(Text.of("Current spell changed to "))
                        .append(Text.of(spellType.getSpellName())).color(TextColors.AQUA).build());
                updateScoreboard(player, userData);
            } else {
                Optional<SpellType> spellType_ = SpellType.getById(userData.getCurrentSpellId());

                SpellType spellType;
                if (spellType_.isPresent()) {
                    spellType = spellType_.get();
                } else {
                    spellType = SpellType.FIREBALL;
                }
                if (userData.getMana() >= spellType.getSpell().getManaCost()) {
                    spellType.getSpell().handle(plugin, player);
                    userData.reduceMana(spellType.getSpell().getManaCost());
                    player.offer(userData);
                    updateScoreboard(player, userData);
                } else {
                    player.sendMessage(Text.of("Not enough mana"));
                }
            }
        }
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
        Optional<SpellType> spellType_ = SpellType.getById(userData.getCurrentSpellId());
        if (spellType_.isPresent()) {
            SpellType spellType = spellType_.get();
            //Current spell score text
            Score spellScore = obj.getOrCreateScore(Text.builder()
                    .append(Text.of("Spell: "))
                    .append(Text.builder()
                            .append(Text.of(spellType.getSpellName()))
                            .color(TextColors.BLUE).build())
                    .append(Text.of(", Cost:"))
                    .build());
            spellScore.setScore(spellType.getSpell().getManaCost());

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
    }

    public SpellType getNextSpellType(int spellId) {
        SpellType[] spells = SpellType.values();
        for (int i = 0; i < spells.length; i++) {
            if (spells[i].getSpellId() == spellId) {
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
