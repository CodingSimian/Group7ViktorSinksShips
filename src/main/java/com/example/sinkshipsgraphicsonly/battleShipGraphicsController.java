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

public class battleShipGraphicsController  { //Klass för att få fram spelplanen
    @FXML
    private GridPane leftGrid;
    Connection someConnection=new Connection();




    @FXML
    public void clientButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("boardView2.fxml"));
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void serverButtonPressed(ActionEvent actionEvent) throws IOException {
        //someConnection.newServer();
        System.out.println("Server knappen har blivit tryckt");
        //Denna metod ger funktionalitet till spela-som-server-knappen. Denna mapp innehåller endast den grafiska implementationen
        //För spelet, pågrund av detta kommenteras ur den metod som egentligen ska ligga här i slutversionen.
    }

    public void exitButtonPressed(ActionEvent actionEvent) throws IOException{
    Platform.exit();
    }


    public void continueButtonPressed(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("boardView2.fxml"));
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setScene(scene); //Eftersom jag utgår från att båtars position. skott etc kommer externt från backend
        //kod, så behövs inte någon speciiell kod för att gå tillbaka till spelplanen. Man helt enkelt bara laddar in
        //all backend kod som redan håller koll på mängden båtar/skott. Precis som i början
        stage.show();
    }


    @FXML
    public void thisCoordinate(javafx.scene.input.MouseEvent mouseEvent) {
        Node mousenode = (Node) mouseEvent.getSource();

        StackPane mySPane = new StackPane();
        mySPane.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        System.out.println("X koordinaten blir: " + leftGrid.getColumnIndex(mousenode) + " medan Y blir: " + mouseEvent.getY());
        leftGrid.add(mySPane, 0, 0);
    }


}
