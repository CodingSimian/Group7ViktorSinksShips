package com.example.sinkshipsgraphicsonly;



import java.util.ArrayList;

public class Square {

    //properties
    private Ship ship;
    private String name;
    private boolean occupied;
    private boolean hit;

    private boolean hasShip;


    private ArrayList<Square> neighbours = new ArrayList<>();

    //konstruktor
    Square(String name){
        this.name = name;
        occupied = false;
        hit = false;


    }

    public String getName(){
        return this.name;
    }
    public boolean isOccupied(){
        return this.occupied;
    }
    // metod för att placera ett skepp på rutan
    public void addShip(Ship ship){
        this.ship = ship;
        this.occupied = true;
        this.hasShip = true;

    }

    public Ship getShip(){
        return this.ship;

    }


    public void setOccupied(){
        this.occupied = true;
    }
    // metod som användas när spelplanen meddelar att rutan är beskjuten ,
    // om det finns ett skepp på rutan skickar den vidare meddelandet , och skickar sedan feedback tillbaka om det är träff/miss/sänkt skepp

    public String hit(){
        this.hit = true;
        String feedback;
       if(ship != null) {
           ship.hit();
           feedback = "h";
           if(ship.isSunk()){
               feedback=  "s";

           }

       }else feedback = "m";

       return feedback;
    }

    public boolean isHit(){
        return this.hit;
    }

    public boolean isHasShip(){
        return this.hasShip;
    }
}
