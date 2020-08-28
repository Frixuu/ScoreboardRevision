package io.github.frixuu.scoreboardrevision;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Rien on 3-12-2018.
 */
public class Session {

    // Dependencies
    // PlaceholderAPI = dependency ID 0
    public static String[] dependencies = {"PlaceholderAPI"};
    public static ArrayList<String> enabled_dependencies = new ArrayList<>();

    // Objects
    public static ScoreboardPlugin plugin = null;

    // Bools
    public static boolean isuptodate = false;

    // Blocked users
    public static ArrayList<Player> disabled_players = new ArrayList<>();
    public static ArrayList<Player> re_enable_players = new ArrayList<>();
}
