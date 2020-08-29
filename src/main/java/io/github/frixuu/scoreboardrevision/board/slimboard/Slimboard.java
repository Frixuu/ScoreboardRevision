package io.github.frixuu.scoreboardrevision.board.slimboard;

import lombok.Getter;
import lombok.var;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.bukkit.ChatColor.getLastColors;

/**
 * A wrapper for raw Minecraft scoreboard for easier row updates and placeholder support.
 * @author Rien
 */
public class Slimboard {

    private final Player player;
    private final Plugin plugin;
    private final Objective objective;

    @Getter private final int rowCount;
    private final boolean lineCanBeLonger;
    private final Map<Integer, String> rowCache = new HashMap<>();
    public Scoreboard board;

    public Slimboard(Plugin plugin, FileConfiguration config, Player player, final int size) {
        this.player = player;
        this.plugin = plugin;
        this.rowCount = size;

        lineCanBeLonger = config.getBoolean("settings.longline");

        var scoreboardManager = Objects.requireNonNull(plugin.getServer().getScoreboardManager());
        board = scoreboardManager.getNewScoreboard();

        objective = board.registerNewObjective("sb1", "sb2", "...");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        IntStream.range(0, size).forEachOrdered(i -> {
            var color = ChatColor.values()[i].toString();
            var team = this.board.registerNewTeam(String.valueOf(i));
            team.addEntry(color);
            // In Minecraft, scoreboard elements are shown in the descending order
            objective.getScore(color).setScore(size - i);
        });

        player.setScoreboard(board); // Set the board to the player
    }

    public void setTitle(String title) {

        if (title == null) {
            title = "";
        }
        // Check if the PAPI plugin is enabled and the string has a placeholder
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") &&
            PlaceholderAPI.containsPlaceholders(title)) {
            title = PlaceholderAPI.setPlaceholders(player, title); // Run placeholders!
        }

        if (!rowCache.containsKey(-1) || !rowCache.get(-1).equals(title)) {
            rowCache.remove(-1); // Remove it from cache, it is different!
            rowCache.put(-1, title); // Put this in the cache!
            objective.setDisplayName(title); // And set the title
        }
    }

    public void setLine(int rowNum, String value) {

        if (value == null) {
            value = "";
        }

        // The line hasn't updated?
        if (rowCache.containsKey(rowNum) && rowCache.get(rowNum).equals(value)) {
            return;
        }

        // Line has updated, refresh the cache!
        rowCache.remove(rowNum);
        rowCache.put(rowNum, value);

        // Prepare the string to preserve colors
        value = lineCanBeLonger ? prep(value) : prepForShortline(value);
        var allowedLineLength = lineCanBeLonger ? 64 : 16;
        var parts = convertIntoPieces(value, allowedLineLength);

        var team = Objects.requireNonNull(board.getTeam(String.valueOf(rowNum)));

        if (lineCanBeLonger) {
            team.setPrefix(parts.get(0));
            team.setSuffix(parts.get(1));
        } else {
            team.setPrefix(trimIfNecessary(parts.get(0)));
            team.setSuffix(trimIfNecessary(parts.get(1)));
        }
    }


    private static String trimIfNecessary(String part) {
        return part.length() <= 16 ? part : part.substring(16);
    }

    private String prep(String color) {
        var allowedLength = lineCanBeLonger ? 64 : 15;
        var parts = convertIntoPieces(color, allowedLength);
        return parts.get(0) + "§f" + getLastColors(parts.get(0)) + parts.get(1);
    }

    private static String prepForShortline(String color) {
        if (color.length() <= 16) {
            return color;
        }
        var pieces = convertIntoPieces(color, 16);
        return pieces.get(0) + "§f" + getLastColors(pieces.get(0)) + pieces.get(1);
    }

    private static List<String> convertIntoPieces(String input, int allowedLineLength) {
        if (ChatColor.stripColor(input).length() <= allowedLineLength) {
            return Arrays.asList(input, "");
        }

        var first = input.substring(0, allowedLineLength);
        var second = input.substring(allowedLineLength);
        if (second.length() > allowedLineLength) {
            second = second.substring(0, allowedLineLength);
        }
        return Arrays.asList(first, second);
    }
}
