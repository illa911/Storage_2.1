package client;

import common.Commands;
import common.FileSender;
import server.auth.AuthService;
import server.auth.Users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Client {
    private static String userName;
    private static String userFolderServer;
    private static String userFolder = getUserFolder();


    public static void main(String[] args) throws IOException {
        mainHandler();
    }
    private static void mainHandler() throws IOException {

        System.out.println("Авторизуйтесь! Введите логин и пароль через пробел.");
        doAuth();
        System.out.println(" Здравствуйте " + userName + "\n наберите help для получения справки");
        Network net = new Network();
        ServerNetwork servNet = new ServerNetwork();
        String inputLine;
        while (true) {
            InputStream in = System.in;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            if (net.isConnect()) {
                while (true) {
                    if (!FileSender.isLoadingStatus()) {
                        break;
                    }
                }
                inputLine = bufferedReader.readLine().trim().toLowerCase();
                String firstCommand = inputLine.split(" ")[0];
                Commands commands;
                try {
                    commands = Commands.valueOf(firstCommand.toUpperCase());
                    switch (commands) {
                        case UPLOAD:
                            servNet.sendFile(inputLine, net);
                            break;
                        case DOWNLOAD:
                            servNet.downloadFile(inputLine, net);
                            break;
                        case DELETE:
                            servNet.deleteFile(inputLine);
                            break;
                        case RENAME:
                            servNet.renameFile(inputLine);
                            break;
                        case LS:
                            walk();
                            break;
                        case HELP:
                            System.out.println(commands.helpInfo());
                            break;
                        case EXIT:
                            System.out.println("Досвидание!");
                            net.close();
                            System.exit(0);
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Нет такой команды");
                    mainHandler();
                }
            }
        }
    }
    public static void walk() {
        Stream<Path> stream;
        try {
            stream = Files.walk(Path.of(userFolderServer));
            System.out.println("Содержимое вашего хранилища:");
            stream.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }

    }
    private static void doAuth() {
        AuthService authService = new AuthService();
        AtomicBoolean click = new AtomicBoolean(false);
        while (click.get() == false) {
            Scanner scanner = new Scanner(System.in);
            String authStr = scanner.nextLine();
            String[] authData = authStr.split("\\s");
            if (authData.length > 1) {
                authService.doAuth(authData[0], authData[1]).ifPresentOrElse(
                        user -> {
                            userName = user.getNickname();
                            userFolderServer = user.getServerStorage();
                            System.out.println(userName + " вы вошли");
                            click.set(true);
                        },
                        new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Нет таких логина и пароля, попробуйте еще раз");
                            }
                        }
                );
            } else{
                System.out.println("Логин и пароль введен некоректно");
            }
        }
    }

    public static String getUserFolderServer() {
        return userFolderServer;
    }

    public static String getUserFolder() {
        return userFolder;
    }
}
