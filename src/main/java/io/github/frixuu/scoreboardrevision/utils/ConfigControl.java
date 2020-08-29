package io.github.frixuu.scoreboardrevision.utils;


import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Created by Rien on 21-10-2018.
 */
public class ConfigControl {

    private final ScoreboardPlugin plugin;

    private static ConfigControl instance = null;
    private final HashMap<String, FileConfiguration> designations = new HashMap<>();

    public ConfigControl(ScoreboardPlugin plugin) {
        this.plugin = plugin;
        this.createDataFiles();
    }

    public static ConfigControl get() {
        assert instance != null;
        return instance;
    }

    public static void setInstance(ConfigControl cc) {
        instance = cc;
    }

    public void createDataFiles() {

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        createConfigFile("settings.yml");
    }

    public void purge() {
        designations.clear();
    }

    public void createConfigFile(String path) {
        File file = new File(plugin.getDataFolder(), path);

        boolean needCopyDefaults = false;

        try {
            if (!file.exists()) {
                file.createNewFile();
                needCopyDefaults = true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (needCopyDefaults) {
            try {
                Reader defConfigStream = new InputStreamReader(ConfigControl.class.getResourceAsStream("/" + path + ".yml"), StandardCharsets.UTF_8);
                PrintWriter writer = new PrintWriter(file, "UTF-8");
                writer.print(read(defConfigStream));
                writer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        designations.put(path, fileConfig);
    }


    public void reloadConfigs() {
        this.purge();
        this.createDataFiles();
    }

    public FileConfiguration gc(String fc) {
        return designations.get(fc);
    }

    public String read(Reader reader) throws IOException {
        char[] arr = new char[8 * 1024];
        StringBuilder buffer = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
            buffer.append(arr, 0, numCharsRead);
        }
        reader.close();
        return buffer.toString();
    }
}
