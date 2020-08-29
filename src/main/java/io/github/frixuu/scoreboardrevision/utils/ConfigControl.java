package io.github.frixuu.scoreboardrevision.utils;


import io.github.frixuu.scoreboardrevision.ScoreboardPlugin;
import lombok.Setter;
import lombok.var;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.HashMap;

import static io.github.frixuu.scoreboardrevision.utils.ResourceUtils.getResourceAsReader;

/**
 * Created by Rien on 21-10-2018.
 */
public class ConfigControl {

    private final ScoreboardPlugin plugin;

    @Setter private static ConfigControl instance = null;
    private final HashMap<String, FileConfiguration> designations = new HashMap<>();

    public ConfigControl(ScoreboardPlugin plugin) {
        this.plugin = plugin;
        this.createDataFiles();
    }

    public static ConfigControl get() {
        assert instance != null;
        return instance;
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

    public void createConfigFile(String name) {
        var path = name + ".yml";
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
            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                Reader defConfigStream = getResourceAsReader(path);
                writer.print(read(defConfigStream));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        designations.put(name, fileConfig);
    }


    public void reloadConfigs() {
        this.purge();
        this.createDataFiles();
    }

    public FileConfiguration getConfig(String fc) {
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
