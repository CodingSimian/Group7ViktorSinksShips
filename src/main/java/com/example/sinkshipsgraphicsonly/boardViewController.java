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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.example.sinkshipsgraphicsonly.HelloApplication.*;

public class boardViewController implements Initializable { //javaklassen som kontrollerar xmlfilerna så att saker och ting händer.
    // Klass för att få ut själva båtarna, initialize ansvarar för att båtarna läggs ut.
    @FXML
    private GridPane leftGrid;

    @FXML
    private Button nejButton;


    @FXML
    private BorderPane alertBox;

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

    }

    public void alertBoxActivated2(ActionEvent actionEvent) throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Vill du verkligen avsluta?");
        window.setMinHeight(300);
        window.setMinWidth(200);


        Label popupLabel = new Label();
        popupLabel.setText("Är du säker?");
        Button nejButton = new Button("NEJ");
        nejButton.setOnAction(e -> window.close()); //Stänger ned popup-rutan

        Button jaButton = new Button("JA");
        jaButton.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("startMenu.fxml"));
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


    public void alertBoxActivated(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();

        //Stage window = new Stage();
        stage.setTitle("Vill du avsluta?");
        stage.initModality(Modality.APPLICATION_MODAL); //Gör så att man nt kan klicka utanför rutan


        Scene scene = new Scene(alertBox);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void nejButtonpressed(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) nejButton.getScene().getWindow();
        stage.close(); //Gör så att popup stängs
    }

    public void avslutaMatchButtonPressed(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("startMenu.fxml"));
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setScene(scene); //Denna kod är originellt från bro-codes tutorial på youtube
        stage.show();
    }
}
