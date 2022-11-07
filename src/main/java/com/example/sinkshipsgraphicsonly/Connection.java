package com.example.sinkshipsgraphicsonly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;


public class Connection {

    //properties
    protected boolean server;
    protected boolean connected;
    protected Socket user;
    private ServerSocket serverSocket;
    private InputStreamReader in;
    private BufferedReader br;
    private PrintWriter out;


    // konstruktor
    Connection(){
        server = false;
        connected = false;
    }


    // öppnar ny serverSocket och väntar på att ta emot on anslutning
    // skapar reader och printer för kommunikation genom socketen.
    public void newServer() throws IOException {
        if(!connected) {
            try {
                serverSocket = new ServerSocket(4499);
                System.out.println("Server Running");
                user = serverSocket.accept();
                System.out.println("Client Connected");
                in = new InputStreamReader(user.getInputStream());
                br = new BufferedReader(in);
                out = new PrintWriter(user.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            server = true;

        }
    }
    // upprättar en anslutning till ny användare utan att skapa ny serverSocket.

    public void newServerConnection()throws IOException{
        user = serverSocket.accept();
        in = new InputStreamReader(user.getInputStream());
        br = new BufferedReader(in);
        out = new PrintWriter(user.getOutputStream());
        System.out.println("Client Connected" + user.getLocalAddress() +"\n" + user.getInetAddress());
    }


    // ansluter till en host.
    //  koppplar reader och printer för kommunikation genom socket.
    public void connectToServer()throws IOException{

        if(!server) {
            try {
                user = new Socket("localhost", 4499);
                in = new InputStreamReader(user.getInputStream());
                br = new BufferedReader(in);
                out = new PrintWriter(user.getOutputStream());
                connected = true;
                System.out.println("Connected to server");


            } catch (ConnectException e) {
                e.printStackTrace();
                System.out.println("connection refused");
            }

        }
    }
    // metod för att skicka meddelande till anslutningen
    public void sendMessage(String message){
        if(user.isConnected()) {
            out.println(message);
            out.flush();
        }
    }


    // metod för att ta emot meddelande från anslutnigen
    public String reciveMessage()throws IOException{
        String message = "";

        try {
            message = br.readLine();
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

    // metod för att stänga anslutningen
    public void closeConnection() throws IOException{
        try {
            user.close();
            br.close();
            in.close();
            out.close();
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
