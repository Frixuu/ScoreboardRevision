package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.board.slimboard.Slimboard;
import io.github.frixuu.scoreboardrevision.services.PlaceholderService;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by Rien on 22-10-2018.
 */
public class ScoreboardHolder {

    private final BoardRunnable boardRunnable;
    private final PlaceholderService placeholderService;
    private final ScoreboardPlugin plugin;

    private final Slimboard slim;
    public Player player;
    @Getter @Setter
    private boolean enabled = true;

    /**
     * Construct a new holder
     *
     * @param boardRunnable
     * @param player
     */
    public ScoreboardHolder(BoardRunnable boardRunnable, ScoreboardPlugin plugin, PlaceholderService placeholderService, FileConfiguration config, Player player) {
        this.boardRunnable = boardRunnable;
        this.placeholderService = placeholderService;
        this.player = player;
        this.plugin = plugin;

        slim = new Slimboard(plugin, config, player, placeholderService, boardRunnable.getRows().size());

        boardRunnable.registerHolder(this);
    }

    /**
     * Update the holder and all the rows
     */
    public void update() {

        if (Session.disabledPlayers.contains(this.player)) {
            if (isEnabled()) {
                player.setScoreboard(ScoreboardPlugin.empty);
                enabled = false;
            }
            return;
        }

        if (Session.reEnablePlayers.contains(this.player)) {
            Session.reEnablePlayers.remove(this.player);
            player.setScoreboard(this.slim.board);
            enabled = true;
        }

        slim.setTitle(boardRunnable.getTitleRow().getCurrentLine());

        int count = 0;
        for (ScoreboardRow row : boardRunnable.getRows()) {
            String line = row.getCurrentLine();
            if (row.containsPlaceholders) {
                if (placeholderService != null && placeholderService.containsPlaceholders(line)) {
                    line = placeholderService.setPlaceholders(player, line);
                }
            }
            slim.setLine(count, line);
            count++;
        }
    }
}
