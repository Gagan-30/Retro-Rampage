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
            // Extract the numeric part after "Volume: "
            String numberPart = line.replace("Volume: ", "").trim();
            return Integer.parseInt(numberPart);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 50; // Default volume if there's an error
        }
    }

    public void setVolume(int volume) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath))) {
            writer.write("Volume: " + volume);
            // No need to explicitly close the writer here, as it's handled by try-with-resources
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
