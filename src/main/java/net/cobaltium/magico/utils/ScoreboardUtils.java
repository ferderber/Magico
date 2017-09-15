package net.cobaltium.magico.utils;

import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.spells.SpellType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.scoreboard.critieria.Criteria;
import org.spongepowered.api.scoreboard.displayslot.DisplaySlots;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class ScoreboardUtils {

    //Default scoreboard. Shows mana, mana regen, and current spell.
    public static void SetScoreboardNormal(Player player, Optional<PluginContainer> plugin_) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        List<Text> lines = new ArrayList<>();

        //Mana
        lines.add(Text.builder()
                .append(Text.of("Mana:  "))
                .append(Text.builder()
                        .append(Text.of(userData.getMana()))
                        .color(TextColors.GREEN).build())
                .build());
        //mana regen
        double manaPerSec = userData.getManaRestoreMultiplier();
        lines.add(Text.builder()
                .append(Text.of("Regen:  "))
                .append(Text.builder()
                        .append(Text.of(manaPerSec))
                        .append(Text.of("/s"))
                        .color(TextColors.GREEN).build()).build());
        //current spell
        Optional<SpellType> spellType_ = SpellType.getById(userData.getCurrentSpellId());
        if (spellType_.isPresent()) {
            SpellType spellType = spellType_.get();
            lines.add(Text.builder()
                    .append(Text.of("Spell:  "))
                    .append(Text.builder()
                            .append(Text.of(spellType.getSpellName()))
                            .color(TextColors.BLUE).build())
                    .append(Text.of(", Cost:  "))
                    .append(Text.builder()
                            .append(Text.of(spellType.getSpell().getManaCost()))
                            .color(TextColors.GREEN).build())
                    .build());
        }
        ApplyScoreboard(lines, player, plugin_);
    }

    //scoreboard that shows the list of spells the player has.
    public static void SetScoreboardList(Player player, List<SpellType> spells, Optional<PluginContainer> plugin_) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        List<Text> lines = new ArrayList<>();

        for (int i = 0; i < spells.size(); i++) {
            SpellType spell = spells.get(i);
            if (spell.getSpellId() == userData.getCurrentSpellId()) {
                lines.add(Text.builder()
                        .append(Text.of(spell.getSpellName()))
                        .color(TextColors.BLUE)
                        .build());

            } else {
                lines.add(Text.of(spell.getSpellName()));
            }
        }
        ApplyScoreboard(lines, player, plugin_);
    }

    //a minimal scoreboard that shows only mana and mana regen.
    public static void SetScoreboardMinimal(Player player, Optional<PluginContainer> plugin_) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        List<Text> lines = new ArrayList<>();
        //Mana
        lines.add(Text.builder()
                .append(Text.of("Mana:  "))
                .append(Text.builder()
                        .append(Text.of(userData.getMana()))
                        .color(TextColors.GREEN).build())
                .build());
        //mana regen
        double manaPerSec = userData.getManaRestoreMultiplier();
        lines.add(Text.builder()
                .append(Text.of("Regen:  "))
                .append(Text.builder()
                        .append(Text.of(manaPerSec))
                        .append(Text.of("/s"))
                        .color(TextColors.GREEN).build()).build());
        ApplyScoreboard(lines, player, plugin_);
    }

    //applies scoreboard to target player. Called by above methods, never externally.
    private static void ApplyScoreboard(List<Text> lines, Player player, Optional<PluginContainer> plugin_) {
        Scoreboard scoreboard = Scoreboard.builder().build();
        Objective obj = Objective.builder()
                .name("MagicoScoreboard")
                .criterion(Criteria.DUMMY)
                .name("Stats")
                .build();
        for (int i=0; i < lines.size(); i++) {
            obj.getOrCreateScore(lines.get(i)).setScore(lines.size()-1-i);
        }
        scoreboard.addObjective(obj);
        scoreboard.updateDisplaySlot(obj, DisplaySlots.SIDEBAR);


        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
        player.setScoreboard(scoreboard);

        //remove scoreboard after 5 seconds
        if (!userData.getScoreboardClosing() && plugin_.isPresent()) {
            PluginContainer plugin = plugin_.get();
            userData.setScoreboardClosing(true);
            player.offer(userData);
            Task.builder().delay(5, TimeUnit.SECONDS).execute(() -> {
                MagicoUserData user = player.getOrCreate(MagicoUserData.class).get();
                if (user.getDisplayMana()) {
                    SetScoreboardMinimal(player, Optional.empty());
                } else {
                    player.setScoreboard(Scoreboard.builder().build());
                }
                user.setScoreboardClosing(false);
                player.offer(user);
            }).submit(plugin);
        }
    }
}
