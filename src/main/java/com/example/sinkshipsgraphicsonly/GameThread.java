package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;

import java.io.IOException;

public class GameThread extends Thread{
    private boardViewController test;
    public Game1 game;
    private boolean active;

    @Override
    public void run() {
        this.active = true;
        try {
            game.play();
        } catch (Exception e) {
            System.out.println("Anslutningen bruten återvänder till start skärmen inom 5 sekunder.");
            Platform.runLater( ()-> {
                try {
                    test.errorPopup();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

            });

        }

    }
    GameThread(boardViewController controller){
         game = new Game1(controller);
         test = controller;

    }
    public boolean isActive(){
        return this.active;
    }


    public Game1 getGame(){
        return this.game;
    }


}
