package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.board.events.PlayerQuitListener;
import io.github.frixuu.scoreboardrevision.board.events.PlayerJoinListener;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.Getter;
import lombok.var;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rien on 21-10-2018.
 */
public class BoardRunnable extends BukkitRunnable {

    public static boolean longline = false;
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
        // conf
        BoardRunnable.longline = ConfigControl.get().gc("settings").getBoolean("settings.longline"); // Are we in longline?
        this.board = board; // What is the current board?

        //Events
        var pluginManager = server.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(this), plugin);

        // Setup title row
        List<String> lines = ConfigControl.get().gc("settings").getConfigurationSection(board + ".title").getStringList("liner"); // Get the lines
        int interval = ConfigControl.get().gc("settings").getInt(board + ".title.interval"); // Get the intervals
        titleRow = new ScoreboardRow((ArrayList<String>) lines, interval); // Create the title row!

        for (int i = 1; i < 200; i++) // Loop over all lines
        {
            ConfigurationSection section = ConfigControl.get().gc("settings").getConfigurationSection(board + ".rows." + i); // Get their rows
            if (null != section) // Is the section null?
            {
                ScoreboardRow row = new ScoreboardRow((ArrayList<String>) section.getStringList("liner"), section.getInt("interval")); // Create a new row
                rows.add(row); // Add this line to the row list
            }
        }

        // Register already joined players
        if (board.equals("board")) {
            server.getOnlinePlayers()
                .forEach(player -> new ScoreboardHolder(this, player));
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
        for (ScoreboardHolder holder : holders)
            if (holder.player == player) {
                holders.remove(holder);
                break;
            }
    }

    @Override
    public void run() {
        titleRow.update();
        rows.forEach(ScoreboardRow::update);
        holders.forEach(ScoreboardHolder::update);
    }
}
