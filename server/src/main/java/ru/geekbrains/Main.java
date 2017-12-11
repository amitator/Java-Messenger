package ru.geekbrains;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server started on port 8189");
            Socket socket = serverSocket.accept(); //Waiting for client and not moving forward until client connected
            System.out.println("Client connected");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            while (true){
                String str = in.readUTF(); //Blocking the rest of the code until client write a line
                out.writeUTF("ECHO: " + str);
                out.flush();
                System.out.println("Client: "  + str);
            }
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
