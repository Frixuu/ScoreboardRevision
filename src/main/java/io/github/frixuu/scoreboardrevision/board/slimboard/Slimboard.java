package io.github.frixuu.scoreboardrevision.board.slimboard;

import io.github.frixuu.scoreboardrevision.Session;
import io.github.frixuu.scoreboardrevision.board.BoardRunnable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rien on 23-10-2018.
 */
public class Slimboard {

    private final Player player;
    private final Plugin plugin;
    private final Objective objective;
    private final int linecount;
    private final HashMap<Integer, String> cache = new HashMap<>();
    public Scoreboard board;

    /**
     * Construct the board
     *
     * @param plugin
     * @param player
     * @param linecount
     */
    public Slimboard(Plugin plugin, Player player, int linecount) {
        this.player = player;
        this.plugin = plugin;
        this.linecount = linecount;
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
        if (string == null) string = ""; // Is there no title? Make it empty!
        // Check if the PAPI plugin is enabled and the string has a placeholder
        if (Session.enabled_dependencies.contains(Session.dependencies[0]) && org.bukkit.Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") &&
            PlaceholderAPI.containsPlaceholders(string)) {
            string = PlaceholderAPI.setPlaceholders(player, string); // Run placeholders!
        }

        if (cache.containsKey(-1) && cache.get(-1) == string) return; // Is it in cache?
        cache.remove(-1); // Remove it from cache, it is different!
        cache.put(-1, string); // Put this in the cache!
        objective.setDisplayName(string); // And set the title
    }

    /**
     * Set a specific line
     *
     * @param line
     * @param string
     */
    public void setLine(int line, String string) {
        Team t = board.getTeam((line) + ""); // Get the team we use
        if (string == null) string = ""; // Line null? No problem, make it empty!

        if (cache.containsKey(line) && cache.get(line) == string)
            return; // The line hasn't updated?
        cache.remove(line); // Line has updated, remove it from cache!
        cache.put(line, string); // Put the new line in the cache

        if (BoardRunnable.longline) string = prep(string);
        else string = prepForShortline(string); // Prepare the string to preserve colors
        ArrayList<String> parts = null;
        if (BoardRunnable.longline) parts = convertIntoPieces(string, 64);
        else parts = convertIntoPieces(string, 16); // Convert it into pieces!

        t.setPrefix(fixAnyIssues(parts.get(0))); // Set the first
        t.setSuffix(fixAnyIssues(parts.get(1))); // Set the scond
    }


    /*
    Parter
     */

    private String fixAnyIssues(String part) {
        if (BoardRunnable.longline) {
            return part;
        } else {
            if (part.length() > 16) {
                return part.substring(16);
            } else {
                return part;
            }
        }
    }

    private String prep(String color) {
        ArrayList<String> parts = null;
        if (BoardRunnable.longline) parts = convertIntoPieces(color, 64);
        else parts = convertIntoPieces(color, 15);
        return parts.get(0) + "§f" + getLastColor(parts.get(0)) + parts.get(1);
    }

    private String prepForShortline(String color) {
        if (color.length() > 16) {
            ArrayList<String> pieces = convertIntoPieces(color, 16);
            return pieces.get(0) + "§f" + getLastColor(pieces.get(0)) + pieces.get(1);
        }
        return color;
    }

    private String getLastColor(String s) {
        String last = ChatColor.getLastColors(s);
        if (last == null)
            return "";
        return last;
    }

    private ArrayList<String> convertIntoPieces(String s, int allowed_line_size) {
        ArrayList<String> parts = new ArrayList<>();

        if (ChatColor.stripColor(s).length() > allowed_line_size) {
            parts.add(s.substring(0, allowed_line_size));

            String s2 = s.substring(allowed_line_size);
            if (s2.length() > allowed_line_size)
                s2 = s2.substring(0, allowed_line_size);
            parts.add(s2);
        } else {
            parts.add(s);
            parts.add("");
        }

        return parts;
    }

//    private ArrayList<String> getPartsForShortline(String s)
//    {
//
//        ArrayList<String> parts = new ArrayList<>();
//
//        if(ChatColor.stripColor(s).length() > 16)
//        {
//            parts.add(s.substring(0, 16));
//
//            String s2 = s.substring(16, s.length());
//            if(s2.length() > 16)
//                s2 = s2.substring(0, 16);
//            parts.add(s2);
//        } else {
//            parts.add(s);
//            parts.add("");
//        }
//
//        return parts;
//    }
//    private ArrayList<String> getPartsForLongline(String s)
//    {
//
//        ArrayList<String> parts = new ArrayList<>();
//
//        if(ChatColor.stripColor(s).length() > 64)
//        {
//            parts.add(s.substring(0, 64));
//
//            String s2 = s.substring(64, s.length());
//            if(s2.length() > 64)
//                s2 = s2.substring(0, 64);
//            parts.add(s2);
//        } else {
//            parts.add(s);
//            parts.add("");
//        }
//
//        return parts;
//
//    }

}
