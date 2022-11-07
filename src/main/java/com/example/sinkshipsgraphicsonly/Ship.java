package com.example.sinkshipsgraphicsonly;

public abstract class Ship {

    // properties för abstrakta klassen så att alla klasser av typen ship har dessa properties
  protected String type;
  protected int lenght;
  protected int hits;
  protected boolean sunk;



  Ship(){

  }

  //metod som square objekten som skeppet ligger på anropar ifall den kordinaten blir träffad blir träffad
    // ifall den blivit träffad för många gånger så blir den sänkt.
  public void hit(){
      this.hits ++;
      if( hits>= lenght){
          this.sunk = true;

      }

  }

    public String getType() {
        return type;
    }

    public boolean isSunk() {
        return sunk;
    }

    public int getLength(){
      return lenght;
    }


}
     // sub klasser för alla typer av skepp
     // dessa ärver ju alla metoder och properties från abstrakta Ship
     // så dessa klasser har bara en konstruktor som gör dom till tex en carrier eller ett Battleship och sätter rätt längd och typ.
     class Carrier extends Ship{
      Carrier(){
          this.lenght = 5;
          this.type = "Carrier";
      }
   }
   class BattleShip extends Ship{
    BattleShip(){
        this.lenght = 4;
        this.type = "BattleShip";
    }
   }

   class Cruiser extends Ship{
    Cruiser(){
        this.lenght = 3;
        this.type = "Cruiser";
    }
   }

   class Submarine extends Ship{
    Submarine(){
        this.lenght = 2;
        this.type = "Submarine";
    }

   }




