package io.github.frixuu.scoreboardrevision.services;

import org.bukkit.entity.Player;

public interface PlaceholderService {
    String setPlaceholders(Player player, String input);
    boolean containsPlaceholders(String input);
}
