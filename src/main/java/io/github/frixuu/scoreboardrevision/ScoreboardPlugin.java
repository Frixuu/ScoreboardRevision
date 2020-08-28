package io.github.frixuu.scoreboardrevision;

import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import io.github.frixuu.scoreboardrevision.board.WorldManager;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
    public static void loadBoards() {
        newApp("board", true); // Default board

        for (String s : ConfigControl.get().gc("settings").getStringList("enabled-boards")) {
            Session.plugin.getLogger().info("Attempting to start app-creator for: " + s);
            if (ConfigControl.get().gc("settings").isConfigurationSection(s)) newApp(s, false);
            else
                Session.plugin.getLogger().severe("Tried enabling board '" + s + "', but it does not exist!");
        }
    }

    /**
     * Unload all board drivers
     */
    public static void disolveBoards() {
        for (BoardRunnable boardRunnable : apps.values())
            boardRunnable.cancel();
        apps.clear();
    }

    /**
     * Construct a new app
     *
     * @param board
     * @param isdefault
     */
    public static void newApp(String board, boolean isdefault) {
        BoardRunnable boardRunnable = new BoardRunnable(board);
        if (ConfigControl.get().gc("settings").getBoolean("settings.safe-mode"))
            boardRunnable.runTaskTimer(Session.plugin, 1L, 1L);
        else boardRunnable.runTaskTimerAsynchronously(Session.plugin, 1L, 1L);
        apps.put(board, boardRunnable);
        Session.plugin.getLogger().info("Loaded app handler for board: " + board);
        boardRunnable.isdefault = isdefault;
    }

    @Override
    public void onEnable() {
        init();
    }

    /**
     * Initiate the plugin
     */
    private void init() {
        Session.plugin = this;
        ConfigControl.get().createDataFiles();
        empty = getServer().getScoreboardManager().getNewScoreboard();

        autoloadDependencies();
        registerCommands();
        loadBoards();

        new WorldManager().runTaskTimer(this, 20L, 40L);

        getLogger().info("Hey, we're online! ScoreboardRevision is now running.");
    }

    /**
     * Load in dependencies
     */
    private void autoloadDependencies() {
        for (String dependency : Session.dependencies)
            if (Bukkit.getPluginManager().isPluginEnabled(dependency))
                Session.enabled_dependencies.add(dependency);
    }

    /**
     * Create the commands
     */
    private void registerCommands() {
        Objects.requireNonNull(getCommand("sb")).setExecutor(new CommandManager());
    }

}
