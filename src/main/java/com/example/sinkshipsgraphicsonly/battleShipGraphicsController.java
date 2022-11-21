package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class battleShipGraphicsController {

    @FXML
    private Slider startMenuSlider;

    private Parent root;
    protected boolean server;

    @FXML
    public void clientButtonPressed(ActionEvent actionEvent) throws IOException, InterruptedException {


        double tickValue = startMenuSlider.getValue();//hämtar det senaste värdet på slidern.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("boardView2.fxml")); //rad 42-47 möjliggör
        //att värderna mellan slidesen mellan scenerna startMenu och boardview2.fxml sparas när man startar ett game.
        root = loader.load();


        boardViewController boardController = loader.getController();//skapar en instans för controllen som gäller för spelplanen.
        boardController.setServer(false);
        boardController.sliderValueSet(tickValue);



        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Styles.css");
        stage.setScene(scene);
        stage.show();
    }


    public void serverButtonPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        double tickValue = startMenuSlider.getValue();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("boardView2.fxml"));
        root = loader.load();


        boardViewController boardController = loader.getController();
        boardController.setServer(true);
        boardController.sliderValueSet(tickValue);


        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Styles.css");

        stage.setScene(scene);
        stage.show();

    }

    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }

    public void someSLiderValueSet(double myTickValue) {
        startMenuSlider.setValue(myTickValue);
    }


    public boolean getServer() {
        return this.server;
    }


}