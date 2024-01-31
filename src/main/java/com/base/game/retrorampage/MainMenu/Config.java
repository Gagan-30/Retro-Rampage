package com.base.game.retrorampage.MainMenu;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final String configFilePath;
    private final Map<String, String> settings = new HashMap<>();
    private final Map<String, String> keybinds = new HashMap<>();


    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
        loadSettings();
        loadKeybinds();
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

    private void loadKeybinds() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].startsWith("Keybind_")) {
                    String key = parts[0].substring(8).trim(); //
                    String value = parts[1].trim();
                    keybinds.put(key, value);
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

    public String getKeybind(String action) {
        return keybinds.getOrDefault(action, "None");
    }

    public void saveKeybind(String action, String key) {
        keybinds.put(action, key);
        saveKeybindsToFile();
    }

    private void saveKeybindsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, false))) {
            // First, save the settings
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }

            // Then, save the keybinds
            for (Map.Entry<String, String> entry : keybinds.entrySet()) {
                writer.write("Keybind_" + entry.getKey() + ": " + entry.getValue());
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

    // Method to load individual keybinds
    public String loadMoveUp1Keybind() {
        return keybinds.getOrDefault("MoveUp1", "W");
    }

    public String loadMoveUp2Keybind() {
        return keybinds.getOrDefault("MoveUp2", "UP");
    }

    public String loadMoveDown1Keybind() {
        return keybinds.getOrDefault("MoveDown1", "S");
    }

    public String loadMoveDown2Keybind() {
        return keybinds.getOrDefault("MoveDown2", "DOWN");
    }

    public String loadMoveLeft1Keybind() {
        return keybinds.getOrDefault("MoveLeft1", "A");
    }

    public String loadMoveLeft2Keybind() {
        return keybinds.getOrDefault("MoveLeft2", "LEFT");
    }

    public String loadMoveRight1Keybind() {
        return keybinds.getOrDefault("MoveRight1", "D");
    }

    public String loadMoveRight2Keybind() {
        return keybinds.getOrDefault("MoveRight2", "RIGHT");
    }

    public String loadShoot1Keybind() {
        return keybinds.getOrDefault("Shoot1", "LEFT CLICK");
    }

    public String loadShoot2Keybind() {
        return keybinds.getOrDefault("Shoot2", "SPACE");
    }

    public String loadAim1Keybind() {
        return keybinds.getOrDefault("Aim1", "RIGHT CLICK");
    }

    public String loadAim2Keybind() {
        return keybinds.getOrDefault("Aim2", "");
    }

    public String loadInventory1Keybind() {
        return keybinds.getOrDefault("Inventory1", "TAB");
    }

    public String loadInventory2Keybind() {
        return keybinds.getOrDefault("Inventory2", "");
    }

}
