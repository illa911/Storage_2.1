package server;

import client.Client;
import common.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.nio.file.Path;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static String userFolder = Client.getUserFolder();
    private static String userFolderServer = Client.getUserFolderServer();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        if (ReceivingFile.getCurrentState() == State.IDLE) {
            byte readed = buf.readByte();
            Commands commands = Commands.valueOf(readed);
            switch (commands) {
                case DOWNLOAD:
                    downloadFileFromServer(ctx, buf,msg);
                    break;
                case UPLOAD:
                    uploadFileToServer(msg);
                    break;
            }
        }
        if (ReceivingFile.getCurrentState() == State.FILE) {
            uploadFileToServer(msg);
        }
    }
    private void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf, Object msg) throws IOException {
        String fileName = ReceivingString.receiveAndEncodeString(buf);
        System.out.println("fileName ".toUpperCase() + fileName);
        System.out.println("STATE: Start file download");
        FileSender.sendFile(Path.of(userFolderServer, fileName),
                (io.netty.channel.Channel) ctx.channel(),
                Method.getChannelFutureListener("Файл успешно передан"));
        ReceivingFile.fileReceive(msg, (byte) 3);
        buf.clear();
    }
    private void uploadFileToServer(Object msg) throws IOException {
        ReceivingFile.fileReceive(msg, (byte) 2);
    }


}
