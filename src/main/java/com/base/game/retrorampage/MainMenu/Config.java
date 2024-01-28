package com.base.game.retrorampage.MainMenu;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final String configFilePath;
    private final Map<String, String> settings = new HashMap<>();

    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
        loadSettings();
    }

    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSetting(String key, String value) {
        settings.put(key, value);
        saveSettingsToFile();
    }

    private void saveSettingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, false))) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveResolutionSetting(String resolution) {
        saveSetting("Resolution", resolution);
    }

    public void saveFullScreenSetting(boolean fullscreen) {
        saveSetting("Fullscreen", String.valueOf(fullscreen));
    }

    public void saveVolumeSetting(int volume) {
        saveSetting("Volume", String.valueOf(volume));
    }

    public String loadResolutionSetting() {
        return settings.getOrDefault("Resolution", "800 x 600");
    }

    public boolean loadFullscreenSetting() {
        return Boolean.parseBoolean(settings.getOrDefault("Fullscreen", "false"));
    }

    public int loadVolumeSetting() {
        return Integer.parseInt(settings.getOrDefault("Volume", "50"));
    }
}
