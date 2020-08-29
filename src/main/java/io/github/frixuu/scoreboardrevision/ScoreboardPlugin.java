package io.github.frixuu.scoreboardrevision;

import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import io.github.frixuu.scoreboardrevision.board.WorldManager;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.var;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Rien on 21-10-2018.
 */
public class ScoreboardPlugin extends JavaPlugin {

    public static Scoreboard empty;

    public static HashMap<String, BoardRunnable> apps = new HashMap<>();

    /**
     * Load in all board drivers
     */
    public void loadBoards() {
        var config = ConfigControl.get().getConfig("settings");
        newApp(getServer(), "board", true); // Default board
        config.getStringList("enabled-boards").forEach(board -> {
            getLogger().info("Attempting to start app-creator for: " + board);
            if (config.isConfigurationSection(board)) {
                newApp(getServer(), board, false);
            } else {
                getLogger().severe("Tried enabling board '" + board + "', but it does not exist!");
            }
        });
    }

    /**
     * Unload all board drivers
     */
    public static void disolveBoards() {
        apps.values().forEach(BukkitRunnable::cancel);
        apps.clear();
    }

    /**
     * Construct a new app
     *
     * @param board
     * @param isdefault
     */
    public void newApp(Server server, String board, boolean isdefault) {
        var boardRunnable = new BoardRunnable(board, server, this);
        var safeMode = ConfigControl.get().getConfig("settings").getBoolean("settings.safe-mode", true);
        if (safeMode) {
            boardRunnable.runTaskTimer(this, 1L, 1L);
        } else {
            boardRunnable.runTaskTimerAsynchronously(this, 1L, 1L);
        }
        apps.put(board, boardRunnable);
        getLogger().info("Loaded app handler for board: " + board);
        boardRunnable.isdefault = isdefault;
    }

    @Override
    public void onEnable() {
        var config = new ConfigControl(this);
        ConfigControl.setInstance(config);
        config.createDataFiles();

        var scoreboardManager = getServer().getScoreboardManager();
        empty = scoreboardManager.getNewScoreboard();

        registerCommands();
        loadBoards();

        new WorldManager(this.getServer(), config).runTaskTimer(this, 20L, 40L);

        getLogger().info("Hey, we're online! ScoreboardRevision is now running.");
    }

    /**
     * Create the commands
     */
    private void registerCommands() {
        Objects.requireNonNull(getCommand("sb")).setExecutor(new ScoreboardCommand(this));
    }

}
