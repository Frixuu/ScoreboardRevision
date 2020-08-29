package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.utils.ChatUtils;
import lombok.Getter;

import java.util.List;

/**
 * Created by Rien on 21-10-2018.
 */
public class ScoreboardRow {

    private final int interval;
    private final List<String> lines;
    private final boolean isStatic;
    public final boolean containsPlaceholders;
    @Getter private String currentLine;
    private int currentIndex = 0;
    private int ticksSinceLastChange = 0;

    /**
     * Construct the row.
     * @param lines Variants of the row.
     * @param interval How often will the row change between provided lines, measured in ticks.
     */
    public ScoreboardRow(List<String> lines, int interval) {

        if (lines.isEmpty()) {
            lines.add("");
        }

        this.lines = lines;
        this.interval = interval;

        isStatic = lines.size() == 1;
        containsPlaceholders = lines.stream().anyMatch(line -> line.contains("%"));

        currentLine = ChatUtils.color(lines.get(0));
    }

    /**
     * Updates a line. This should be called every tick.
     */
    public void update() {
        update(1);
    }

    public void update(int ticksPassed) {

        if (isStatic) {
            return;
        }

        ticksSinceLastChange += ticksPassed;

        if (ticksSinceLastChange >= interval) {
            ticksSinceLastChange -= interval;
            currentIndex++;
            if (currentIndex >= lines.size()) {
                currentIndex = 0;
            }
            currentLine = ChatUtils.color(lines.get(currentIndex));
        }
    }
}
