package server;

import java.nio.channels.Channel;

public class User {
    private Channel channel;
    private String userName;
    private String directory;

    public User(Channel channel, String userName, String directory) {
        this.channel = channel;
        this.userName = userName;
        this.directory = directory;
    }

}

