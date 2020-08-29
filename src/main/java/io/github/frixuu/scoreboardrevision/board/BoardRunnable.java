package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.board.events.PlayerJoinListener;
import io.github.frixuu.scoreboardrevision.board.events.PlayerQuitListener;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.Getter;
import lombok.var;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

/**
 * Created by Rien on 21-10-2018.
 */
public class BoardRunnable extends BukkitRunnable {

    @Getter private final ScoreboardRow titleRow;
    @Getter private final ArrayList<ScoreboardRow> rows = new ArrayList<>();
    public ArrayList<ScoreboardHolder> holders = new ArrayList<>();
    public String board;
    public boolean isdefault = true;

    /**
     * Construct a new board driver
     *
     * @param board
     */
    public BoardRunnable(String board, Server server, ScoreboardPlugin plugin) {
        var config = ConfigControl.get().getConfig("settings");
        this.board = board; // What is the current board?

        //Events
        var pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this, plugin, config), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(this), plugin);

        // Setup title row
        var sectionTitle = config.getConfigurationSection(board + ".title");
        var linesTitle = sectionTitle.getStringList("liner"); // Get the lines
        var intervalTitle = config.getInt(board + ".title.interval"); // Get the intervals
        titleRow = new ScoreboardRow(linesTitle, intervalTitle); // Create the title row!

        for (int i = 1; i < 200; i++) {
            var section = config.getConfigurationSection(board + ".rows." + i); // Get their rows
            if (section != null) {
                var interval = section.getInt("interval");
                var lines = section.getStringList("liner");
                rows.add(new ScoreboardRow(lines, interval));
            }
        }

        // Register already joined players
        if (board.equals("board")) {
            server.getOnlinePlayers()
                .forEach(player -> new ScoreboardHolder(this, plugin, config, player));
        }
    }

    /**
     * Register a scoreboardholder
     *
     * @param holder
     */
    public void registerHolder(ScoreboardHolder holder) {
        holders.add(holder);
    }

    /**
     * Unregister a holder
     *
     * @param holder
     */
    public void unregisterHolder(ScoreboardHolder holder) {
        holders.remove(holder);
    }

    /**
     * Unregister a holder via player
     *
     * @param player
     */
    public void unregisterHolder(Player player) {
        holders.stream()
            .filter(holder -> holder.player == player)
            .findFirst()
            .ifPresent(holder -> holders.remove(holder));
    }

    @Override
    public void run() {
        titleRow.update();
        rows.forEach(ScoreboardRow::update);
        holders.forEach(ScoreboardHolder::update);
    }
}
