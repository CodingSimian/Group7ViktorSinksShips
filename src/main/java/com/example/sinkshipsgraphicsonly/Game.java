package com.example.sinkshipsgraphicsonly;

import java.util.Scanner;

public class Game
{
    //Properties
   // 2 st gameBoard, 1 för spelaren och 1 för motståndaren. 
    private GameBoard player1; //Håller koll på min spelplan egna skepp
    private GameBoard player2; //Håller koll på en tom spelplan för att ha grafiken.

    private String temporaryDataOfShipsHit;
    //koordinaten för första skottet som träffade skeppet vi vill sänka, om vi klurat ut riktningen etc

    private String coordinatesToShoot;
    //Som skjuter på koordinater bredvid skeppet sm blivit träffat i temporaryDataOfShipsHit
    //Samt borde sedan i metoden köra en loop med incrementering?


    public Game(Connection sending)
    {
        Sending = sending; //Tar klassen i Connection och använder oss av alla metoderna.
        //sendning för att komma åt metoderna.
        player1 = new GameBoard(); //Skapar en instans av GameBoard. 
        player2 = new GameBoard(); //Skapar en instans av GameBoard.
        //Gameboard blir inte byggt av bara construktion utan behöver ha metoderna med.
    }

    public Game(GameBoard player1, GameBoard player2, String temporaryDataOfShipsHit, String coordinatesToShoot, Connection sending)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.temporaryDataOfShipsHit = temporaryDataOfShipsHit;
        this.coordinatesToShoot = coordinatesToShoot;
        Sending = sending;
    }

    //Metoder1
    public void test(){
        player1.fire("6A");//Skjuter t.ex på rad 1 column 1 och skickar tillbaka feedback vad som hände med skotten.
        player1.convertCoordinate('A'); //konvertar stringvärden och konverterar till en siffra, så att man kan skicka dem vidare till fire sen.
    }
    private String shootRandom()
    {
        //borde ha math.random för att skjuta random?
        return null; //borde returerna något värde?
    }

    public String intelligenceAI(){
        boolean ShipsIsHit = true;
        if(ShipsIsHit)
      {
       //   System.out.println(temporaryDataOfShipsHit +"A ship has been hit" + breakDownMessage//vilket skepp + c );
          //Som skjuter på en koordinat bredvid men inte vet vilken riktning skeppet behövs skjutas på.

         // Vilket skepp  + buildGameBoard) +;//Har för mig att läraren sa att vi behöver bygga en ny gameBoard efter varje skott?
      } else{
           System.out.println("No hit =  Miss!!!  " + shootRandom());
          //  +   + buildGameBoard +);} //Har för mig att läraren sa att vi behöver bygga en ny gameBoard efter varje skott?


        }
        return "hej";
   }




    //Scanner scanner = new Scanner();
    //Scanner.next(); //Skickar in strängen i den,

    // Message.dekrypt(); //Metoden ska ta emot en sträng och scannern ska läsa av strängen och dela upp den i tre delar bla göra den till en int.
    //Första delen är resultat och uppdatera motståndarens spelplan, 2:a e shoot (kan slängas) och 3:e är vad de vill skjuta på sen, d.v.s koordinaterna, bokstavsrad och sifferkolumn.
    //Connection.reciewMessage();
     //Vi behöver bygga en metod för att ta isär meddelandet och 1 metod för att bygga ihop strängen så att den blir korrekt formaterad.
    //Skapar randommetoden, spelare 1 skjuter först.math.random
    Connection Sending;
    


    // Getters & Setters.
    public GameBoard getPlayer1()
    {
        return player1;
    }

    public void setPlayer1(GameBoard player1)
    {
        this.player1 = player1;
    }

    public GameBoard getPlayer2()
    {
        return player2;
    }

    public void setPlayer2(GameBoard player2)
    {
        this.player2 = player2;
    }

    public String getTemporaryDataOfShipsHit()
    {
        return temporaryDataOfShipsHit;
    }

    public void setTemporaryDataOfShipsHit(String temporaryDataOfShipsHit)
    {
        this.temporaryDataOfShipsHit = temporaryDataOfShipsHit;
    }

    public String getCoordinatesToShoot() {
        return coordinatesToShoot;
    }

    public void setCoordinatesToShoot(String coordinatesToShoot)
    {
        this.coordinatesToShoot = coordinatesToShoot;
    }

    public Connection getSending()
    {
        return Sending;
    }

    public void setSending(Connection sending)
    {
        Sending = sending;
    }
}
