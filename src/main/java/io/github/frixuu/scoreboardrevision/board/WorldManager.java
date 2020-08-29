package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class WorldManager extends BukkitRunnable {

    private final List<String> disabledWorlds;
    private final Server server;

    public WorldManager(Server server, ConfigControl config) {
        this.server = server;
        disabledWorlds = config.gc("settings").getStringList("disabled-worlds")
            .stream()
            .map(world -> world.toLowerCase().trim())
            .collect(Collectors.toList());
    }

    @Override
    public void run() {
        server.getOnlinePlayers().forEach(player -> {
            if (disabledWorlds.contains(player.getWorld().getName().toLowerCase().trim())) {
                if (!Session.disabledPlayers.contains(player))
                    Session.disabledPlayers.add(player);
            } else {
                Session.disabledPlayers.remove(player);
                if (!Session.reEnablePlayers.contains(player))
                    Session.reEnablePlayers.add(player);
            }
        });
    }
}
