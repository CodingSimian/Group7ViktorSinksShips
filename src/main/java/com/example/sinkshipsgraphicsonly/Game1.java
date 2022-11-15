package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.util.Scanner;

public class Game1
{

    protected GameBoard player;
    protected GameBoard enemy;

    private int delay;  // TickValue värdet på själva slidern och göra om det till en long för att
//Delay är inte kopplad till controllern, men TickValue är det.
    //Grejen som ska göras är att delayen ska kopplas till TickValue, så när tickValue ändras så ändras dalyen med automatisk.
    //TickValue finns i Game1 och i borderController
    //delay = tickvalue, så att delayen är lika med thickvalue och inte referar, obs fallgrupp.
    //TickValue = är delayen/hastigheten.
    // i sånt fall så är det bara att koppla ihop delay och thickValue.
    //i game1 så är delay = controller.ThickValue;
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
            if(incomingMesssage.equalsIgnoreCase("GAMEOVER")){
                uppDateRightBoard("H");
                gameover = true;
            }
            if(!gameover) {
                System.out.println("Incoming: " + "  " + incomingMesssage);
                outGoingMessage = breakDownMessage(incomingMesssage);

                Thread.sleep(delay);
                round++;
                System.out.println("Outgoing: round " + round + "  " + outGoingMessage);
                connection.sendMessage(outGoingMessage);
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
            if(feedbackLeftBoard.equalsIgnoreCase("Gameover")){
                uppdateLeftBoard("H",newMessage);
                newMessage = feedbackLeftBoard;
            }else{
                uppdateLeftBoard(feedbackLeftBoard,newMessage);
                newMessage = feedbackLeftBoard;
                newMessage += " Shoot " ;
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
                  System.out.println("LeftBoard: " + feedback + " " + colum + " "  +row);
                  Platform.runLater(() -> {
                      controller.hitOnCoordinate(false, feedback, colum, row);
                  });

              }
              /*
                 Metod som tar emot feedbacken på senaste skottet och väljer därefter vilken medtod i GameBoard
                 som skall kallas på för att ta fram nästa koordinat
               */
              public String nextCoordinate(String feedback){
                   String nextCoordinate = "";
                  switch (feedback){

                      case "M": // kalla på getRandomCoordinate();
                          if(enemy.logicActive){
                              nextCoordinate = enemy.nextLogicalCoordinate(feedback);
                          }else {
                              nextCoordinate = enemy.getRandomCoordinate();
                          }
                          break;
                      case "H": // kalla på getLogicalCooordinate();
                          if(enemy.logicActive) {
                              nextCoordinate = enemy.nextLogicalCoordinate(feedback);
                              System.out.println("Nextlogical");
                          } else {
                              nextCoordinate  = enemy.startLogicalCoordinate();
                              System.out.println("startLogical");
                          }
                          break;
                      case "S": // reset logicalCoordinate(); kalla därefter på getRandomCoordinate();
                          nextCoordinate = enemy.getRandomCoordinate();
                          System.out.println("Reset Logic");
                          enemy.resetLogicalCoordinate();
                          break;
                      case "I":
                          nextCoordinate = enemy.getRandomCoordinate();
                          break;
                  }
                  return nextCoordinate;
              }
}
