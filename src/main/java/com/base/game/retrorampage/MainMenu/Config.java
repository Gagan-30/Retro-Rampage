package com.base.game.retrorampage.MainMenu;

import java.io.*;
import java.util.*;

public class Config {
    private final String configFilePath;
    private final Map<String, String> settings = new HashMap<>();
    private final Map<String, String> keybinds = new HashMap<>();
    private static final Map<String, String> defaultKeybinds = new HashMap<>();

    static {
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
        defaultKeybinds.put("Inventory1", "TAB");
    }

    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
        initializeDefaultKeybinds();
        loadSettings();
        loadKeybinds();
    }

    private void initializeDefaultKeybinds() {
        keybinds.putAll(defaultKeybinds);
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
                    String key = parts[0].substring(8).trim();
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

    public void saveSettingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath, false))) {
            writer.write("Fullscreen: " + settings.getOrDefault("Fullscreen", "false"));
            writer.newLine();
            writer.write("Resolution: " + settings.getOrDefault("Resolution", "800 x 600"));
            writer.newLine();
            writer.write("Volume: " + settings.getOrDefault("Volume", "53"));
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
            writer.write("Inventory1: " + keybinds.getOrDefault("Inventory1", ""));
            writer.newLine();
            writer.write("Inventory2: " + keybinds.getOrDefault("Inventory2", ""));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getKeybind(String action) {

        return keybinds.getOrDefault(action, defaultKeybinds.getOrDefault(action, "None"));
    }


    public void saveKeybind(String action, String key) {
        keybinds.put(action, key);
        saveSettingsToFile();
    }


    public void removeKeybindIfAssigned(String newKeybind) {
        keybinds.entrySet().removeIf(entry -> entry.getValue().equals(newKeybind));
    }

    public boolean isDuplicateKeybind(String input, String currentAction) {
        return keybinds.entrySet().stream()
                .anyMatch(entry -> entry.getValue().equals(input) && !entry.getKey().equals(currentAction));
    }

    public void removeKeybind(String action) {
        keybinds.remove(action);
        saveSettingsToFile(); // Save changes to the file
    }

    public Map<String, String> getAllKeybinds() {
        return new HashMap<>(keybinds);
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
