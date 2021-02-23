package common;

import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;



public class ReceivingFile {
    private static State currentState = State.IDLE;
    private static String userFolder = "src/main/java/client/test/";
    private static String userFolderServer = "src/main/java/server/fileUser1/";
    private static final int FOUR = 4;
    private static final int EIGHTS = 8;
    private static int nextLength;
    private static long fileLength;
    private static long receivedFileLength;
    private static BufferedOutputStream out;

    public static State getCurrentState() {
        return currentState;
    }
    public static void fileReceive(Object msg, Byte bt
                                   ) throws IOException {
        String fileNameStr;
        String fullPathString = null;
        if(bt == (byte) 2){
            fullPathString = userFolderServer;
        } else if (bt == (byte) 3){
            fullPathString = userFolder;
        }

        ByteBuf buf = ((ByteBuf) msg);

        if (currentState == State.IDLE) {
            receivedFileLength = 0;
            buf.resetReaderIndex();
            byte readed = buf.readByte();
            if (readed == (byte) 2) {
                currentState = State.NAME_LENGTH;
                System.out.println("currentState changed: " + currentState);
                System.out.println("Client: Start file receiving");
            } else {
                System.out.println("(class ReceivingFiles)ERROR: Invalid first byte - " + readed);
                return;
            }
        }

        if (currentState == State.NAME_LENGTH) {
            if (buf.readableBytes() < FOUR) {
                return;
            }
            if (buf.readableBytes() >= FOUR) {
                System.out.println("STATE: Get filename length");
                nextLength = buf.readInt();
                currentState = State.NAME;
                System.out.println("currentState changed: " + currentState);
            }
        }

        if (currentState == State.NAME) {
            if (buf.readableBytes() < nextLength) {
                return;
            }
            if (buf.readableBytes() >= nextLength) {
                byte[] fileName = new byte[nextLength];
                buf.readBytes(fileName);
                fileNameStr = new String(fileName);
                System.out.println("STATE: Filename received - _" + new String(fileName));
                out = new BufferedOutputStream(new FileOutputStream(fullPathString + fileNameStr));
                currentState = State.FILE_LENGTH;
                System.out.println("currentState changed: " + currentState);
            }
        }

        if (currentState == State.FILE_LENGTH) {
            if (buf.readableBytes() < EIGHTS) {
                return;
            }
            if (buf.readableBytes() >= EIGHTS) {
                fileLength = buf.readLong();
                System.out.println("STATE: File length received - " + fileLength);
                currentState = State.FILE;
                System.out.println("currentState changed: " + currentState);
            }
        }
        if (currentState == State.FILE) {
            while (buf.readableBytes() > 0) {
                out.write(buf.readByte());
                receivedFileLength++;
                if (fileLength == receivedFileLength) {
                    currentState = State.IDLE;
                    System.out.println("currentState changed: " + currentState);
                    System.out.println("File received");
                    if (buf.readableBytes() == 0) {
                        buf.clear();
                    }
                    out.close();
                }
            }
        }
    }
}

