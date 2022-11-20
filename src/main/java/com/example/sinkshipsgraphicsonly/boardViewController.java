package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

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


    @FXML
    private Text outgoingText;

    @FXML
    private Text incomingText;



    private Parent root;

    private GameThread gameThread;



    public boolean server ;





//metoder

    public void hitOnCoordinate(Boolean isRightGrid, String message, int X, int Y,GameBoard gameBoard) { //Denna kod är mera som ett grafiskt test, menad att användas när Osman
        //Och Daniel har skrivit mera backend kod som man kan länka denna mot.

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
                case "GAMEOVER":
                    incomingText.setText("Game Over" + " \n" + incomingText.getText());
            }


        }
    };



        @Override
        public void initialize (URL url, ResourceBundle resourceBundle){
            //initialize är en speciell metod, som anropas så fort ett fxml dokument har laddats in.
            //intialize metoden anropas när boardview2.fxml laddas in, eftersom controllerklassen som den tillhör har denna metod i sig och
            //är fäst på boardview2.fxml
            //intialize är isch som main i vanliga java (eftersom detta är fx), så fort ett fxml dokumment laddas in så anropas intialize
            //Alltså efter man har klickat på spela-som-klient-knappen.


            //GameBoard leftGameBoard = new GameBoard();
            //leftGameBoard.buildGameBoard(); //använd buildgameboard först, för att kunna applicera de andra metoder som ligger
            //i GameBoard-klassen //Vi lägger inte till något grafisk utan använder oss av backend-kod på rad 37 och 38, sen vill
            //vi att det ska ske grafiskt det som händer grafiskt ska kunna visualiseras grafiskt. Det är därför forloopens i rad 43 osv används
            //för att placera ut skeppen på själva spelplanen.
            //Victor rekommendererar att vi lägger in kod här som startar själva spelet eftersom initialize startar av sig själv.
            //Skapa en instans av Game och skriv det här och använda oss av metoderna som vi har gjort i Game.


            //leftGameBoard.buildFleet(1, 2, 3, 4);
            //leftGameBoard.placeFleetAtRandom(); //Dessa två linjer har skapat alla skepp, och placerat dom. Då är själva
            //logiken för klient-spelaren utlagd. Det är först efter de 2 for loopar under denna kommentar som bilder
            //på själva rutnätet läggs ut.





            gameThread = new GameThread(this);
                gameThread.game.setGameover(false);
            //leftGameBoard.SquareGrid[row].length
            for (int row = 0; row <gameThread.game.player.SquareGrid.length ; row++) {
                for (int col = 0; col < gameThread.game.player.SquareGrid[row].length; col++) {
                    if (gameThread.game.player.SquareGrid[col][row].isHasShip()) {

                        leftGrid.add(new ImageView("Boat1.jpg"), row, col);
                    }
                }
            }

            /*try {
                returnToStart();
            } catch (IOException e) {
                throw new RuntimeException(e); //Hihi Viktor testar
            }*/


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
                if(gameThread.isAlive()) {
                    gameThread.game.gameover = true;
                }
                        try {
                            double someBoardTickValue = boardSlider.getValue();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));
                            root = loader.load();

                            battleShipGraphicsController battleGraphicController = loader.getController(); //Den här koden sparar
                            //OCKSÅ värdet från den föregående slidern och applicerar det på den nya scenen.
                            //Copy pastea med andra variabel-namn helt enkelt
                            battleGraphicController.someSLiderValueSet(someBoardTickValue);


                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Scene scene = new Scene(root);
                            scene.getStylesheets().add("Styles.css");
                            stage.setScene(scene); //Denna kod laddar in startMenu.fxml filen, try & catch används för att
                            //effektivt använda "throws IOException" med lambda uttrycket.
                            window.close();
                            stage.show();
                        } catch (IOException ex) {

                        }
                    }

            );


            VBox layout = new VBox(10);
            layout.getChildren().addAll(popupLabel, nejButton, jaButton);
            layout.setAlignment(Pos.CENTER);

            //layout.getStyleClass.add("Styles.css"); <-- denna funkar inte som tänkt

            Scene scene = new Scene(layout); //de tre metoder som dyker upp efter denna har jag ej fått att fungera
            //tills vidare är alertBoxActivated2 metoden som gäller när det kommer till att fråga användaren om hen
            //verkligen vill stänga ned spelet
            scene.getStylesheets().add("Styles.css");
            window.setScene(scene);
            window.showAndWait();
        }

        public void sliderValueSet(double theTickValue){ //Metod som används för att sätta värdet på programmet.
            boardSlider.setValue(theTickValue);


        }
        public void setServer(boolean a){
            this.server = a;
        }

        private boolean getServer(){
            return this.server;
        }

        @FXML
        public void startButtonPressed(ActionEvent event) throws IOException, InterruptedException {
            gameThread.game.server = server;
            gameThread.start();

        }

        public void oponnentDisconect(){

        }

        //@FXML
        public void returnToStart()throws IOException{ //Denna metod skall skicka tillbaka användaren till startmenyn
            //När ett error framkommer.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startMenu.fxml"));

            Parent myRoot = loader.load();

            Stage coolerStage = new Stage(); //Denna rad måste ändras till det nuvarande staget.

            Scene scene = new Scene(myRoot, 1000, 800);
            scene.getStylesheets().add("Styles.css");

            coolerStage.setScene(scene);
            coolerStage.show();

            FXMLLoader someSortaLoader = new FXMLLoader(getClass().getResource("boardView2.fxml"));
            //boardViewController someSortaController = someSortaLoader.getController();
            Stage stage = (Stage) leftGrid.getScene().getWindow();
            stage.close(); //Rad 234-237 kommer endast att fungera som tänkt, om man använder den medans boardView2.fxml visas.
            //Om startMenu.fxml visas så stängs ju endast boardview2.fxml's stage, det stage som visar "startMenu.fxml" stängs ju aldrig.


        }
        public int delayValue(){
            return (int) boardSlider.getValue()*1000; //.getValue() mulitipliceras med 1000 för att tillgodose att det
            //Rör sig om millisekunder.
        }

        public void showWinner(String theName){
            Stage window = new Stage();

            //window.initModality(Modality.APPLICATION_MODAL); Denna rad gör det omöjligt att klicka utanför fönstret

            window.setTitle("VINNARE");
            window.setMinHeight(200);
            window.setMinWidth(320);



            Label popupLabel = new Label();
            popupLabel.setText("Du har nu vunnit, grattis " + theName + "!");

            VBox layout = new VBox(10);
            layout.getChildren().addAll(popupLabel);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            Stage oldStage = (Stage) leftGrid.getScene().getWindow();

            window.setX(oldStage.getX()); //Rad 295-298 gör så att popup fönstret hamnar innanför programmets fönster.
            window.setY(oldStage.getY());

            scene.getStylesheets().add("Styles.css");
            window.setScene(scene);
            //window.showAndWait(); denna rad funkar med application_modal
            window.show();
        }

    public void showLoser( String theName){
        Stage window = new Stage();

        //window.initModality(Modality.APPLICATION_MODAL); Denna rad gör det omöjligt att klicka utanför fönstret

        window.setTitle("FÖRLORARE");
        window.setMinHeight(200);
        window.setMinWidth(320);



        Label popupLabel = new Label();
        popupLabel.setText("Du har nu förlorat, bättre lycka nästa gång " + theName +"!");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(popupLabel);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        scene.getStylesheets().add("Styles.css");

        FXMLLoader someSortaLoader = new FXMLLoader(getClass().getResource("boardView2.fxml"));
        boardViewController someSortaController = someSortaLoader.getController();
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

        popupLabel.setText("Anslutningen bruten återvänder till start skärmen inom 5 sekunder.");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(popupLabel);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("Styles.css");
        FXMLLoader someSortaLoader = new FXMLLoader(getClass().getResource("boardView2.fxml"));
        boardViewController someSortaController = someSortaLoader.getController();
        Stage oldStage = (Stage) leftGrid.getScene().getWindow();
        // window.setX(oldStage.getX());
        // window.setY(oldStage.getY());
        window.setScene(scene);
        window.show();
        Thread.sleep(5000); //KLicka på avsluta-match för att generera error
        window.close();
        returnToStart();
    }
    @FXML
    public void muteButtonToggled(ActionEvent actionEvent) throws IOException{
            //Denna metod kallas varje gång man trycker på knappen, så när du sätter på knappen räknas det som
        //att man har tryckt, och när man stänger av räknas det som att man har tryckt. Ha en if-sats som kollar
        //Om en media player är mutad, om den är det händer inget, om den inte är så mutas den.
        Thread.currentThread().getStackTrace()[1].getClassName();
        if(gameThread.game.mute){
            gameThread.game.mute = false;
        }else {
            gameThread.game.mute = true;
        }
    }

}







