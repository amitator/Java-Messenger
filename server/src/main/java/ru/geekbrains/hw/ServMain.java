package ru.geekbrains.hw;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServMain {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server started");
            Socket socket = serverSocket.accept();
            System.out.println("Client Connected");
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String msg = in.nextLine();
                        System.out.println("Client: " + msg);
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    while (true){
                        String msg = scanner.nextLine();
                        out.write("Server: " + msg + "\n");
                        out.flush();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

