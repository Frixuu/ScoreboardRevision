package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import io.github.frixuu.scoreboardrevision.board.ScoreboardHolder;
import io.github.frixuu.scoreboardrevision.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Rien on 22-10-2018.
 */
public class EIntergrate implements Listener {

    private final BoardRunnable boardRunnable;

    public EIntergrate(BoardRunnable boardRunnable) {
        this.boardRunnable = boardRunnable;
    }

    @EventHandler
    public void Intergrate(PlayerJoinEvent e) {
        if (boardRunnable != null && boardRunnable.isdefault) {
            new ScoreboardHolder(boardRunnable, e.getPlayer());
        }
    }
}
