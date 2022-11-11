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

public class battleShipGraphicsController  { //Klass för att få fram spelplanen
    @FXML
    private GridPane leftGrid;

    Connection theGameConnection = new Connection(); //statisk property

    @FXML
    private Slider startMenuSlider;

    private Parent root;


    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    theGameConnection = new Connection();

    }*/

    @FXML
    public void clientButtonPressed(ActionEvent actionEvent) throws IOException {
        theGameConnection.connectToServer();

        double tickValue = startMenuSlider.getValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("boardView2.fxml")); //rad 42-47 möjliggör
        //så att värderna mellan slidesen mellan scenerna startMenu och boardview2.fxml sparas när man startar ett game.

        //Jag tänker att detta endast gäller om man är klient, så att även om man ändrar på slidern, men trycker att man
        //spelar som server så ligger slidern enligt klienten.
        root = loader.load();

        boardViewController boardController = loader.getController();
        boardController.sliderValueSet(tickValue);

        //Denna kontroller bör ha connection
        //klassen, så att man skapar upp en connection mellan två datorer, och först efter den anslutningen har lyckats
        //så laddar clientButtonPressed in boardview2.fxml
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void serverButtonPressed(ActionEvent actionEvent) throws IOException {
        theGameConnection.newServer();


        Parent root = FXMLLoader.load(getClass().getResource("boardView2.fxml"));
        Stage stage =(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exitButtonPressed(ActionEvent actionEvent) throws IOException{
    Platform.exit();
    }

public void someSLiderValueSet(double myTickValue){
        startMenuSlider.setValue(myTickValue);
}

public Connection getConnection(){
        return this.theGameConnection;
}
}
