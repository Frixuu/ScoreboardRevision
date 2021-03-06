package io.github.frixuu.scoreboardrevision.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Rien on 23-10-2018.
 */
public final class ChatUtils {

    private static final String prefixLong = "&cScoreboard: &7";
    private static final String prefixShort = "&c[SB] &7";

    public static void sendPrefixedMessage(CommandSender player, String message) {
        player.sendMessage(color(prefixLong + message));
    }

    public static void sendShortPrefixedMessage(CommandSender player, String message) {
        player.sendMessage(color(prefixShort + message));
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
