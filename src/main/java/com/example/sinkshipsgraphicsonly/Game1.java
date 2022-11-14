package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;
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


    Game1(boardViewController controller )  {
        player = new GameBoard();
        player.buildGameBoard();
        player.buildFleet(1, 2, 3, 4);
        player.placeFleetAtRandom();
        System.out.println(player.fleet.size());
        enemy = new GameBoard();
        enemy.buildGameBoard();
        delay = 0;
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

        if(server){
            connection.newServer();
        }else connection.connectToServer();

        if (connection.isConnected()) {
            System.out.println("test");
            outGoingMessage = "I shoot ";
            outGoingMessage += enemy.getRandomCoordinate();
            System.out.println(outGoingMessage);
            connection.sendMessage(outGoingMessage);
        }
        while (!gameover) {

            incomingMesssage = connection.reciveMessage();
            System.out.println("Incoming: " + "  " + incomingMesssage);
            outGoingMessage = breakDownMessage(incomingMesssage);
            Thread.sleep(delay);
            round++;
            System.out.println("Outgoing: round " + round + "  " + outGoingMessage);
            connection.sendMessage(outGoingMessage);


        }
    }
        // Plockar isär informationen från motståndaren och tar fram ett nytt meddelande beroende på svar från GameBoard.
        // Samt uppdatera Grafiska spelplanerna.
        public String breakDownMessage(String message){
            scan = new Scanner(message);
            int row;
            int colum;
            String newMessage;

            if(message.equalsIgnoreCase("GAMEOVER")){
                gameover = true;
                return "GAMEOVER";
            }
            String feedback = scan.next();
            scan.next();
            newMessage = scan.next();
            colum = Character.getNumericValue(newMessage.charAt(0));
            row = player.convertCoordinate(newMessage.charAt(1));
            newMessage = player.fire(newMessage);
            String finalNewMessage = newMessage;

            Platform.runLater(() -> {
                controller.hitOnCoordinate(false, finalNewMessage, colum, row);
            });
            if(!feedback.equalsIgnoreCase("I")) {
                if (enemy.logicActive) {
                    System.out.println(feedback + " " + enemy.getLastLogicalCoordinate());
                    int x = Character.getNumericValue(enemy.getLastLogicalCoordinate().charAt(0));
                    int y = enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1));
                    String  f = feedback;
                    Platform.runLater( ()-> {
                        controller.hitOnCoordinate(true, f,x,y );

                    });
                } else {
                    System.out.println(feedback + " " + enemy.getLastRandomCoordinate());
                    int x = Character.getNumericValue(enemy.getLastRandomCoordinate().charAt(0));
                    int y = enemy.convertCoordinate(enemy.getLastRandomCoordinate().charAt(1));
                    String f = feedback;
                    Platform.runLater(() -> {
                        controller.hitOnCoordinate(true, f,x,y);

                    });

                }

            }
            newMessage += " Shoot " ;

           switch (feedback){

               case "M": // kalla på getRandomCoordinate();
                   if(enemy.logicActive){
                       newMessage += enemy.nextLogicalCoordinate(feedback);
                   }else {
                       newMessage += enemy.getRandomCoordinate();
                   }
                   break;
               case "H": // kalla på getLogicalCooordinate();
                   if(enemy.logicActive) {
                       newMessage += enemy.nextLogicalCoordinate(feedback);
                       System.out.println("Nextlogical");
                   } else {
                       newMessage += enemy.startLogicalCoordinate();
                       System.out.println("startLogical");
                   }
                   break;
               case "S": // reset logicalCoordinate(); kalla därefter på getRandomCoordinate();
                   newMessage += enemy.getRandomCoordinate();
                   System.out.println("Reset Logic");
                   enemy.resetLogicalCoordinate();
                   break;
               case "I":
                   newMessage += enemy.getRandomCoordinate();
                   break;
           }

            return newMessage;
        }

}
