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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 39324
 */
public class gestore implements EventHandler<ActionEvent>{
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private TextField cifra, cas;
    private Label saldo;
    private VBox h;
    private HBox hh;
    
    public gestore(Socket s, TextField cifra, Label saldo, VBox h, HBox hh, TextField cas) {
        this.s = s;
        this.cifra = cifra;
        this.saldo = saldo;
        this.h = h;
        this.cas = cas;
        this.hh = hh;
        try {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public void handle(ActionEvent event) {
        Button b = (Button) event.getSource();
        out.println(new JSONObject()
            .put("action", b.getText())
            .put("jsonObject", Float.parseFloat(cifra.getText()))
            .put("casuale", cas.getText()));
        try {
            System.out.println(in.readLine());
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
        } catch (IOException ex) {
            Logger.getLogger(gestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
