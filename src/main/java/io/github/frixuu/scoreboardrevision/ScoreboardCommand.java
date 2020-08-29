package io.github.frixuu.scoreboardrevision;

import io.github.frixuu.scoreboardrevision.utils.ConfigControl;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.frixuu.scoreboardrevision.utils.ChatUtils.sendPrefixedMessage;
import static io.github.frixuu.scoreboardrevision.utils.ChatUtils.sendShortPrefixedMessage;

/**
 * Created by Rien on 23-10-2018.
 */
@RequiredArgsConstructor
public class ScoreboardCommand implements CommandExecutor {

    private final ScoreboardPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player-only command!");
            return true;
        }

        if (args.length < 1) {
            sendPrefixedMessage(sender, "Too few arguments!");
            showUsageInstructions(sender);
            return true;
        }

        Player player = (Player) sender;
        var subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "reload":
                if (player.hasPermission("scoreboard.reload")) {
                    plugin.disolveBoards();
                    ConfigControl.get().reloadConfigs();
                    plugin.loadBoards();
                    sendShortPrefixedMessage(player, "Scoreboard reloaded");
                } else {
                    sendShortPrefixedMessage(player, "You lack the permission &cscoreboard.reload");
                }
                break;
            default:
                sendPrefixedMessage(player, "Unknown command!");
                showUsageInstructions(player);
                break;
        }

        return true;
    }

    private void showUsageInstructions(CommandSender player) {
        sendShortPrefixedMessage(player, "/sb reload (Reload config and application)");
    }
}
