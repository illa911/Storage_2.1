package client;

import common.Method;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerNetwork {
    public static Network client;
    private static String userFolder = "src/main/java/client/test/";
    private static String userFolderServer ="src/main/java/server/fileUser1/";

    public static void downloadFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            sendStringAndCommandByte(command, (byte) 3);
        } else {
            System.out.println("error");
        }
    }
    public static void sendFile(String inputLine, Network client) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);
            if (Files.exists(Path.of(userFolder+fileName))) {
                client.sendFile(userFolder+fileName);
            } else {
                System.out.println("Файл не найден");
            }
        }
    }

    public static String deleteFile (String inputLine) {
        String result = "";
        String fileName;
        if (isThereaSecondElement(inputLine)) {
            fileName = getSecondElement(inputLine);
            System.out.println(fileName);
            Path path = Paths.get(userFolderServer, fileName);
            if (Files.exists(path)) {
                Method.deleteFile(path.toString());
                System.out.println("Файл удалён");
            }else {
                System.out.println("Файл не найден");
            }
        }
        return result;
    }

    public void renameFile (String nameFile){
            String[] mas = nameFile.split("\\s");
            if(mas.length == 1){
                System.out.println("Команда введена неправильно, воспользуйтесь help");
            }else{
            String nameFile1 = mas[1];
            String renameFile = mas[2];
            System.out.println(nameFile1 + " " + renameFile); // для тестирования
            Path folder = Paths.get(userFolderServer);
            File file = folder.resolve(nameFile1).toFile();
            File newFile = folder.resolve(renameFile).toFile();
            if (file.renameTo(newFile)) {
                System.out.println("Файл переименован успешно");
                ;
            } else {
                System.out.println("Файл не был переименован");
            }
        }

    }

    public static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }
    public static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[1];
    }
    public static void sendStringAndCommandByte(String folderName, byte commandByte) {
        System.out.println(folderName);
        System.out.println(commandByte);
            client.sendStringAndCommand(folderName, commandByte);
    }
}
