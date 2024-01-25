package com.base.game.retrorampage;

import java.io.*;

public class Config {
    private String configFilePath;

    public Config(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public int getVolume() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line = reader.readLine();
            System.out.println("Read volume from file: " + line); // Logging read operation
            return Integer.parseInt(line);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 50; // Default volume if there's an error
        }
    }

    public void setVolume(int volume) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(configFilePath);
            writer.write(String.valueOf(volume));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
