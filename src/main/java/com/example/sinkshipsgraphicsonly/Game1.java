package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.util.Scanner;

public class Game1 {

    protected GameBoard player;
    protected GameBoard enemy;

    private int delay;

    private Scanner scan;
    private boardViewController controller;

    Connection connection;
    int round;
    boolean gameover;
    String outGoingMessage;
    String incomingMesssage;
    boolean server;

    private String name;

    Game1(boardViewController controller )  {
        player = new GameBoard();
        player.buildGameBoard();
        player.buildFleet(1, 2, 3, 4);
        player.placeFleetAtRandom();
        System.out.println(player.fleet.size());
        enemy = new GameBoard();
        enemy.buildGameBoard();
        delay = 1500;
        gameover = false;
        round = 0;

        this.controller = controller;
        connection = new Connection();


    }

    public void waitForPlayer() throws IOException, InterruptedException {
        while(true) {
            if (connection.isConnected() || connection.isServer()) {
                play();
            }
        }
    }
    public void play() throws IOException, InterruptedException {
        /*if(server){
            name="Server";
        }else{
            name="Client";
        }*/

        if(server){
            name = "Server";
            connection.newServer();

        }else {
            name = "Client";
            connection.connectToServer();
        }

        if (connection.isConnected() && !server) { //Skrev om
            outGoingMessage = "i shot ";
            outGoingMessage += enemy.getRandomCoordinate();
            System.out.println(outGoingMessage);
            connection.sendMessage(outGoingMessage);
        }
        while (!gameover) {

            incomingMesssage = connection.reciveMessage();
            if(incomingMesssage.equalsIgnoreCase("GAMEOVER")){ //om incommingMessage är gameover,
                //Så är den instansen av programmet en vinnare. Och om outGoingMessage är gameoaver så är den
                //instansen förlorare.

                uppDateRightBoard("GAMEOVER");

                Platform.runLater( ()-> {
                    controller.showWinner(name);

                });
                gameover = true;
            }
            if(!gameover) {
                System.out.println("Incoming: " + "  " + incomingMesssage);
                outGoingMessage = breakDownMessage(incomingMesssage);

                delay = controller.delayValue();
                //Literally ba en metod som kallar på boardSlider.getValue()
                //Inlägd här för att tillgodose att hela spelet sker i en while-loop.
                Thread.sleep(delay);

                round++;
                System.out.println("Outgoing: round " + round + "  " + outGoingMessage);
                connection.sendMessage(outGoingMessage);

                if(outGoingMessage.equalsIgnoreCase("GAMEOVER")){
                    Platform.runLater( ()-> {
                        controller.showLoser(name);

                    });
                }
            }



        }
    }
    // Plockar isär informationen från motståndaren och tar fram ett nytt meddelande beroende på svar från GameBoard.
    // Samt kallar på metoder för att uppdatera grafiska spelplanerna;
    public String breakDownMessage(String message){
        scan = new Scanner(message);
        boolean gameover;
        String newMessage;
        String feedbackLeftBoard;
        String feedback = scan.next();
        scan.next();
        newMessage = scan.next();
        feedbackLeftBoard = player.fire(newMessage);
        uppDateRightBoard(feedback);
        if(feedbackLeftBoard.equalsIgnoreCase("gameover")){
            uppdateLeftBoard("gameover",newMessage);
            newMessage = feedbackLeftBoard;
        }else{
            uppdateLeftBoard(feedbackLeftBoard,newMessage);
            newMessage = feedbackLeftBoard;
            newMessage += " shot " ;
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
        if(!feedback.equalsIgnoreCase("I")) {
            if (enemy.logicActive) {
                System.out.println(feedback + " " + enemy.getLastLogicalCoordinate());
                int x = Character.getNumericValue(enemy.getLastLogicalCoordinate().charAt(0));
                int y = enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1));
                String  f = feedback;
                Platform.runLater( ()-> {
                    controller.hitOnCoordinate(true, f,x,y,enemy );

                });
            } else {
                System.out.println(feedback + " " + enemy.getLastRandomCoordinate());
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
                    System.out.println("Nextlogical");
                } else {
                    nextCoordinate  = enemy.startLogicalCoordinate();
                    System.out.println("startLogical");
                }
                break;
            case "s":
                nextCoordinate = enemy.getRandomCoordinate();
                System.out.println("Reset Logic");
                enemy.resetLogicalCoordinate();
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

    public void setDelay(int daDelay){
    this.delay = daDelay;
    }
}
