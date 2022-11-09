package com.example.sinkshipsgraphicsonly;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class boardViewController implements Initializable {
    @FXML
    private GridPane leftGrid;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameBoard leftGameBoard = new GameBoard();
        leftGameBoard.buildGameBoard(); //använd buildgameboard först, för att kunna applicera de andra metoder som ligger
        //i GameBoard-klassen

        leftGameBoard.buildFleet(1,2,3,4);
        leftGameBoard.placeFleetAtRandom();

        for (int row = 0; row < leftGameBoard.SquareGrid.length; row++) {
            for (int col = 0; col < leftGameBoard.SquareGrid[row].length; col++) {
                if (leftGameBoard.SquareGrid[col][row].isHasShip()) { //Måste få denna kod att fungera utan
                    //musklick, och det måste även uppdatera listan om båtar blir skjutna

                    leftGrid.add(new ImageView("Boat1.jpg"),row,col);
                }
            }
        }
    }

    public void pausButtonPressed(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pausMenu.fxml"));
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
