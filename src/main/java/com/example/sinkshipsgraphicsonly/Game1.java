package com.example.sinkshipsgraphicsonly;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Game1 {

    private GameBoard player;
    private GameBoard enemy;

    private int delay;

    private Scanner scan;
    private boardViewController controller;

    Connection connection;
    int round;
    boolean gameover;
    String outGoingMessage;
    String incomingMesssage;


    Game1(Connection connection, boardViewController controller) {
        player = new GameBoard();
        player.buildGameBoard();
        player.buildFleet(1, 2, 3, 4);
        player.placeFleetAtRandom();
        System.out.println(player.fleet.size());
        enemy = new GameBoard();
        enemy.buildGameBoard();
        this.connection = connection;
        delay = 1000;
        gameover = false;
        round = 0;
        this.controller = controller;

    }
    public void play() throws IOException, InterruptedException {

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
            colum = newMessage.charAt(0);
            row = player.convertCoordinate(newMessage.charAt(1));
            newMessage = player.fire(newMessage);

            switch(newMessage){
                case "M":
                    controller.hitOnCoordinate(false,"M",colum,row);
                    newMessage += player.getRandomCoordinate();
                    break;
                case "H":
                    // row, colum kalla på kontroller för att uppdatera player Gameboard;

                        controller.hitOnCoordinate(false, "H", colum, row);
                    newMessage += player.getLogicalCoordinate();

                    break;
                case "S":
                    //  row, colum kalla på kontroller för att uppdatera player Gameboard;
                    controller.hitOnCoordinate(false, "S",Character.getNumericValue(player.getLastLogicalCoordinate().charAt(0)), player.convertCoordinate(player.getLastLogicalCoordinate().charAt(1)));
                    newMessage += player.getRandomCoordinate();
                    break;

            }

            newMessage += " Shoot ";

           switch (feedback){

               case "M": // kalla på getRandomCoordinate();
                   controller.hitOnCoordinate(true,"M",Character.getNumericValue(enemy.getLastRandomCoordinate().charAt(0)), enemy.convertCoordinate(enemy.getLastRandomCoordinate().charAt(1)));
                   newMessage += enemy.getRandomCoordinate();
                   break;
               case "H": // kalla på getLogicalCooordinate();
                   if(enemy.logicActive) {
                       controller.hitOnCoordinate(true, "H", Character.getNumericValue(enemy.getLastLogicalCoordinate().charAt(0)), enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1)));
                   }
                   else
                       controller.hitOnCoordinate(true, "H", Character.getNumericValue(enemy.getLastRandomCoordinate().charAt(0)), enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1)));
                   newMessage += enemy.getLogicalCoordinate();
                   break;
               case "S": // reset logicalCoordinate(); kalla därefter på getRandomCoordinate();
                   controller.hitOnCoordinate(true, "S",Character.getNumericValue(enemy.getLastLogicalCoordinate().charAt(0)), enemy.convertCoordinate(enemy.getLastLogicalCoordinate().charAt(1)));
                   newMessage += enemy.getRandomCoordinate();
                   break;
           }

            return newMessage;
        }

}
