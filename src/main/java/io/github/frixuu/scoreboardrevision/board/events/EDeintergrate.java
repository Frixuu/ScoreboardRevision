package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Rien on 23-10-2018.
 */
public class EDeintergrate implements Listener {

    private final BoardRunnable boardRunnable;

    public EDeintergrate(BoardRunnable boardRunnable) {
        this.boardRunnable = boardRunnable;
    }

    @EventHandler
    public void Deintergrate(PlayerQuitEvent e) {
        if (boardRunnable == null) return;
        boardRunnable.unregisterHolder(e.getPlayer());
        e.getPlayer().setScoreboard(ScoreboardPlugin.empty);
    }

}
