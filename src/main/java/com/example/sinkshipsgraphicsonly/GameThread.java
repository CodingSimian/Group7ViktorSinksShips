package com.example.sinkshipsgraphicsonly;

import java.io.IOException;

public class GameThread extends Thread{
    private boardViewController controller;
    public Game1 game;

    @Override
    public void run() {
        try {
            game.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    GameThread(boardViewController controller){
         game = new Game1(controller);

    }


}
