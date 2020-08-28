package io.github.frixuu.scoreboardrevision.board.events;

import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.board.App;
import io.github.frixuu.scoreboardrevision.board.ScoreboardHolder;
import io.github.frixuu.scoreboardrevision.util.Func;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Rien on 22-10-2018.
 */
public class EIntergrate implements Listener {

    private final App app;

    public EIntergrate(App app) {
        this.app = app;
    }

    @EventHandler
    public void Intergrate(PlayerJoinEvent e) {

        if (app == null || !app.isdefault) return;
        if (e.getPlayer().isOp() && !Session.isuptodate)
            e.getPlayer().sendMessage(Func.color("&cYou are running an outdated version of Scoreboard, please update as soon as possible for performance gain, security- or bugfixes."));
        new ScoreboardHolder(app, e.getPlayer());
    }

}
