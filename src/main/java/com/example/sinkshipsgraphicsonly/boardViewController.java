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
import javafx.scene.layout.*;

import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class boardViewController implements Initializable {

    //properties
    @FXML
    private GridPane leftGrid;

    @FXML
    private GridPane rightGrid;

    @FXML
    private Slider boardSlider;


    @FXML
    private Text outgoingText;

    @FXML
    private Text incomingText;

    @FXML
    private Button startButton;
    @FXML
    private Button avslutaMatchButton;





    private Parent root;

    private GameThread gameThread;



    public boolean server ;





//metoder

    public void hitOnCoordinate(Boolean isRightGrid, String message, int X, int Y,GameBoard gameBoard) {

        if (isRightGrid) {

            switch (message) {
                case "h":
                case "s":
                    rightGrid.add(new ImageView("Hit.jpg"), X, Y);
                    outgoingText.setText(outgoingText.getText() + "\n" + message + " " + gameBoard.getSquare(X, Y).getName()); // med scrolling
//                  outgoingText.setText(message + " " + gameBoard.getSquare(X, Y).getName()); // tar bort scrolling

                    break;

                case "m":

                        rightGrid.add(new ImageView("Miss.jpg"), X, Y);
                        outgoingText.setText(outgoingText.getText() + "\n" + message + " " + gameBoard.getSquare(X, Y).getName()); // med scrolling
//                       outgoingText.setText(message + " " + gameBoard.getSquare(X, Y).getName()); // tar bort scrolling


                    break;
                    case "GAMEOVER":
                        rightGrid.add(new ImageView("Hit.jpg"),X,Y);
                    outgoingText.setText("Game Over" + " \n" + outgoingText.getText());
            }
        } else {
            switch (message) {
                case "h":
                case "s":
                    leftGrid.add(new ImageView("Hit.jpg"), X, Y);
                    incomingText.setText(incomingText.getText() + "\n" + message + " " + gameBoard.getSquare(X, Y).getName()); // med scrolling
//                    incomingText.setText(message + " " + gameBoard.getSquare(X, Y).getName()); // tar bort scrolling

                    break;

                case "m":
                    incomingText.setText(incomingText.getText() + "\n" + message + " " + gameBoard.getSquare(X, Y).getName()); // med scrolling
//                    incomingText.setText(message + " " + gameBoard.getSquare(X, Y).getName()); // tar bort scrolling

                    leftGrid.add(new ImageView("Miss.jpg"), X, Y);
                    break;
                case "gameover":
                    leftGrid.add(new ImageView("Hit.jpg"),X,Y);
                    incomingText.setText("Game Over" + " \n" + incomingText.getText());
            }


        }
    }



        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            //initialize är en speciell metod, som anropas så fort ett fxml dokument har laddats in.


            gameThread = new GameThread(this);
                gameThread.game.setGameover(false);

            for (int row = 0; row <gameThread.game.player.SquareGrid.length ; row++) {
                for (int col = 0; col < gameThread.game.player.SquareGrid[row].length; col++) {
                    if (gameThread.game.player.SquareGrid[col][row].isHasShip()) {

                        leftGrid.add(new ImageView("Boat1.jpg"), row, col);
                        //For loop som placerar skepp på vänster rutnät
                    }
                }
            }


        }

        public void alertBoxActivated2 (ActionEvent actionEvent) throws IOException {
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

                    gameThread.game.gameover = true;
                try {
                    gameThread.game.connection.closeConnection();
                } catch (IOException ex) {

                }


                try {
                            double someBoardTickValue = boardSlider.getValue();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
                            root = loader.load();

                            battleShipGraphicsController battleGraphicController = loader.getController();
                            battleGraphicController.someSLiderValueSet(someBoardTickValue);

//lambda uttryck används med try-catch block för att spara slider värdet och gå tillbaka till huvudmenyn från att
                    //man trycker på att man vill avsluta
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            scene.getStylesheets().add("Styles.css");
                            stage.setScene(scene);

                            window.close();
                            stage.show();
                        } catch (IOException ex) {

                        }
                    }

            );


            VBox layout = new VBox(10);
            layout.getChildren().addAll(popupLabel, nejButton, jaButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            scene.getStylesheets().add("Styles.css");
            window.setScene(scene);
            window.showAndWait();
        }

        public void sliderValueSet(double theTickValue){ //Metod som används för att sätta värdet på slidern i den nuvarande scenen.
            boardSlider.setValue(theTickValue);


        }
        public void setServer(boolean a){
            this.server = a;
        }



        @FXML
        public void startButtonPressed(ActionEvent event) throws IOException, InterruptedException {
                gameThread.game.server = server;
                gameThread.start();

            startButton.setDisable(true);
            startButton.setOpacity(0.5);

        }




        public void returnToStart()throws IOException{ //Denna metod skall skicka tillbaka användaren till startmenyn
            //När ett error framkommer.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));

            Parent myRoot = loader.load();

            Stage startStage = new Stage();

            Scene scene = new Scene(myRoot, 1000, 800);
            scene.getStylesheets().add("Styles.css");

            startStage.setScene(scene);
            startStage.show();


            Stage boardStage = (Stage) leftGrid.getScene().getWindow();
            boardStage.close(); //Rad 245-246 kommer endast att fungera som tänkt, om man använder den medans boardView2.fxml visas.
            //Om startMenu.fxml visas så stängs endast boardview2.fxml's stage. Det stage som visar "startMenu.fxml" stängs ju aldrig.


        }
        public int delayValue(){
            return (int) boardSlider.getValue()*1000; //.getValue() mulitipliceras med 1000 för att tillgodose att det
            //Rör sig om millisekunder.
        }

        public void showWinner(String theName) throws IOException, InterruptedException {
            Stage window = new Stage();

            window.setTitle("VINNARE");
            window.setMinHeight(200);
            window.setMinWidth(320);
            avslutaMatchButton.setDisable(true);


            Label popupLabel = new Label();
            popupLabel.setText("Du har nu vunnit, grattis " + theName + "!");
            Button button = new Button("OK");
            button.setOnAction(e->{
                try {
                    returnToStart();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                window.close();
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(popupLabel, button);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            Stage oldStage = (Stage) leftGrid.getScene().getWindow();

            window.setX(oldStage.getX()); //dessa två rader gör att alert-meddelande stannar inom programmet
            window.setY(oldStage.getY());

            scene.getStylesheets().add("Styles.css");
            window.setScene(scene);
            window.show();

        }

    public void showLoser( String theName) throws IOException, InterruptedException {
        Stage window = new Stage();

        window.setTitle("FÖRLORARE");
        window.setMinHeight(200);
        window.setMinWidth(320);
        avslutaMatchButton.setDisable(true);


        Label popupLabel = new Label();
        popupLabel.setText("Du har nu förlorat, bättre lycka nästa gång " + theName +"!");
        Button button = new Button("OK");
        button.setOnAction(e->{
            try {
                returnToStart();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(popupLabel,button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        scene.getStylesheets().add("Styles.css");

        Stage oldStage = (Stage) leftGrid.getScene().getWindow();

       window.setX(oldStage.getX());
       window.setY(oldStage.getY());


       window.setScene(scene);
       window.show();

    }
    public void errorPopup()throws IOException,InterruptedException {
            Stage window = new Stage();
            window.setTitle("Ingen anslutning");
            window.setMinHeight(200);
            window.setMinWidth(320);

            Label popupLabel = new Label();
            Button theCloseButton = new Button("Tryck här för att stänga ned och gå tillbaka till huvudmenyn");
            theCloseButton.setOnAction(e -> {
                window.close();
            });
        popupLabel.setText("Din anslutning blev bruten");


            VBox layout = new VBox(10);
            layout.getChildren().addAll(popupLabel,theCloseButton);
            layout.setAlignment(Pos.CENTER);


            Scene scene = new Scene(layout);
            scene.getStylesheets().add("Styles.css");

            window.setScene(scene);
            window.showAndWait();

            returnToStart();



    }
    @FXML
    public void muteButtonToggled(ActionEvent actionEvent) throws IOException{
            //Denna metod kallas varje gång man trycker på knappen, så när du sätter på knappen räknas det som
        //att man har tryckt, och när man stänger av räknas det som att man har tryckt.
        if(gameThread.game.mute){
            gameThread.game.mute = false;
        }else {
            gameThread.game.mute = true;
        }
    }

}







