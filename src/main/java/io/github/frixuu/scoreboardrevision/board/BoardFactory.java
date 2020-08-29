package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import lombok.RequiredArgsConstructor;
import lombok.var;

@RequiredArgsConstructor
public class BoardFactory {

    private final ScoreboardPlugin plugin;

    /**
     * Creates and schedules a new board task.
     */
    public BoardRunnable create(String key, boolean isDefault, boolean runAsync) {
        var boardTask = new BoardRunnable(key, plugin.getServer(), plugin);

        if (runAsync) {
            boardTask.runTaskTimerAsynchronously(plugin, 1L, 1L);
        } else {
            boardTask.runTaskTimer(plugin, 1L, 1L);
        }

        boardTask.isdefault = isDefault;
        return boardTask;
    }
}
