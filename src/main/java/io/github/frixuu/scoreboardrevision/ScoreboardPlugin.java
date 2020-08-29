package io.github.frixuu.scoreboardrevision;

import io.github.frixuu.scoreboardrevision.board.BoardFactory;
import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import io.github.frixuu.scoreboardrevision.board.WorldManager;
import io.github.frixuu.scoreboardrevision.services.PlaceholderApiService;
import io.github.frixuu.scoreboardrevision.services.PlaceholderService;
import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.var;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Rien on 21-10-2018.
 */
public class ScoreboardPlugin extends JavaPlugin {

    private BoardFactory boardFactory;

    public static Scoreboard empty;

    public static Map<String, BoardRunnable> boards = new HashMap<>();

    /**
     * Load in all board drivers
     */
    public void loadBoards() {
        var config = ConfigControl.get().getConfig("settings");
        var safeMode = config.getBoolean("settings.safe-mode", true);

        createAndRegisterBoard("board", true, !safeMode); // Default board
        config.getStringList("enabled-boards").forEach(board -> {
            getLogger().info("Attempting to start board-creator for: " + board);
            if (config.isConfigurationSection(board)) {
                createAndRegisterBoard(board, false, !safeMode);
            } else {
                getLogger().severe("Tried enabling board '" + board + "', but it does not exist!");
            }
        });
    }

    public void unregisterAllBoards() {
        boards.values().forEach(BukkitRunnable::cancel);
        boards.clear();
    }

    /**
     * Constructs a new board.
     */
    public void createAndRegisterBoard(String boardKey, boolean isDefault, boolean async) {
        var boardTask = boardFactory.create(boardKey, isDefault, async);
        boards.put(boardKey, boardTask);
        getLogger().info("Loaded board handler for board: " + boardKey);
    }

    @Override
    public void onEnable() {
        PlaceholderService placeholderService = null;
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderService = new PlaceholderApiService();
            getLogger().info("PlaceholderAPI is present! Registered support for placeholders.");
        } else {
            getLogger().warning("PlaceholderAPI is not enabled on this server! Placeholder support is disabled.");
        }

        boardFactory = new BoardFactory(this, placeholderService);

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

    public void reloadBoards() {
        unregisterAllBoards();
        ConfigControl.get().reloadConfigs();
        loadBoards();
    }
}
