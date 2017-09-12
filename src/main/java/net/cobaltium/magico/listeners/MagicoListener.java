package net.cobaltium.magico.listeners;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.UserSpells;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        if (player_.isPresent() && e.getItemStack().getType() == ItemTypes.STICK) {
            Player player = player_.get();
            if (player.hasPermission("magico.spells")) {
                MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();

                if (player.get(Keys.IS_SNEAKING).isPresent() && player.get(Keys.IS_SNEAKING).get()) {

                    DataSourceConnectionSource con = null;
                    try {
                        con = Database.getConnection();
                        Dao<UserSpells, UUID> userSpellsDao = DaoManager.createDao(con, UserSpells.class);
                        List<UserSpells> userSpells = userSpellsDao.queryForEq("user_id", player.getUniqueId());
                        List<SpellType> availableSpells = new ArrayList<>();
                        if (userSpells.size() > 0) {
                            userSpells.forEach(spell_ -> spell_.getSpellType().ifPresent(spellType -> availableSpells.add(spellType)));
                            SpellType nextSpell = getNextSpellType(userData.getCurrentSpellId(), availableSpells);
                            userData.setCurrentSpellId(nextSpell.getSpellId());
                            player.offer(userData);
                            player.sendMessage(Text.builder()
                                    .append(Text.of("Current spell changed to "))
                                    .append(Text.of(nextSpell.getSpellName())).color(TextColors.AQUA).build());
                            updateScoreboard(player, userData);
                        } else {
                            player.sendMessage(Text.of("You don't have any spells"));
                        }
                    } catch (SQLException ex) {
                    } finally {
                        con.closeQuietly();
                    }
                } else {
                    int spellId = userData.getCurrentSpellId();
                    if (spellId >= 0) {
                        Optional<SpellType> spellType_ = SpellType.getById(userData.getCurrentSpellId());

                        SpellType spellType;
                        if (spellType_.isPresent()) {
                            spellType = spellType_.get();
                        } else {
                            spellType = SpellType.FIREBALL;
                        }
                        if (player.hasPermission(spellType.getPermission())) {
                            if (userData.getMana() >= spellType.getSpell().getManaCost()) {
                                userData.modifyMana(-spellType.getSpell().getManaCost());
                                player.offer(userData);
                                updateScoreboard(player, userData);
                                spellType.getSpell().handle(plugin, player);
                            } else {
                                player.sendMessage(Text.of("Not enough mana"));
                            }
                        } else {
                            player.sendMessage(Text.of("Permission required to use " + spellType.getSpellName()));
                        }
                    } else {
                        player.sendMessage(Text.of("No spell selected"));
                    }
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

    public SpellType getNextSpellType(int spellId, List<SpellType> spellTypes) {
        for (int i = 0; i < spellTypes.size(); i++) {
            if (spellTypes.get(i).getSpellId() == spellId) {
                if (i < spellTypes.size() - 1) {
                    return spellTypes.get(i + 1);
                } else {
                    return spellTypes.get(0);
                }
            }
        }
        return spellTypes.get(0);
    }
}
