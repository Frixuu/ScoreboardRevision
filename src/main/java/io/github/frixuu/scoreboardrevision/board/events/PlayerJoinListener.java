package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import io.github.frixuu.scoreboardrevision.board.ScoreboardHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Rien on 22-10-2018.
 */
public class PlayerJoinListener implements Listener {

    private final BoardRunnable boardRunnable;

    public PlayerJoinListener(BoardRunnable boardRunnable) {
        this.boardRunnable = boardRunnable;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (boardRunnable != null && boardRunnable.isdefault) {
            new ScoreboardHolder(boardRunnable, e.getPlayer());
        }
    }
}
