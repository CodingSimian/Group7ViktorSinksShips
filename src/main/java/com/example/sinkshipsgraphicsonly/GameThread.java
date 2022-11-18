package com.example.sinkshipsgraphicsonly;

import java.io.IOException;

public class GameThread extends Thread{
    private boardViewController controller;
    public Game1 game;
    private boolean active;

    @Override
    public void run() {
        this.active = true;
        try {
            game.play();
        } catch (IOException e) {
            System.out.println("Anslutningen bruten återvänder till start skärmen inom 5 sekunder.");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
             // kalla på match avbruten ruta
            // controller.returnToStart


            throw new RuntimeException(e);
        }

    }
    GameThread(boardViewController controller){
         game = new Game1(controller);
         controller = controller;

    }
    public boolean isActive(){
        return this.active;
    }


    public Game1 getGame(){
        return this.game;
    }


}
