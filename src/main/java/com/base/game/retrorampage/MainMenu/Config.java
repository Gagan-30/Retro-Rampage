package com.base.game.retrorampage.MainMenu;

import java.io.*;
import java.util.*;

// This class is responsible for handling configuration settings and keybindings for the game.
public class Config {
    private final String configFilePath; // Path to the configuration file.
    private final Map<String, String> settings = new HashMap<>(); // Stores application settings.
    private final Map<String, String> keybinds = new HashMap<>(); // Stores user-defined keybindings.
    private static final Map<String, String> defaultKeybinds = new HashMap<>(); // Stores default keybindings.

    // Static initializer block to populate default keybindings.
    static {
        // Default keybindings are defined here.
        // Additional default keybindings follow the same pattern.
        defaultKeybinds.put("MoveUp1", "W");
        defaultKeybinds.put("MoveUp2", "UP");
        defaultKeybinds.put("MoveDown1", "S");
        defaultKeybinds.put("MoveDown2", "DOWN");
        defaultKeybinds.put("MoveLeft1", "A");
        defaultKeybinds.put("MoveLeft2", "LEFT");
        defaultKeybinds.put("MoveRight1", "D");
        defaultKeybinds.put("MoveRight2", "RIGHT");
        defaultKeybinds.put("Shoot1", "Left Click");
        defaultKeybinds.put("Shoot2", "SPACE");
        defaultKeybinds.put("Aim1", "Right Click");
        defaultKeybinds.put("Reload1", "R");
        defaultKeybinds.put("Inventory1", "TAB");
        defaultKeybinds.put("Inventory2", "E");
    }

    // Constructor that initializes the configuration with a file path.
    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
        initializeDefaultKeybinds(); // Loads default keybindings into the keybinds map.
        loadSettings(); // Loads application settings from the configuration file.
        loadKeybinds(); // Loads user-defined keybindings from the configuration file.
    }

    // Initializes keybinds map with default keybindings.
    private void initializeDefaultKeybinds() {
        keybinds.putAll(defaultKeybinds);
    }

    // Loads settings from the configuration file.
    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Splits each line by ":", expecting a key-value pair.
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    // Stores each setting in the settings map.
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads keybindings from the configuration file, similar to loading settings.
    private void loadKeybinds() {
        // Implementation follows a similar pattern as loadSettings, but focuses on keybinds.
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].startsWith("Keybind_")) {
                    String key = parts[0].substring(8).trim();
                    String value = parts[1].trim();
                    keybinds.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Saves a single setting and then writes all settings to the configuration file.
    public void saveSetting(String key, String value) {
        settings.put(key, value);
        saveSettingsToFile();
    }

    // Writes settings and keybindings back to the configuration file.
    public void saveSettingsToFile() {
        // Implementation includes writing each setting and keybinding to the file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, false))) {
            writer.write("Fullscreen: " + settings.getOrDefault("Fullscreen", "false"));
            writer.newLine();
            writer.write("Resolution: " + settings.getOrDefault("Resolution", "800 x 600"));
            writer.newLine();
            writer.write("Volume: " + settings.getOrDefault("Volume", "50"));
            writer.newLine();

            // Save keybindings
            writer.write("MoveUp1: " + keybinds.getOrDefault("MoveUp1", ""));
            writer.newLine();
            writer.write("MoveUp2: " + keybinds.getOrDefault("MoveUp2", ""));
            writer.newLine();
            writer.write("MoveDown1: " + keybinds.getOrDefault("MoveDown1", ""));
            writer.newLine();
            writer.write("MoveDown2: " + keybinds.getOrDefault("MoveDown2", ""));
            writer.newLine();
            writer.write("MoveLeft1: " + keybinds.getOrDefault("MoveLeft1", ""));
            writer.newLine();
            writer.write("MoveLeft2: " + keybinds.getOrDefault("MoveLeft2", ""));
            writer.newLine();
            writer.write("MoveRight1: " + keybinds.getOrDefault("MoveRight1", ""));
            writer.newLine();
            writer.write("MoveRight2: " + keybinds.getOrDefault("MoveRight2", ""));
            writer.newLine();
            writer.write("Shoot1: " + keybinds.getOrDefault("Shoot1", ""));
            writer.newLine();
            writer.write("Shoot2: " + keybinds.getOrDefault("Shoot2", ""));
            writer.newLine();
            writer.write("Aim1: " + keybinds.getOrDefault("Aim1", ""));
            writer.newLine();
            writer.write("Aim2: " + keybinds.getOrDefault("Aim2", ""));
            writer.newLine();
            writer.write("Reload1: " + keybinds.getOrDefault("Reload1", ""));
            writer.newLine();
            writer.write("Reload2: " + keybinds.getOrDefault("Reload2", ""));
            writer.newLine();
            writer.write("Inventory1: " + keybinds.getOrDefault("Inventory1", ""));
            writer.newLine();
            writer.write("Inventory2: " + keybinds.getOrDefault("Inventory2", ""));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retrieves a keybinding for a given action, with a fallback to default if not found.
    public String getKeybind(String action) {
        return keybinds.getOrDefault(action, defaultKeybinds.getOrDefault(action, "None"));
    }

    // Updates a keybinding for a specific action and saves the change.
    public void saveKeybind(String action, String key) {
        keybinds.put(action, key);
        saveSettingsToFile();
    }

    // Removes a keybind if it is already assigned to another action.
    public void removeKeybindIfAssigned(String newKeybind) {
        keybinds.entrySet().removeIf(entry -> entry.getValue().equals(newKeybind));
    }

    // Checks if a keybind is a duplicate, except for the current action being modified.
    public boolean isDuplicateKeybind(String input, String currentAction) {
        // Implementation uses streaming API to check for duplicates.
        return keybinds.entrySet().stream()
                .anyMatch(entry -> entry.getValue().equals(input) && !entry.getKey().equals(currentAction));
    }

    // Removes a keybind for a specific action and saves the configuration.
    public void removeKeybind(String action) {
        keybinds.remove(action);
        saveSettingsToFile(); // Save changes to the file.
    }

    // Convenience methods for saving specific types of settings (resolution, fullscreen, volume).
    // Each method updates the corresponding setting and then saves all settings to the file.

    public void saveResolutionSetting(String resolution) {
        saveSetting("Resolution", resolution);
    }

    public void saveFullScreenSetting(boolean fullscreen) {
        saveSetting("Fullscreen", String.valueOf(fullscreen));
    }

    public void saveVolumeSetting(int volume) {
        saveSetting("Volume", String.valueOf(volume));
    }

    // Methods for loading specific settings (resolution, fullscreen, volume) from the configuration.
    // Each method retrieves the setting value with a default if not found.
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
