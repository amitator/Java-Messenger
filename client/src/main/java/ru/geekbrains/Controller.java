package ru.geekbrains;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
    private boolean authorized;
    private String nick;

    @FXML
    TextArea mainTextArea;

    @FXML
    TextField msgField;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;

    @FXML
    HBox msgPanel, authPanel;

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
        if (this.authorized){
            authPanel.setVisible(false);
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);
        } else {
            authPanel.setVisible(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);
            nick = "";
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            setAuthorized(false);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true){
                            String str = in.readUTF();
                            if (str.startsWith("/authok")){
                                setAuthorized(true);
                                nick = str.split(" ")[1];
                                break;
                            }
                            mainTextArea.appendText(str);
                            mainTextArea.appendText("\n");
                        }
                        mainTextArea.clear();
                        mainTextArea.appendText(nick);
                        while (true){
                            String str = in.readUTF();
                            mainTextArea.appendText(str);
                            mainTextArea.appendText("\n");
                            }
                        } catch (IOException e) {
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
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(){
        try {
            out.writeUTF(msgField.getText());
            System.out.println(msgField.getText());
            msgField.clear();
            msgField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void login(ActionEvent actionEvent) {
        try {
            String str = "/auth " + loginField.getText() + " " + passField.getText();
            out.writeUTF(str);
            loginField.clear();
            passField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
