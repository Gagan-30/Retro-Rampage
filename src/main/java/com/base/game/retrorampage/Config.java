package com.base.game.retrorampage;

import java.io.*;

public class Config {
    private String configFilePath;

    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    // Save resolution setting
    public void saveResolutionSetting(String resolution) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("Resolution: " + resolution);
            writer.newLine();
            // No need to explicitly close the writer here, as it's handled by try-with-resources
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save fullscreen setting
    public void saveFullScreenSetting(boolean fullscreen) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("Fullscreen: " + fullscreen);
            writer.newLine();
            // No need to explicitly close the writer here, as it's handled by try-with-resources
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Save volume setting
    public void saveVolumeSetting(int volume) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("Volume: " + volume);
            writer.newLine();
            // No need to explicitly close the writer here, as it's handled by try-with-resources
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Load resolution setting
    public String loadResolutionSetting() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Resolution:")) {
                    return line.split(":")[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "800 x 600"; // Default resolution
    }

    // Load fullscreen setting
    public boolean loadFullscreenSetting() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Fullscreen:")) {
                    return Boolean.parseBoolean(line.split(":")[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Default fullscreen setting
    }

    // Load volume setting
    public int loadVolumeSetting() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Volume:")) {
                    return Integer.parseInt(line.split(":")[1].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 50; // Default volume
    }
}
