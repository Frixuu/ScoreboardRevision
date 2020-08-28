package io.github.frixuu.scoreboardrevision.board;

import io.github.frixuu.scoreboardrevision.utils.ChatUtils;

import java.util.ArrayList;

/**
 * Created by Rien on 21-10-2018.
 */
public class Row {

    private final int interval;
    private final ArrayList<String> lines;
    // Rules
    public boolean static_line = false;
    public boolean placeholders = false;
    public boolean active = false;
    private String line;
    private int current = 1;
    private int count = 0;

    /**
     * Construct the row
     *
     * @param lines
     * @param interval
     */
    public Row(ArrayList<String> lines, int interval) {
        this.lines = lines;
        this.interval = interval;

        if (lines.size() == 1)
            static_line = true;
        for (String line : lines)
            if (line.contains("%")) placeholders = true;

        if (static_line)
            if (lines.size() < 1)
                line = "";
            else
                line = ChatUtils.color(lines.get(0));


        line = ChatUtils.color(lines.get(0));
    }

    /**
     * Update a line
     */
    public void update() {
        if (static_line) return;
        active = true;
        if (count >= interval) {
            count = 0;
            current++;
            if (current >= lines.size())
                current = 0;
            line = ChatUtils.color(lines.get(current));
        } else {
            count++;
        }
    }


    /**
     * Get  the last animated line
     *
     * @return
     */
    public String getLine() {
        return this.line;
    }

}
