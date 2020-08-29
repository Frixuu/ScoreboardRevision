package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.board.events.PlayerJoinListener;
import io.github.frixuu.scoreboardrevision.board.events.PlayerQuitListener;
import io.github.frixuu.scoreboardrevision.services.PlaceholderService;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.Getter;
import lombok.var;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rien on 21-10-2018.
 */
public class BoardRunnable extends BukkitRunnable {

    @Getter private final ScoreboardRow titleRow;
    @Getter private final List<ScoreboardRow> rows = new ArrayList<>();
    @Getter private final List<ScoreboardHolder> holders = new ArrayList<>();

    @Getter private final String boardKey;
    @Getter private final boolean isDefault;

    public BoardRunnable(String boardKey, Server server, ScoreboardPlugin plugin, PlaceholderService placeholderService, boolean isDefault) {
        this.boardKey = boardKey;
        this.isDefault = isDefault;

        var config = ConfigControl.get().getConfig("settings");

        var pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this, plugin, config, placeholderService), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(this), plugin);

        // Setup title row
        var sectionTitle = config.getConfigurationSection(boardKey + ".title");
        var linesTitle = sectionTitle.getStringList("liner"); // Get the lines
        var intervalTitle = config.getInt(boardKey + ".title.interval"); // Get the intervals
        titleRow = new ScoreboardRow(linesTitle, intervalTitle); // Create the title row!

        for (int i = 1; i < 200; i++) {
            var section = config.getConfigurationSection(boardKey + ".rows." + i); // Get their rows
            if (section != null) {
                var interval = section.getInt("interval");
                var lines = section.getStringList("liner");
                rows.add(new ScoreboardRow(lines, interval));
            }
        }

        // Register already joined players
        if (isDefault) {
            server.getOnlinePlayers()
                .forEach(player -> new ScoreboardHolder(this, plugin, placeholderService, config, player));
        }
    }

    public void registerHolder(ScoreboardHolder holder) {
        holders.add(holder);
    }

    public void unregisterHolder(ScoreboardHolder holder) {
        holders.remove(holder);
    }

    public void unregisterHolder(Player player) {
        holders.stream()
            .filter(holder -> holder.player == player)
            .findFirst()
            .ifPresent(this::unregisterHolder);
    }

    @Override
    public void run() {
        titleRow.update();
        rows.forEach(ScoreboardRow::update);
        holders.forEach(ScoreboardHolder::update);
    }
}
