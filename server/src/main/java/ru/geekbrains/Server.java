package ru.geekbrains;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private ServerSocket serverSocket;
    private Vector<ClientHandler> clients;

    public Server() {
        try {
            SQLHandler.connect();
            serverSocket = new ServerSocket(8189);
            System.out.println("Server started on port 8189");
            clients = new Vector<ClientHandler>();

            while (true) {
                Socket socket = serverSocket.accept(); //Waiting for client and not moving forward until client connected
                System.out.println("Client connected");
                clients.add(new ClientHandler(this, socket));
        }
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SQLHandler.disconnect();
        }
    }

    public void broadcastMsg(String msg){
        for (ClientHandler o: clients) {
            o.sendMsg(msg);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
