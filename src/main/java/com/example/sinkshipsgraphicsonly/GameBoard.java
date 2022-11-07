package com.example.sinkshipsgraphicsonly;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard {
    //properties

    // array som användas för att skapa all squares med rätt namn
    String[] xAxis = {"A", "B", "C", "D", "E", "F", "G", "H","I", "J"};

    // Innehåller alla skep som skapas.
    ArrayList<Ship> fleet = new ArrayList<>();

    // 2D array som innehåller alla Squares
    Square[][] SquareGrid = new Square[10][10];

    Random random = new Random();
    int antalSänkta;


    GameBoard() {


    }

    // populerar spelplanen med Squares
    public void buildGameBoard(){

        for(int i = 0; i< xAxis.length; i++){
            SquareGrid[i][0] = new Square(0+xAxis[i]);
            System.out.println(getSquare(i,0).getName()); // ta bort senare
            for(int j = 1; j<10; j++){
                SquareGrid[i][j] = new Square((j)+xAxis[i]);
                System.out.println(SquareGrid[i][j].getName()+ "i = " + i + " j= " + j); // ta bort senare
            }

        }
    }

    public void squareGridComunnication(){ // ta bort senare

        for(int i = 1; i <SquareGrid.length  ; i++){
            //System.out.println(SquareGrid[0][i].getName() + " " + i);
             setNeighboursOcupied(0,i);
            for(int j = 1; j<10; j++){
                //System.out.println(SquareGrid[j][i].getName());
                setNeighboursOcupied(j,i);

            }
        }

    }

    public void setOcuppied(Square square){

    }

    // metod för att hämta en referens från spelplanen
    public Square getSquare(int row, int colum){
        return SquareGrid[row][colum];
    }

    // följande metoder kontrollerar en Square utifrån angiven kordinat och sätter den till occupied så att inte skepp
    // kan placeras invid varandra
    public void neighbourEast(int row, int colum){
             if(colum < SquareGrid.length-2) {
                 getSquare(row,colum+1).setOccupied();
             }
    }
    public void neighbourWest( int row, int colum){
        if(colum > 0) {
            getSquare(row,colum-1).setOccupied();
        }
    }
    public void neighbourNorth( int row, int colum){
        if(row > 0) {
            //square.addNeighbour(getSquare(row - 1, colum));
            getSquare(row -1, colum).setOccupied();
        }
    }
    public void neighbourNorthEast( int row, int colum){
        if(row > 0 && colum < SquareGrid.length-1) {
            //square.addNeighbour(getSquare(row - 1, colum + 1));
            getSquare(row-1,colum +1).setOccupied();
        }
    }
    public void neighbourNorthWest( int row, int colum){
        if(row > 0 && colum > 0) {
            //square.addNeighbour(getSquare(row  - 1, colum - 1));
            getSquare(row-1,colum-1).setOccupied();
        }
    }
    public void neighbourSouth( int row,int colum){
        if(row < SquareGrid.length-1) {
            //square.addNeighbour(getSquare(row + 1, colum));
            getSquare(row+ 1,colum).setOccupied();
        }
    }
    public void neighbourSouthEast( int row , int colum){
        if(row < SquareGrid.length-1 && colum < SquareGrid.length-1) {
            //square.addNeighbour(getSquare(row + 1, colum + 1));
            getSquare(row+1,colum+1).setOccupied();
        }

    }
    public void neighbourSouthWest( int row , int colum){
        if(row < SquareGrid.length-1 && colum > 0) {
            //square.addNeighbour(getSquare(row + 1, colum - 1));
            getSquare(row+1,colum-1).setOccupied();
        }
    }

    // metod som användas för att ta närligande rutor ur spel när ett skepp placeras.
    // den kallar på flera mindre metoder som kontrollerar och ändrar respektive ruta ur spel
    public void setNeighboursOcupied( int row, int colum){
        neighbourNorth(row,colum);
        neighbourNorthEast( row,colum);
        neighbourNorthWest(row,colum);
        neighbourEast(row,colum);
        neighbourWest( row,colum);
        neighbourSouth(row,colum);
        neighbourSouthEast(row,colum);
        neighbourSouthWest(row,colum);
    }
    // metod för som tar emot en int för rad och int för colum , skjuter därefter på motsvarande Square på planen.
    public String fire(int row, int colum){
        String feedback;
        boolean gameover = false;

          feedback = getSquare(row,colum).hit();
          if(feedback.equalsIgnoreCase("S")){
              antalSänkta ++;
              fleet.remove(getSquare(row,colum).getShip());
              if(fleet.isEmpty()){
                  feedback = "GAMEOVER";
              }


          }

      return feedback;
    }



    // metod för att placera ett skepp vertikalt på spelplanen
    // den anropar sedan setNeighboursOccupied för att ta närliggande rutor ur spel.
    public boolean placeShipVerticaly(int row , int colum, Ship ship){
        boolean valid = true;
        if( row+ship.getLength() < SquareGrid.length) {
            for (int i = 0; i < ship.getLength(); i++) {
                if (getSquare(row + i, colum).isOccupied()) {
                    valid = false;
                    //System.out.println("Invalid placement");
                    break;
                }
            }
        }else{
            valid = false;
            //System.out.println("Ship to long");

        }

         if(valid){
             for(int i = 0; i<ship.getLength(); i ++){
                 getSquare(row+i,colum).addShip(ship);
                 System.out.println("Ship placed at" + getSquare(row+i,colum).getName());
             }
             for(int i = 0; i<ship.getLength(); i++){
                 setNeighboursOcupied(row+i,colum);
             }
         }
        System.out.println();
         return valid;
    }

    public boolean placeShipHorizontally(int row, int colum, Ship ship){
        boolean valid = true;
        if( colum+ship.getLength() < SquareGrid.length) {
            for (int i = 0; i < ship.getLength(); i++) {
                if (getSquare(row , colum + i).isOccupied()) {
                    valid = false;
                    //System.out.println("Invalid placement");
                    break;
                }
            }
        }else{
            valid = false;
            //System.out.println("Ship to long");

        }

        if(valid){
            for(int i = 0; i<ship.getLength(); i ++){
                getSquare(row,colum + i).addShip(ship);
                System.out.println("Ship placed at" + getSquare(row,colum + i).getName());
            }
            for(int i = 0; i<ship.getLength(); i++){
                setNeighboursOcupied(row,colum + i);
            }
            System.out.println();

        }
        return valid;
    }


    // metod för att bygga en flotta, tar emot 4st int values för att bestämma antal av varje skeep
    // ropar därefter på addShip för varje typ av skepp.
    public void buildFleet(int carrier, int battleship, int cruiser, int submarine) {
        addShip(0,carrier);
        addShip(1,battleship);
        addShip(2,cruiser);
        addShip(3,submarine);
    }

    // Metod som tar emot en int för att kunna välja vilka typ av skepp den skall bygga,
    public Ship buildShip(int type){
         Ship ship;
        switch(type){
            case 0:
                ship = new Carrier();
                break;
            case 1:
                ship = new BattleShip();
                break;
            case 2:
                ship = new Cruiser();
                break;
            default:
                ship = new Submarine();
                break;

        }
        return ship;
    }
    // metod som lägger till skepp i fleet ArrayList
    // den tar emot en int för att avgöra vilken typ av skepp och en int för att avgöra antal.
    public void addShip(int type, int numberOfShips){
        for(int i = 0; i<numberOfShips; i++) {
            fleet.add(buildShip(type));
        }

    }

    public void placeFleetAtRandom(){

        boolean valid;
        for(Ship ship:fleet){
            valid = false;
            int max = 10- ship.getLength();
            while(!valid) {
                if (random.nextInt(2) == 1) {
                    valid = placeShipVerticaly(random.nextInt(max), random.nextInt(10), ship);
                } else {
                    valid = placeShipHorizontally(random.nextInt(10), random.nextInt(max), ship);
                }
            }


        }
    }


    // test metod , ta bort senare
    public void printFleet(){
       for(Ship ship: fleet){
           System.out.println(ship.getType() + " " + ship.lenght);
       }
    }

    // test metod , ta bort senare
    public void printBoard(){
        for(int i = 0; i<SquareGrid.length; i++){

            System.out.print(getSquare(i,0).getName() + "  ");

            for(int j = 1; j< SquareGrid.length; j++){
                System.out.print(getSquare(i,j).getName() + "  ");
            }
            System.out.println();
            System.out.println("---------------------------------------------");
        }
    }

    public String getCoordinate(int coordinate){
        return xAxis[coordinate];
    }
    public int convertCoordinate(String coordinate){
        for(int i = 0; i< xAxis.length; i++){
            if(xAxis[i].equalsIgnoreCase(coordinate)){
                return i;

            }
        }
        return 0;
    }
    public boolean validCoordinate(int colum, int row){
        if(getSquare(colum,row).isHit()){
            return false;
        }else return true;

    }







}
