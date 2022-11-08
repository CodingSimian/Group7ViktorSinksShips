package com.example.sinkshipsgraphicsonly;

import java.util.Scanner;

public class Game
{
    //Properties
   // 2 st gameBoard, 1 för spelaren och 1 för motståndaren. 
    GameBoard player1; //Håller koll på min spelplan egna skepp
    GameBoard player2; //Håller koll på en tom spelplan för att ha grafiken. 

    public Game(Connection sending)
    {
        Sending = sending; //Tar klassen i Connection och använder oss av alla metoderna.
        //sendnig. för att komma åt metoderna. 
        player1 = new GameBoard(); //Skapar en instans av GameBoard. 
        player2 = new GameBoard(); //Skaper en instans av GameBoard.
        //Gameboard blir inte byggt av bara construktion utan behöver ha metoderna med.
    }
    
    //Metoder1
    public void test(){
        player1.fire(1,1);//Skjuter t.ex på rad 1 column 1 och skickar tillbaka feedback vad som hände med skotten.
        player1.convertCoordinate("a"); //konverar stringvärden och konverterar till en siffra, så att man kan skicka dem vidare till fire sen.
    }
    Scanner scanner = new Scanner();
    Scanner.next(); //Skickar in strängen i den,

    // Message.dekrypt(); //Metoden ska ta emot en sträng och scannern ska läsa av strängen och dela upp den i tre delar bla göra den till en int.
    //Första delen är resultat och uppdatera motståndarens spelplan, 2:a e shoot (kan slängas) och 3:e är vad de vill skjuta på sen, d.v.s koordinaterna, bokstavsrad och sifferkolumn.
    //Connection.reciewMessage();
     //Vi behöver bygga en metod för att ta isär meddelandet och 1 metod för att bygga ihop strängen så att den blir korrekt formaterad.
    //Skapar randommetoden, spelare 1 skjuter först.math.random
    Connection Sending;
    


    // Getters & Setters.

}
