// Package declaration aligns with the application's structure.
package com.base.game.retrorampage.MainMenu;

// Necessary Java I/O and utility imports for handling file operations and data structures.

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {
    // A static map to store default keybindings. This is populated in a static initializer block,
    // ensuring that default keybindings are available to all instances of this class.
    private static final Map<String, String> defaultKeybinds = new HashMap<>();

    static {
        // Static initializer block to populate the defaultKeybinds map with default keybindings.
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

    // Path to the configuration file where settings and keybindings are stored.
    private final String configFilePath;
    // Maps to store application settings and keybindings. The settings map stores general application settings,
    // while the keybinds map stores user-defined keybindings.
    private final Map<String, String> settings = new HashMap<>();
    private final Map<String, String> keybinds = new HashMap<>();

    // Constructor that initializes the Config object with a specific file path for the configuration file.
    // It loads default keybindings, settings, and keybinds from the configuration file.
    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
        initializeDefaultKeybinds();
        loadKeybinds();
        loadSettings();
    }

    // Initializes the keybinds map with default keybindings from the defaultKeybinds map.
    private void initializeDefaultKeybinds() {
        keybinds.putAll(defaultKeybinds);
    }

    // Loads application settings from the configuration file. Each setting is expected to be in a "key: value" format.
    private void loadSettings() {
        // Implementation for loading settings from the configuration file.
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

    // Loads user-defined keybindings from the configuration file, similar to how settings are loaded.
    private void loadKeybinds() {
        // Implementation for loading keybindings from the configuration file.
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

    // Saves a single setting to the settings map and then writes all settings back to the configuration file.
    public void saveSetting(String key, String value) {
        settings.put(key, value);
        saveSettingsToFile();
    }

    // Writes all settings and keybindings back to the configuration file.
    public void saveSettingsToFile() {
        // Implementation includes writing settings and keybindings to the configuration file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, false))) {
            // Save Graphics
            writer.write("Fullscreen: " + settings.getOrDefault("Fullscreen", "false"));
            writer.newLine();
            writer.write("Resolution: " + settings.getOrDefault("Resolution", "800 x 600"));
            writer.newLine();
            // Save Volume
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

    // Retrieves a keybinding for a given action, providing a default if the keybinding is not found.
    public String getKeybind(String action) {
        return keybinds.getOrDefault(action, defaultKeybinds.getOrDefault(action, "None"));
    }

    // Saves a keybinding for a specific action and updates the configuration file.
    public void saveKeybind(String action, String key) {
        keybinds.put(action, key);
        saveSettingsToFile();
    }

    // Removes a keybind if it is already assigned to any action, to prevent duplicate keybindings.
    public void removeKeybindIfAssigned(String newKeybind) {
        keybinds.entrySet().removeIf(entry -> entry.getValue().equals(newKeybind));
    }

    // Checks if a given keybinding is a duplicate, excluding the current action being modified.
    public boolean isDuplicateKeybind(String input, String currentAction) {
        // Implementation uses the streaming API to check for duplicate keybindings.
        return keybinds.entrySet().stream()
                .anyMatch(entry -> entry.getValue().equals(input) && !entry.getKey().equals(currentAction));
    }

    // Removes a keybind for a specific action and updates the configuration file.
    public void removeKeybind(String action) {
        keybinds.remove(action);
        saveSettingsToFile(); // Save changes to the configuration file.
    }

    // Convenience methods for saving specific types of settings, such as resolution, fullscreen, and volume.
    // Each method updates its corresponding setting in the settings map and then saves all settings to the file.
    public void saveResolutionSetting(String resolution) {
        saveSetting("Resolution", resolution);
    }

    public void saveFullScreenSetting(boolean fullscreen) {
        saveSetting("Fullscreen", String.valueOf(fullscreen));
    }

    public void saveVolumeSetting(int volume) {
        saveSetting("Volume", String.valueOf(volume));
    }

    // Methods for loading specific settings from the configuration file. Each method retrieves its setting value,
    // providing a default if the setting is not found.
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
