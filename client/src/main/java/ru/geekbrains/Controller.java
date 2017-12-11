package ru.geekbrains;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @FXML
    TextArea mainTextArea;

    @FXML
    TextField msgField;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        try {
            out.writeUTF(msgField.getText());
//            msgField.clear
            msgField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
