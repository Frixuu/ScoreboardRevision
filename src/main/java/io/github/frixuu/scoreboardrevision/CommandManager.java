package io.github.frixuu.scoreboardrevision;

import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import io.github.frixuu.scoreboardrevision.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.frixuu.scoreboardrevision.utils.ChatUtils.sendShortPrefixedMessage;

/**
 * Created by Rien on 23-10-2018.
 */
@RequiredArgsConstructor
public class CommandManager implements CommandExecutor {

    private final ScoreboardPlugin plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player-only command!");
        } else {

            Player player = (Player) commandSender;

            if (args.length < 1) {
                ChatUtils.sendPrefixedMessage(player, "Too few arguments!");
                help(player);
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("scoreboard.reload")) {
                        plugin.disolveBoards();
                        ConfigControl.get().reloadConfigs();
                        plugin.loadBoards();
                        sendShortPrefixedMessage(player, "Scoreboard reloaded");
                    } else {
                        sendShortPrefixedMessage(player, "You lack the permission &cscoreboard.reload");
                    }
                } else {
                    ChatUtils.sendPrefixedMessage(player, "Unknown command!");
                    help(player);
                }
            }
        }

        return false;
    }

    private void help(Player player) {
        sendShortPrefixedMessage(player, "/sb reload (Reload config and application)");
    }
}
