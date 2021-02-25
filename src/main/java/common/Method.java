package common;

import io.netty.channel.ChannelFutureListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Method {
    public static ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.out.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
            }
        };
    }

    public static void deleteFile(String folder) {
        Path path = Paths.get(folder);
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("Failed to delete: " + e.getMessage());
        }
    }
}

