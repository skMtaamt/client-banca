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
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;


/**
 *
 * @author 39324
 */
public class ClientBanca extends Application {
    private Object scene;
    
    @Override
    public void start(Stage primaryStage){
        Socket s = null;
        Label error = new Label();
        error.setTextFill(Color.RED);
        try {
            s = new Socket("127.0.0.1", 8080);
        } catch (IOException ex) {
            error.setText(ex.toString());
        }
        Stage second = new Stage();
        VBox root2 = new VBox();
        root2.setAlignment(Pos.CENTER);
        Label saldo = new Label();
        saldo.setText("saldo:");
        HBox h = new HBox();
        TextField cifra = new TextField();
        cifra.setPrefWidth(50);
        cifra.setMaxWidth(50);
        TextField cas = new TextField();
        cas.setPrefWidth(50);
        cas.setMaxWidth(50);
        h.setAlignment(Pos.CENTER);
        root2.getChildren().addAll(saldo, new Separator(), h);
        Scene scena2 = new Scene(root2, 500, 500);
        second.setTitle("Banca");
        second.setScene(scena2);
        gestore g = new gestore(s, cifra, saldo, root2, h, cas);
        Button versa = new Button();
        versa.setText("versa");
        versa.setOnAction(g);
        Button preleva = new Button();
        preleva.setText("preleva");
        preleva.setOnAction(g);
        h.getChildren().addAll(versa, cifra, cas, preleva);
        VBox root = new VBox();
        Label email = new Label();
        email.setText("email:");
        email.setPadding(new Insets(10, 10, 10, 10));
        Label pass = new Label();
        pass.setText("password");
        pass.setPadding(new Insets(10, 10, 10, 10));
        Button login = new Button();
        TextField emailinput = new TextField();
        emailinput.setPrefWidth(100);
        emailinput.setMaxWidth(150);
        PasswordField  passinput = new PasswordField();
        passinput.setPrefWidth(100);
        passinput.setMaxWidth(150);
        login.setText("Login");
        Login l = null;
        StackPane p = new StackPane();
        p.getChildren().add(login);
        p.setAlignment(Pos.CENTER);
        p.setPadding(new Insets(10, 10, 10, 10));
        
        StackPane perror = new StackPane();
        perror.getChildren().add(error);
        perror.setAlignment(Pos.CENTER);
        p.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(email, emailinput, pass, passinput, p, perror);
        root.setAlignment(Pos.CENTER);
        Scene scena = new Scene(root, 300, 300);
        try {
            l = new Login(emailinput, passinput, error, s, primaryStage, second, saldo, root2, h);
        } catch (IOException ex) {
            error.setText(ex.toString());
        }
        login.setOnAction(l);
        primaryStage.setTitle("Banca");
        primaryStage.setScene(scena);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
