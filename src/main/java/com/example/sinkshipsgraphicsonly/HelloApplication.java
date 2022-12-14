package com.example.sinkshipsgraphicsonly;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        scene.getStylesheets().add("Styles.css");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() { //Gör så att det inte går att kryssa ner programmet
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });

        stage.setTitle("BattleShips");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();



    }

}