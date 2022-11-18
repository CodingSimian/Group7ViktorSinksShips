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
        } catch (Exception e) {
            System.out.println("Anslutningen bruten återvänder till start skärmen inom 5 sekunder.");

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
