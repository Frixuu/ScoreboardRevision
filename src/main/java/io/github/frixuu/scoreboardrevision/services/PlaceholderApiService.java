package io.github.frixuu.scoreboardrevision.services;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderApiService implements PlaceholderService {

    @Override
    public String setPlaceholders(Player player, String input) {
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    @Override
    public boolean containsPlaceholders(String input) {
        return PlaceholderAPI.containsPlaceholders(input);
    }
}
