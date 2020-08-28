package io.github.frixuu.scoreboardrevision.triggers;

import io.github.frixuu.scoreboardrevision.Main;
import io.github.frixuu.scoreboardrevision.board.App;
import io.github.frixuu.scoreboardrevision.board.ScoreboardHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Rien on 24-10-2018.
 */
public class onCombat implements Listener {

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent e)
    {
        if(e.getDamager() instanceof Player)
        {
            Player damager = (Player) e.getDamager();
            damager.setScoreboard(Main.empty);
            for(App app : Main.apps.values())
                app.unregisterHolder(damager);

            Main.apps.get("combat").registerHolder(new ScoreboardHolder(Main.apps.get("combat"), damager));
        }
    }

}
