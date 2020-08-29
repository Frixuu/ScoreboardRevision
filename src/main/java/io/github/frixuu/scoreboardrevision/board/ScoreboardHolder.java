package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.board.slimboard.Slimboard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Rien on 22-10-2018.
 */
public class ScoreboardHolder {

    private final BoardRunnable boardRunnable;
    private final ScoreboardPlugin plugin;

    private final Slimboard slim;
    public Player player;
    private boolean disabled = false;

    /**
     * Construct a new holder
     *
     * @param boardRunnable
     * @param player
     */
    public ScoreboardHolder(BoardRunnable boardRunnable, ScoreboardPlugin plugin, Player player) {
        this.boardRunnable = boardRunnable;
        this.player = player;
        this.plugin = plugin;

        slim = new Slimboard(plugin, player, boardRunnable.getRows().size());

        boardRunnable.registerHolder(this);
    }

    /**
     * Update the holder and all the rows
     */
    public void update() {

        if (Session.disabled_players.contains(this.player)) {
            if (!disabled)
                this.player.setScoreboard(ScoreboardPlugin.empty);
            disabled = true;
            return;
        } else if (Session.re_enable_players.contains(this.player)) {
            disabled = false;
            this.player.setScoreboard(this.slim.board);
            Session.re_enable_players.remove(this.player);
        }

        slim.setTitle(boardRunnable.getTitleRow().getCurrentLine());

        int count = 0;
        for (ScoreboardRow row : boardRunnable.getRows()) {
            String line = row.getCurrentLine();
            if (row.containsPlaceholders) {
                // Check if the PAPI plugin is enabled and the string has a placeholder
                if (Session.enabled_dependencies.contains(Session.dependencies[0]) && org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") &&
                    PlaceholderAPI.containsPlaceholders(line)) {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                }
            }
            slim.setLine(count, line);
            count++;
        }
    }
}
