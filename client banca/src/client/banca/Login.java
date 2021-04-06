/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.banca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 39324
 */
public class Login implements EventHandler<ActionEvent>{
    private Socket s;
    private TextField email;
    private PasswordField pass;
    private Label error;
    private BufferedReader in;
    private PrintWriter out;
    private Stage primaryStage, second;
    private Label saldo;
    private VBox h;
    private HBox hh;

    public Login(TextField email, PasswordField  pass, Label error, Socket s,Stage primaryStage, Stage second, Label saldo, VBox h, HBox hh) throws IOException {
        this.s = s;
        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.email = email;
        this.pass = pass;
        this.error = error;
        this.primaryStage = primaryStage;
        this.second = second;
        this.saldo = saldo;
        this.h = h;
        this.hh = hh;
    }

    @Override
    public void handle(ActionEvent event) {
        if(email.getText().length() == 0){
            error.setText("Missing email");
        }else if(pass.getText().length() == 0){
            error.setText("Missing password");
        }else{
            out.println(new JSONObject()
                .put("email", email.getText())
                .put("password", pass.getText()));
            try {
                JSONObject json = new JSONObject(in.readLine());
                if(json.getBoolean("status")){
                    primaryStage.close();
                    out.println(new JSONObject().put("action", "saldo"));
                    saldo.setText("saldo: " + in.readLine());
                    out.println(new JSONObject().put("action", "storico"));
                    JSONArray sto = new JSONArray(in.readLine());
                    h.getChildren().clear();
                    h.getChildren().addAll(saldo, new Separator());
                    for(int i = 0; i < sto.length(); i++){
                        Label l = new Label();
                        l.setText(sto.getJSONObject(i).getString("casuale") + " | " 
                                + sto.getJSONObject(i).getString("status").replace("positivo", "+").replace("negativo", "-") 
                                + sto.getJSONObject(i).getFloat("transazione"));
                        h.getChildren().addAll(l, new Separator());
                    }
                     h.getChildren().add(hh);
                    second.show();
                }else{
                    error.setText(json.getString("err"));
                }
            } catch (IOException ex) {
                error.setText(ex.toString());
            }
        }
       
    }
    
    
}
