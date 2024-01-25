package com.base.game.retrorampage;

import java.io.*;
import java.nio.channels.FileChannel;

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
        try (FileOutputStream fos = new FileOutputStream(configFilePath);
             BufferedWriter writer = new BufferedWriter(new FileWriter(fos.getFD()))) {
            writer.write(String.valueOf(volume));
            writer.flush();

            FileChannel channel = fos.getChannel();
            channel.force(true); // Force synchronization with the disk

            System.out.println("Wrote volume to file: " + volume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
