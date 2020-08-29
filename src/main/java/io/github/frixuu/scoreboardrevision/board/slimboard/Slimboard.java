package io.github.frixuu.scoreboardrevision.board.slimboard;

import lombok.var;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.ChatColor.getLastColors;

/**
 * Created by Rien on 23-10-2018.
 */
public class Slimboard {

    private final Player player;
    private final FileConfiguration config;
    private final Plugin plugin;
    private final Objective objective;

    private final int linecount;
    private final boolean lineCanBeLonger;
    private final HashMap<Integer, String> cache = new HashMap<>();
    public Scoreboard board;

    /**
     * Construct the board
     *
     * @param plugin
     * @param player
     * @param linecount
     */
    public Slimboard(Plugin plugin, FileConfiguration config, Player player, int linecount) {
        this.player = player;
        this.config = config;
        this.plugin = plugin;
        this.linecount = linecount;

        this.lineCanBeLonger = config.getBoolean("settings.longline");

        this.board = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
        this.objective = this.board.registerNewObjective("sb1", "sb2");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName("...");

        int score = linecount;
        for (int i = 0; i < linecount; i++) // Loop through the lines
        {
            Team t = this.board.registerNewTeam(i + ""); // Create the first team
            t.addEntry(ChatColor.values()[i] + ""); // Assign the team with a color

            this.objective.getScore(ChatColor.values()[i] + "").setScore(score); // Set the socre number

            score--; // Lower the score number for the next line
        }

        this.player.setScoreboard(this.board); // Set the board to the player
    }

    /**
     * Set the board title
     *
     * @param string
     */
    public void setTitle(String string) {

        if (string == null) {
            string = "";
        }
        // Check if the PAPI plugin is enabled and the string has a placeholder
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") &&
            PlaceholderAPI.containsPlaceholders(string)) {
            string = PlaceholderAPI.setPlaceholders(player, string); // Run placeholders!
        }

        if (!cache.containsKey(-1) || !cache.get(-1).equals(string)) {
            cache.remove(-1); // Remove it from cache, it is different!
            cache.put(-1, string); // Put this in the cache!
            objective.setDisplayName(string); // And set the title
        }
    }

    /**
     * Set a specific line
     *
     * @param line
     * @param string
     */
    public void setLine(int line, String string) {

        if (string == null) {
            string = "";
        }

        // The line hasn't updated?
        if (cache.containsKey(line) && cache.get(line) == string) {
            return;
        }

        // Line has updated, refresh the cache!
        cache.remove(line);
        cache.put(line, string);

        // Prepare the string to preserve colors
        string = lineCanBeLonger ? prep(string) : prepForShortline(string);
        var allowedLineLength = lineCanBeLonger ? 64 : 16;
        var parts = convertIntoPieces(string, allowedLineLength);

        Team team = board.getTeam((line) + "");
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

    private String prepForShortline(String color) {
        if (color.length() <= 16) {
            return color;
        }
        var pieces = convertIntoPieces(color, 16);
        return pieces.get(0) + "§f" + getLastColors(pieces.get(0)) + pieces.get(1);
    }

    private List<String> convertIntoPieces(String input, int allowedLineLength) {
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
