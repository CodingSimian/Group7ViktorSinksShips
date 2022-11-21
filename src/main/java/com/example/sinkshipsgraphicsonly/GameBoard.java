package com.example.sinkshipsgraphicsonly;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    //properties

    // array som användas för att skapa all squares med rätt namn
    char[] yAxis = {'a', 'b', 'c', 'd', 'e', 'f', 'g','h','i','j'}; //Bytte namn från xAxis till yAxis, Viktor

    // Innehåller alla skepp som skapas.
    ArrayList<Ship> fleet = new ArrayList<>();

    // 2D array som innehåller alla Squares
    public Square[][] SquareGrid = new Square[10][10];

    // String List som innehåller kordinater som ej är beskjutna.
    List<String> validCoordinates = new ArrayList<>();
    // String list som logiken sparar träffar i , töms när skeppet är sänkt
    List<String> logicalHits = new ArrayList<>();

    private String lastRandomCoordinate;        // innehåller sista koordinaten retunerad av randomCoordinat;
    private String lastLogicalCoordinate;      // innehåller sista koordinaten retunerade av logicalCoordinate

    private boolean north, west, east, south;  // boolean som logicalCoordinate använder för att bestämma nästa koordinat
    private boolean vertical;                   //  används för att bestäma riktning
    private boolean horizontal;                 //  används för att bestäma riktning

    boolean logicActive;               // boolean som håller reda på om logiken är aktiverad.
    String [] logicalCoordinates = new String[4];  // om logik är true innehåller denna array möjliga riktignar att välja på.


    Random random = new Random();



    GameBoard() {
    }

    // populerar spelplanen med Squares
    public void buildGameBoard(){

        for(int i = 0; i< yAxis.length; i++){
            SquareGrid[i][0] = new Square("0"+ yAxis[i]);
            validCoordinates.add(getSquare(i,0).getName());
            for(int j = 1; j<10; j++){
                SquareGrid[i][j] = new Square(""+ j + yAxis[i]);
                validCoordinates.add(getSquare(i,j).getName());
            }

        }

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
                 validCoordinates.remove(getSquare(row,colum +1).getName());
             }
    }
    public String neighbourEast(String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(colum < SquareGrid.length-2 && validCoordinates.contains(getSquare(row,colum+1).getName())) {
                return getSquare(row, colum + 1).getName();

        }else return "Null";

    }
    public void neighbourWest( int row, int colum){
        if(colum > 0) {
            getSquare(row,colum-1).setOccupied();
            validCoordinates.remove(getSquare(row,colum-1).getName());
        }
    }
    public String neighbourWest( String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(colum > 0 && validCoordinates.contains(getSquare(row,colum-1).getName())) {
                return getSquare(row, colum - 1).getName();
        }else return "NULL";

    }

    public void neighbourNorth( int row, int colum){
        if(row > 0) {
            getSquare(row -1, colum).setOccupied();
            validCoordinates.remove(getSquare(row -1,colum).getName());
        }
    }
    public String neighbourNorth(String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0));
        int row = convertCoordinate(coordinate.charAt(1));
        if(row > 0 && validCoordinates.contains(getSquare(row -1,colum).getName())) {
            return getSquare(row -1, colum).getName();
        }else return "Null";
    }
    public void neighbourNorthEast(int row , int colum){

        if(row > 0 && colum < SquareGrid.length-1) {
             getSquare(row-1,colum +1).setOccupied();
            validCoordinates.remove(getSquare(row -1,colum +1).getName());
        }
    }
    public void neighbourNorthWest( int row, int colum){
        if(row > 0 && colum > 0) {
            getSquare(row-1,colum-1).setOccupied();
            validCoordinates.remove(getSquare(row -1, colum-1).getName());
        }
    }
    public void neighbourSouth( int row,int colum){
        if(row < SquareGrid.length-1) {
            getSquare(row+ 1,colum).setOccupied();
            validCoordinates.remove(getSquare(row + 1, colum).getName());
        }
    }
    public String neighbourSouth( String coordinate){
        int colum = Character.getNumericValue(coordinate.charAt(0)) ;
        int row = convertCoordinate(coordinate.charAt(1));
        if(row < SquareGrid.length-1 && validCoordinates.contains(getSquare(row + 1,colum).getName())) {
           return getSquare(row+ 1,colum).getName();
        }else return "null";
    }

    public void neighbourSouthEast( int row , int colum){
        if(row < SquareGrid.length-1 && colum < SquareGrid.length-1) {
            getSquare(row+1,colum+1).setOccupied();
            validCoordinates.remove(getSquare(row +1,colum +1).getName());
        }

    }
    public void neighbourSouthWest( int row , int colum){
        if(row < SquareGrid.length-1 && colum > 0) {
            getSquare(row+1,colum-1).setOccupied();
            validCoordinates.remove(getSquare(row+1, colum -1).getName());
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

        if(coordinate.length() == 2) {
            int row = convertCoordinate(coordinate.charAt(1));
            int colum = Character.getNumericValue(coordinate.charAt(0));

            feedback = getSquare(row, colum).hit();
            if (feedback.equalsIgnoreCase("s")) {
                fleet.remove(getSquare(row, colum).getShip());
                if (fleet.isEmpty()) {
                    feedback = "gameover";
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
                    break;
                }
            }
        }else{
            valid = false;
        }

         if(valid){
             for(int i = 0; i<ship.getLength(); i ++){
                 getSquare(row+i,colum).addShip(ship);
             }
             for(int i = 0; i<ship.getLength(); i++){
                 setNeighboursOcupied(row+i,colum);
             }
         }
         return valid;
    }

    private boolean placeShipHorizontally(int row, int colum, Ship ship){
        boolean valid = true;
        if( colum+ship.getLength() < SquareGrid.length) {
            for (int i = 0; i < ship.getLength(); i++) {
                if (getSquare(row , colum + i).isOccupied()) {
                    valid = false;
                    break;
                }
            }
        }else{
            valid = false;

        }
        if(valid){
            for(int i = 0; i<ship.getLength(); i ++){
                getSquare(row,colum + i).addShip(ship);

            }
            for(int i = 0; i<ship.getLength(); i++){
                setNeighboursOcupied(row,colum + i);
            }

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
        boolean reset = false;
        boolean valid;
        int tries = 0;
        for(Ship ship:fleet){
            if(reset){
                System.out.println("Reseted");
                break;
            }
            valid = false;
            int max = 10- ship.getLength();
            while(!valid) {

                if(tries > 200){
                    System.out.println("Hängt sig start om");
                    reset = true;
                    validCoordinates.clear();
                    buildGameBoard();
                    placeFleetAtRandom();
                    break;
                }

                if (random.nextInt(2) == 1) {
                    valid = placeShipVerticaly(random.nextInt(max), random.nextInt(10), ship);
                } else {
                    valid = placeShipHorizontally(random.nextInt(10), random.nextInt(max), ship);
                }
                tries++;
            }


        }
        System.out.println("Reset");
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
        logicalHits.add(lastRandomCoordinate);
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
        }else {
            coordinate = getRandomCoordinate();
        }

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
            logicalHits.add(lastLogicalCoordinate);
            vertical = true;
            if(north){
                coordinate = neighbourNorth(lastLogicalCoordinate);
                if(coordinate.equalsIgnoreCase("NULL")){
                    coordinate = newLogicDirection(1);
                    north = false;
                }


            }else if(west){
                horizontal = true;
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

      // Metod som används av nextLogicalCoordinate metod för att byta rikting på skotten ifall den fått en miss.

    private String newLogicDirection(int a){                                 // nextLogicalCoordinat kallar på denna metod ifall den får en miss för att byta riktning,.
        String coordinate = "NULL";
              if(a == 1 && vertical && !logicalCoordinates[3].equalsIgnoreCase("NULL")){
                  coordinate = logicalCoordinates[3];
                  logicalCoordinates[3] = "NULL";
                  south = true;
              }else if(a == 2 && horizontal && !logicalCoordinates[2].equalsIgnoreCase("NULL")){
                  coordinate = logicalCoordinates[2];
                  logicalCoordinates[2] = "NULL";
                  east = true;
              }else{
                  for(int i = a;i<logicalCoordinates.length; i++ ){
                    if (!logicalCoordinates[i].equalsIgnoreCase("NULL")) {
                       coordinate = logicalCoordinates[i];
                       logicalCoordinates[i] = "NULL";
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

        }
        if(coordinate.equalsIgnoreCase("NULL")){
            coordinate = getRandomCoordinate();
        }
            return coordinate;
    }

    public void resetLogicalCoordinate(){                             // Kallas på när moståndaren meddelar att ett skepp sjunkit.
        logicActive = false;                                          // stänger av och återställer logiken.
        north = false; west = false; east = false; south = false;
        horizontal = false; vertical = false;
        logicalHits.add(lastLogicalCoordinate);
        for(String coordinate: logicalHits){
            int colum = Character.getNumericValue(coordinate.charAt(0));
            int row = convertCoordinate(coordinate.charAt(1));
            setNeighboursOcupied(row,colum);
        }
        logicalHits.clear();
    }

    public String getLastRandomCoordinate(){
        return lastRandomCoordinate;
    }

    public String getLastLogicalCoordinate(){
        return lastLogicalCoordinate;
    }

}
