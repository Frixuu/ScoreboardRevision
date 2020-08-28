package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import io.github.frixuu.scoreboardrevision.board.App;
import io.github.frixuu.scoreboardrevision.board.ScoreboardHolder;

/**
 * Created by Rien on 23-10-2018.
 */
public class EDeintergrate implements Listener {

    private App app;

    public EDeintergrate(App app)
    {
        this.app = app;
    }

    @EventHandler
    public void Deintergrate(PlayerQuitEvent e)
    {
        if(app == null) return;
        app.unregisterHolder(e.getPlayer());
        e.getPlayer().setScoreboard(Main.empty);
    }

}
