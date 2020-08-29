package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Rien on 23-10-2018.
 */
@RequiredArgsConstructor
public class PlayerQuitListener implements Listener {

    private final BoardRunnable boardRunnable;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        boardRunnable.unregisterHolder(e.getPlayer());
        e.getPlayer().setScoreboard(ScoreboardPlugin.empty);
    }
}
