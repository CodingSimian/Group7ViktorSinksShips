package com.example.sinkshipsgraphicsonly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    //properties

    // array som användas för att skapa all squares med rätt namn
    char[] yAxis = {'A', 'B', 'C', 'D', 'E', 'F', 'G','H','I','J'}; //Bytte namn från xAxis till yAxis, Viktor

    // Innehåller alla skepp som skapas.
    ArrayList<Ship> fleet = new ArrayList<>();

    // 2D array som innehåller alla Squares
    public Square[][] SquareGrid = new Square[10][10];

    // String List som innehåller kordinater som ej är beskjutna.
    List<String> validCoordinates = new ArrayList<>();

    private String lastRandomCoordinate;        // innehåller sista koordinaten retunerad av randomCoordinat;
    private String lastLogicalCoordinate;      // innehåller sista koordinaten retunerade av logicalCoordinate
    String lastCoordinateHit;
    boolean north, west, east, south;  // boolean som logicalCoordinate använder för att bestämma nästa koordinat
    boolean logicActive;               // boolean som håller reda på om logiken är aktiverad.
    String [] logicalCoordinates = new String[4];  // om logik är true innehåller denna array möjliga riktignar att välja på.


    Random random = new Random();
    int antalSänkta;


    GameBoard() {


    }

    // populerar spelplanen med Squares
    public void buildGameBoard(){

        for(int i = 0; i< yAxis.length; i++){
            SquareGrid[i][0] = new Square("0"+ yAxis[i]);
            validCoordinates.add(getSquare(i,0).getName());
            //System.out.println(getSquare(i,0).getName()); // ta bort senare
            for(int j = 1; j<10; j++){
                SquareGrid[i][j] = new Square(""+ j + yAxis[i]);
                validCoordinates.add(getSquare(i,j).getName());
                //System.out.println(SquareGrid[i][j].getName()+ "i = " + i + " j= " + j); // ta bort senare
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
    public String neighbourEast(String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(colum < SquareGrid.length-2) {
            return getSquare(row,colum+1).getName();
        }else return "Null";
    }
    public void neighbourWest( int row, int colum){
        if(colum > 0) {
            getSquare(row,colum-1).setOccupied();
        }
    }
    public String neighbourWest( String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(colum > 0) {
            return getSquare(row,colum-1).getName();
        }else return "Null";
    }



    public void neighbourNorth( int row, int colum){
        if(row > 0) {
            getSquare(row -1, colum).setOccupied();
        }
    }
    public String neighbourNorth(String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(row > 0) {
            return getSquare(row -1, colum).getName();
        }else return "Null";
    }
    public void neighbourNorthEast(int row , int colum){

        if(row > 0 && colum < SquareGrid.length-1) {
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
    public String neighbourSouth( String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0)) ;
        int row = convertCoordinate(coordinate.charAt(1));
        if(row < SquareGrid.length-1) {
           return getSquare(row+ 1,colum).getName();
        }else return "null";
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
    // metod för som tar emot en string i formatet siffra för colum och bokstav för row tex, "6A" , skjuter därefter på motsvarande Square på planen.
    public String fire(String coordinate){
        String feedback;
        boolean gameover = false;
        if(coordinate.length() == 2) {
            int row = convertCoordinate(coordinate.charAt(1));
            int colum = Character.getNumericValue(coordinate.charAt(0));

            feedback = getSquare(row, colum).hit();
            if (feedback.equalsIgnoreCase("S")) {
                antalSänkta++;
                fleet.remove(getSquare(row, colum).getShip());
                if (fleet.isEmpty()) {
                    feedback = "GAMEOVER";
                }


            }
        }else feedback = "Felaktigt koordinat";

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

    private boolean placeShipHorizontally(int row, int colum, Ship ship){
        boolean valid = true;
        if( colum+ship.getLength() < SquareGrid.length) {
            for (int i = 0; i < ship.getLength(); i++) {
                if (getSquare(row , colum + i).isOccupied()) {
                    valid = false;
                    System.out.println("Invalid placement");
                    break;
                }
            }
        }else{
            valid = false;
            System.out.println("Ship to long");

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
    private Ship buildShip(int type){
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
    private void addShip(int type, int numberOfShips){
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
           System.out.println(ship.getType() + " " + ship.length);
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

    public char getCoordinate(int coordinate){
        return yAxis[coordinate];
    }
    // tar emot och char som representerar en koordinat och retunera motsvarande int värde i spelplanen
    public int convertCoordinate(char coordinate){
        for(int i = 0; i< yAxis.length; i++){
            if(yAxis[i] == coordinate){
                return i;

            }
        }
        return 0;
    }

    // Metod som använder sig av listan validCoordinates för att retunera en random utvald koordinat som en String
    // Den använder sig av Collections.Shuffle för att få en random koordinat samt tar bort den retunerade koordinaten från listan.
    public String getRandomCoordinate (){  // ta bort senare
        Collections.shuffle(validCoordinates);
        if(!validCoordinates.isEmpty()) {
            lastRandomCoordinate = validCoordinates.get(0);
            return validCoordinates.remove(0);
        }else return "Empty";

    }

    public String startLogicalCoordinate(){ // Metod som kallas på ifall man får en träff detta är starten i logik kedjan
        String coordinate = "";
        if(lastRandomCoordinate.length() == 2 && !logicActive) {
            createLogicalCoordinateList();

            for (int i = 0; i < logicalCoordinates.length; i++) {                  // Bestämmer i vilken riktig första skotten skall vara
                if (!logicalCoordinates[i].equalsIgnoreCase("NULL")) {  // beronde på vilka möjliga riktningar som finns.
                    coordinate = logicalCoordinates[i];
                    logicalCoordinates[i] = "NULL";
                    lastLogicalCoordinate = coordinate;
                    validCoordinates.remove(coordinate);
                    logicActive = true;

                    switch (i) {
                        case 0:
                            north = true;
                            break;
                        case 1:
                            west = true;
                            break;
                        case 2:
                            east = true;
                            break;
                        case 3:
                            south = true;
                            break;
                    }
                    break;
                }
            }
            return coordinate;
        }else coordinate = getRandomCoordinate();





        return coordinate;


    }

    public void createLogicalCoordinateList(){                                  // skapar listan med möjliga riktningar för logik kedjan.
                logicalCoordinates[0] = neighbourNorth(lastRandomCoordinate);
                logicalCoordinates[1] = neighbourWest(lastRandomCoordinate);
                logicalCoordinates[2] = neighbourEast(lastRandomCoordinate);
                logicalCoordinates[3] = neighbourSouth(lastRandomCoordinate);
    }

    public String nextLogicalCoordinate(String feedback){             // kallas på om logik är aktiv och tar fram nästa koordinat baserat på feedbacken från senaste logiska kordinaten.
        String coordinate = "";
        if(feedback.equalsIgnoreCase("H")){
            if(north){
                coordinate = neighbourNorth(lastLogicalCoordinate);
                if(coordinate.equalsIgnoreCase("NULL")){
                    coordinate = newLogicDirection(1);
                    north = false;
                }


            }else if(west){
                coordinate = neighbourWest(lastLogicalCoordinate);
                if(coordinate.equalsIgnoreCase("NULL")) {
                    coordinate = newLogicDirection(2);
                    west = false;
                }
            }else if(east){
                coordinate = neighbourEast(lastLogicalCoordinate);
                if(coordinate.equalsIgnoreCase("NULL")){
                    coordinate = newLogicDirection(3);
                    east = false;
                }
            }else if(south){
                coordinate = neighbourSouth(lastLogicalCoordinate);


            }
        }else{
            if(north){
                coordinate = newLogicDirection(1);
                north = false;
            }else if(west){
                coordinate = newLogicDirection(2);
                west = false;
            }else if(east){
                east = false;
                coordinate = newLogicDirection(3);
            }
        }
        lastLogicalCoordinate = coordinate;
        validCoordinates.remove(coordinate);

        return coordinate;

    }
    /*
       Metod som används av nextLogicalCoordinate metod för att byta rikting på skotten ifall den fått en miss.
     */
    private String newLogicDirection(int a){                                 // nextLogicalCoordinat kallar på denna metod ifall den får en miss för att byta riktning,.
        String coordinate = "NULL";

        for(int i = a;i<logicalCoordinates.length; i++ ){
            if (!logicalCoordinates[i].equalsIgnoreCase("NULL")) {
                coordinate = logicalCoordinates[i];
                logicalCoordinates[i] = "NULL";
                //lastLogicalCoordinate = coordinate;
                //validCoordinates.remove(coordinate);
                switch (i) {
                    case 0:
                        north = true;
                        break;
                    case 1:
                        west = true;
                        break;
                    case 2:
                        east = true;
                        break;
                    case 3:
                        south = true;
                        break;
                }
                break;
            }

        }
        if(coordinate.equalsIgnoreCase("NULL")){
            coordinate = getRandomCoordinate();
        }
            return coordinate;
    }

    public void resetLogicalCoordinate(){                             // Kallas på när moståndaren meddelar att ett skepp sjunkit.
        logicActive = false;                                          // stänger av och återställer logiken.
        north = false; west = false; east = false; south = false;
    }

    public String getLastRandomCoordinate(){
        return lastRandomCoordinate;
    }

    public String getLastLogicalCoordinate(){
        return lastLogicalCoordinate;
    }











}
