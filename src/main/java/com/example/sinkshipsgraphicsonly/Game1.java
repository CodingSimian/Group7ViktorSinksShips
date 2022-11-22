package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Game1 {

    protected GameBoard player;
    protected GameBoard enemy;

    private int delay;

    private Scanner scan;
    private boardViewController controller;

    protected boolean mute;

    Connection connection;
    int round;
    boolean gameover;
    String outGoingMessage;
    String incomingMesssage;
    boolean server;
    private String name;
    private final String miss = "src/main/resources/Miss.mp3";
    private final Media missSound = new Media(new File(miss).toURI().toString());



    private final String hit = "src/main/resources/Hit.mp3";
    private final Media hitSound = new Media(new File(hit).toURI().toString());


    private final String loser = "src/main/resources/Loser.mp3";
    private final Media loserSound = new Media(new File(loser).toURI().toString());


    private final String winner = "src/main/resources/Winner.mp3";
    private final Media winnerSound = new Media(new File(winner).toURI().toString());

    Game1(boardViewController controller )  {
        player = new GameBoard();
        player.buildGameBoard();
        player.buildFleet(1, 2, 3, 4);
        player.placeFleetAtRandom();
        enemy = new GameBoard();
        enemy.buildGameBoard();
        gameover = false;
        round = 0;

        this.controller = controller;
        connection = new Connection();


    }


    public void play() throws IOException, InterruptedException {



        if(server){
            name = "Server";
            connection.newServer();

        }else {
            name = "Client";
            connection.connectToServer();
        }

        if (connection.isConnected()) { //Skrev om
            outGoingMessage = "i shot ";
            outGoingMessage += enemy.getRandomCoordinate();
            connection.sendMessage(outGoingMessage);
        }
        while (!gameover) {

            incomingMesssage = connection.reciveMessage();
            if(incomingMesssage.equalsIgnoreCase("GAMEOVER")){ //om incommingMessage är "GAMEOVER",
                //Så är den instansen av programmet en vinnare. Och om outGoingMessage är "GAMEOVER" så är den
                //instansen förlorare.
                if(!mute){
                    MediaPlayer mediaPlayer = new MediaPlayer(winnerSound);
                    mediaPlayer.play();
                }
                uppDateRightBoard("GAMEOVER");

                Platform.runLater( ()-> {
                    try {
                        controller.showWinner(name);
                    } catch (IOException  | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                gameover = true;

            }
            if(!gameover) {
                outGoingMessage = breakDownMessage(incomingMesssage);
                delay = controller.delayValue();
                //delayValue är en metod som kallar på Slider.getValue() för den slidern som är i boardView2.fxml
                //Inlägd här för att tillgodose att hela spelet sker i en while-loop.
                Thread.sleep(delay);

                round++;
                connection.sendMessage(outGoingMessage);

                if(outGoingMessage.equalsIgnoreCase("GAMEOVER")){
                    if (!mute) {
                        MediaPlayer mediaPlayer = new MediaPlayer(loserSound);
                        mediaPlayer.play();
                    }
                    Platform.runLater( ()-> {
                        try {
                            controller.showLoser(name);
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                    gameover = true;

                }
            }


        }

        connection.closeConnection();
    }
    // Plockar isär informationen från motståndaren och tar fram ett nytt meddelande beroende på svar från GameBoard.
    // Samt kallar på metoder för att uppdatera grafiska spelplanerna;
    public String breakDownMessage(String message){
        scan = new Scanner(message);
        String newMessage;
        String feedbackLeftBoard;
        String feedback = scan.next();
        scan.next();
        newMessage = scan.next();
        feedbackLeftBoard = player.fire(newMessage);
        uppDateRightBoard(feedback);
          if(feedbackLeftBoard.equalsIgnoreCase("gameover")) {
              uppdateLeftBoard("gameover",newMessage);
              newMessage = feedbackLeftBoard;
          }else {

              if (!mute) {
                  switch (feedbackLeftBoard) {
                      case "h", "s" -> {
                          MediaPlayer mediaPlayer = new MediaPlayer(hitSound);
                          mediaPlayer.play();
                      }
                      case "m" -> {
                          MediaPlayer mediaPlayer1 = new MediaPlayer(missSound);
                          mediaPlayer1.play();
                      }
                  }
              }
              uppdateLeftBoard(feedbackLeftBoard, newMessage);
              newMessage = feedbackLeftBoard;
              newMessage += " shot ";
              newMessage += nextCoordinate(feedback);
          }

        return newMessage;

             /* Uppdaterar grafiken på den högra spelplanen , den hämtar informationen från GameBoard enemy
                Angående vilken kordinat som besköts senast samt använder sig av feedback från det inkomande meddelandet
                om resultatet på skottet , läger därefter in ett runlater ärende till controllern för att GUI inte ska fastna
                Den hämtar och oversätter värdet på kordinaterna direkt och skickar in i ärendet på grund av att runLater
                kan köras när nästa kordinat redan har lagts in och det skapar felaktiga värden när den då hämtar värderna direkt från GameBoard;
              */
    }
    public void uppDateRightBoard(String feedback){
        if(!feedback.equalsIgnoreCase("i")) {
            if (enemy.logicActive) {
                int x = Character.getNumericValue(enemy.getLastLogicalCoordinate().charAt(0));
                int y = enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1));
                String  f = feedback;
                Platform.runLater( ()-> {
                    controller.hitOnCoordinate(true, f,x,y,enemy );
                });
            } else {
                int x = Character.getNumericValue(enemy.getLastRandomCoordinate().charAt(0));
                int y = enemy.convertCoordinate(enemy.getLastRandomCoordinate().charAt(1));
                String f = feedback;
                Platform.runLater(() -> {
                    controller.hitOnCoordinate(true, f,x,y,enemy);

                });

            }

        }

    }
    /*
    Uppdaterar grafiken på den vänstra spelplanen, den tar emot feedback på skottet samt vilken koordinat
    översätter kordinaten till  int värden och skapar ett runLater ärende.
     */
    public void uppdateLeftBoard(String feedback, String coordinate){
        int row;
        int colum;
        colum = Character.getNumericValue(coordinate.charAt(0));
        row = player.convertCoordinate(coordinate.charAt(1));
        Platform.runLater(() -> {
            controller.hitOnCoordinate(false, feedback, colum, row,player);
        });

    }
    /*
       Metod som tar emot feedbacken på senaste skottet och väljer därefter vilken medtod i GameBoard
       som skall kallas på för att ta fram nästa koordinat
     */
    public String nextCoordinate(String feedback){
        String nextCoordinate = "";
        switch (feedback){

            case "m":
                if(enemy.logicActive){
                    nextCoordinate = enemy.nextLogicalCoordinate(feedback);
                }else {
                    nextCoordinate = enemy.getRandomCoordinate();
                }
                break;
            case "h":
                if(enemy.logicActive) {
                    nextCoordinate = enemy.nextLogicalCoordinate(feedback);
                } else {
                    nextCoordinate  = enemy.startLogicalCoordinate();
                }
                break;
            case "s":
                enemy.resetLogicalCoordinate();
                nextCoordinate = enemy.getRandomCoordinate();

                break;
            case "i":
                nextCoordinate = enemy.getRandomCoordinate();
                break;
        }
        return nextCoordinate;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }


}
