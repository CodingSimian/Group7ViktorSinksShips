package com.example.sinkshipsgraphicsonly;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class boardViewController implements Initializable { //javaklassen som kontrollerar xmlfilerna så att saker och ting händer.
    // Klass för att få ut själva båtarna, initialize ansvarar för att båtarna läggs ut.

    //properties
    @FXML
    private GridPane leftGrid;

    @FXML
    private GridPane rightGrid;

    @FXML
    private Slider boardSlider;

    private Parent root;

//metoder

    public void hitOnCoordinate(String message, int X, int Y){ //Denna kod är mera som ett grafiskt test, menad att användas när Osman
        //Och Daniel har skrivit mera backend kod som man kan länka denna mot.
        switch(message){
            case "s1":
                rightGrid.add(new ImageView("Hit.jpg"),X,Y);
                break;

            case "s2":
                rightGrid.add(new ImageView("Miss.jpg"),X,Y);

                break;
        }

    };



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialize är en speciell metod, som anropas så fort ett fxml dokument har laddats in.
        //intialize metoden anropas när boardview2.fxml laddas in, eftersom controllerklassen som den tillhör har denna metod i sig och
        //är fäst på boardview2.fxml
        //intialize är isch som main i vanliga java (eftersom detta är fx), så fort ett fxml dokumment laddas in så anropas intialize
        //Alltså efter man har klickat på spela-som-klient-knappen.

        GameBoard leftGameBoard = new GameBoard();
        leftGameBoard.buildGameBoard(); //använd buildgameboard först, för att kunna applicera de andra metoder som ligger
        //i GameBoard-klassen //Vi lägger inte till något grafisk utan använder oss av backend-kod på rad 37 och 38, sen vill
        //vi att det ska ske grafiskt det som händer grafiskt ska kunna visualiseras grafiskt. Det är därför forloopens i rad 43 osv används
        //för att placera ut skeppen på själva spelplanen.
        //Victor rekommendererar att vi lägger in kod här som startar själva spelet eftersom initialize startar av sig själv.
       //Skapa en instans av Game och skriv det här och använda oss av metoderna som vi har gjort i Game.


        leftGameBoard.buildFleet(1, 2, 3, 4);
        leftGameBoard.placeFleetAtRandom(); //Dessa två linjer har skapat alla skepp, och placerat dom. Då är själva
        //logiken för klient-spelaren utlagd. Det är först efter de 2 for loopar under denna kommentar som bilder
        //på själva rutnätet läggs ut.

        for (int row = 0; row < leftGameBoard.SquareGrid.length; row++) {
            for (int col = 0; col < leftGameBoard.SquareGrid[row].length; col++) {
                if (leftGameBoard.SquareGrid[col][row].isHasShip()) { //Måste få denna kod att fungera utan
                    //musklick, och det måste även uppdatera listan om båtar blir skjutna

                    leftGrid.add(new ImageView("Boat1.jpg"), row, col);
                }
            }
        }

        hitOnCoordinate("s2",0,3);
    }

    public void alertBoxActivated2(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Vill du verkligen avsluta?");
        window.setMinHeight(200);
        window.setMinWidth(320);


        Label popupLabel = new Label();
        popupLabel.setText("Är du säker?");
        Button nejButton = new Button("NEJ");
        nejButton.setOnAction(e -> window.close()); //Stänger ned popup-rutan

        Button jaButton = new Button("JA");
        jaButton.setOnAction(e -> {
            try {
                double someBoardTickValue = boardSlider.getValue();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
                root= loader.load();

                battleShipGraphicsController battleGraphicController = loader.getController(); //Den här koden sparar
                //OCKSÅ värdet från den föregående slidern och applicerar det på den nya scenen.
                //Copy pastea med andra variabel-namn helt enkelt
                battleGraphicController.someSLiderValueSet(someBoardTickValue);


                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene); //Denna kod laddar in startMenu.fxml filen, try & catch används för att
                //effektivt använda "throws IOException" med lambda uttrycket.
                window.close();
                stage.show();
            }
            catch(IOException ex){

            }
            }

        );


        VBox layout = new VBox(10);
        layout.getChildren().addAll(popupLabel,nejButton, jaButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout); //de tre metoder som dyker upp efter denna har jag ej fått att fungera
        //tills vidare är alertBoxActivated2 metoden som gäller när det kommer till att fråga användaren om hen
        //verkligen vill stänga ned spelet
        window.setScene(scene);
        window.showAndWait();
    }

public void sliderValueSet(double theTickValue){
        boardSlider.setValue(theTickValue);
}



}
