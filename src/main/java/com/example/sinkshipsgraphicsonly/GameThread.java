package com.example.sinkshipsgraphicsonly;

import javafx.application.Platform;

import java.io.IOException;


public class GameThread extends Thread{
    private boardViewController threadController;
    public Game1 game;


    @Override
    public void run() {
        try {
            game.play();
        } catch (Exception e) {
                if(e instanceof NullPointerException ){

                }else {

                    //e.printStackTrace();
                    if(!game.gameover) {
                        Platform.runLater(() -> {
                            try {

                                threadController.errorPopup();
                            } catch (IOException | InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                        });
                    }
                }

        }

    }
    GameThread(boardViewController controller){
         game = new Game1(controller);
         threadController = controller;

    }



}
