package client;

import common.Method;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerNetwork {
    private static Network client;
    private static String userFolder = "src/main/java/client/test//";
    private static String userFolderServer ="src/main/java/server/fileUser1";

    static void downloadFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String command = getSecondElement(inputLine);
            sendStringAndCommandByte(command, (byte) 3);
        } else {
            System.out.println();
        }
    }
    static void sendFile(String inputLine) {
        if (isThereaSecondElement(inputLine)) {
            String fileName = getSecondElement(inputLine);
            if (isFileExists(fileName)) {
                client.sendFile(userFolder);

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
            Path path = Paths.get(userFolderServer, fileName);
            if (Files.exists(path)) {
                Method.deleteFile(path.toString());
                if (Files.exists(path)) {
                    return "Не удалось удалить указанный файл";
                } else {
                    return "Файл успешно удален";
                }
            } else {
                return "Неправильное имя файла";
            }
        }
        return result;
    }

    public void renameFile (String nameFile, String renameFile){
        System.out.println(nameFile+" "+renameFile); // для тестирования
        Path folder = Paths.get(userFolderServer);
        File file = folder.resolve(userFolderServer+nameFile).toFile();
        File newFile = folder.resolve(userFolderServer+renameFile).toFile();
        if(file.renameTo(newFile)){
            System.out.println("Файл переименован успешно");;
        }else{
            System.out.println("Файл не был переименован");
        }

    }



    public static boolean isThereaSecondElement(String inputLine) {
        return inputLine.split(" ").length == 2;
    }
    public static String getSecondElement(String inputLine) {
        return inputLine.split(" ")[0];
    }
    public static void sendStringAndCommandByte(String folderName, byte commandByte) {
        client.sendStringAndCommand(folderName, commandByte);
    }
    private static boolean isFileExists(String fileName) {
        return Files.exists(Path.of(fileName));
    }

}
