package com.example.sinkshipsgraphicsonly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

//Denna kod har Niclas skrivit. Dock implementeras den inte just nu i detta projekt då endast grafiken visas i denna version.

public class connection {

    boolean server;
    boolean connected;
    Socket user;
    ServerSocket serverSocket;
    InputStreamReader inputReader;
    BufferedReader buffReader;
    PrintWriter printWriter;



    connection(){
        server = false;
        connected = false;
    }




    public void newServer() throws IOException {
        if(!connected) {
            try {
                serverSocket = new ServerSocket(4499);
                System.out.println("Server Running");
                user = serverSocket.accept();
                System.out.println("Client Connected");
                inputReader = new InputStreamReader(user.getInputStream());
                buffReader = new BufferedReader(inputReader);
                printWriter = new PrintWriter(user.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            server = true;

        }
    }

    public void newServerConnection()throws IOException{
        user = serverSocket.accept();
        inputReader = new InputStreamReader(user.getInputStream());
        buffReader = new BufferedReader(inputReader);
        printWriter = new PrintWriter(user.getOutputStream());
        System.out.println("Client Connected" + user.getLocalAddress() +"\n" + user.getInetAddress());
    }



    public void connectToServer()throws IOException{

        if(!server) {
            try {
                user = new Socket("localhost", 4499);
                inputReader = new InputStreamReader(user.getInputStream());
                buffReader = new BufferedReader(inputReader);
                printWriter = new PrintWriter(user.getOutputStream());
                connected = true;
                System.out.println("Connected to server");

            } catch (ConnectException e) {
                e.printStackTrace();
                System.out.println("connection refused");
            }

        }
    }

    public void sendMessage(String message){
        if(user.isConnected()) {
            printWriter.println(message);
            printWriter.flush();
        }
    }


    public String reciveMessage()throws IOException{
        String message = "";

        try {
            message = buffReader.readLine();
        } catch (ConnectException e) {
            e.printStackTrace();
            message = "Connection Error";
            //TODO lägg till kontroll av anslutning om inget meddelande kan tas emot
        }

        if(message.equalsIgnoreCase("Disconnect")){
            closeConnection();
            System.out.println("Client disconnected");
            message = "";
            newServerConnection();

        }
        return message;
    }

    public void closeConnection() throws IOException{
        try {
            user.close();
            buffReader.close();
            inputReader.close();
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public boolean isConnected(){
        return this.connected;
    }
    public boolean isServer(){
        return this.server;
    }
}
