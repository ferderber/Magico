package net.cobaltium.magico.listeners;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import net.cobaltium.magico.data.MagicoUserData;
import net.cobaltium.magico.db.Database;
import net.cobaltium.magico.db.tables.UserSpells;
import net.cobaltium.magico.spells.SpellType;

import static net.cobaltium.magico.utils.ScoreboardUtils.SetScoreboardList;
import static net.cobaltium.magico.utils.ScoreboardUtils.SetScoreboardNormal;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.block.tileentity.TileEntityTypes;
import org.spongepowered.api.block.tileentity.carrier.ShulkerBox;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.crafting.CraftingGridInventory;
import org.spongepowered.api.item.inventory.crafting.CraftingInventory;
import org.spongepowered.api.item.inventory.type.TileEntityInventory;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MagicoListener {

    private PluginContainer plugin;

    public MagicoListener(PluginContainer plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void rightClick(InteractItemEvent.Secondary.MainHand e) {
        Cause c = e.getCause();
        EventContext context = c.getContext();
        Optional<Player> player_ = c.first(Player.class);
        if (player_.isPresent() && e.getItemStack().getType() == ItemTypes.STICK) {
            Player player = player_.get();
            if (player.hasPermission("magico.spells")) {
                if (player.get(Keys.IS_SNEAKING).isPresent() && player.get(Keys.IS_SNEAKING).get()) {
                    changeSpell(player);
                } else {
                    castSpell(context, player);
                }
            }
        }
    }

    private void changeSpell(Player player) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
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
                SetScoreboardList(player, availableSpells, Optional.ofNullable(plugin));
            } else {
                player.sendMessage(Text.of("You don't have any spells"));
            }
        } catch (SQLException ex) {
        } finally {
            con.closeQuietly();
        }

    }

    private void castSpell(EventContext e, Player player) {
        MagicoUserData userData = player.getOrCreate(MagicoUserData.class).get();
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
                    SetScoreboardNormal(player, Optional.ofNullable(plugin));
                    spellType.getSpell().handle(e, plugin, player);
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
