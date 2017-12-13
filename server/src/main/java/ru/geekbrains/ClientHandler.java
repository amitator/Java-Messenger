package ru.geekbrains;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nick;

    public ClientHandler(final Server server, final Socket socket){
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true){
                        String msg = in.readUTF();
                        if (msg.startsWith("/auth ")){
                            String[] tokens = msg.split(" ");
                            String nick = SQLHandler.getNickByLoginPass(tokens[1], tokens[2]);
                            if (nick != null){
                                out.writeUTF("/authok " + nick);
                                this.nick = nick;
                                server.subscribe(this);
                                break;
                            } else {
                                out.writeUTF("Wrong Usernane/Password");
                            }
                        }
                    }
                    while (true){
                        String msg = in.readUTF();
                        server.broadcastMsg(msg);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
