package com.example.sinkshipsgraphicsonly;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;


public class Connection { //Klass för att ansluta sig mellan två olika enheter.

    //properties
    protected boolean server;
    protected boolean connected;
    protected Socket user;
    private ServerSocket serverSocket;
    private InputStreamReader in;
    private BufferedReader br;
    private PrintWriter out;

    private BufferedWriter bw;

    private String name;


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
                serverSocket = new ServerSocket(49152);
                System.out.println("Server Running");
                user = serverSocket.accept();
                System.out.println("Client Connected");
                in = new InputStreamReader(user.getInputStream());
                br = new BufferedReader(in);
                bw = new BufferedWriter( new PrintWriter(user.getOutputStream()));

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
        bw = new BufferedWriter( new PrintWriter(user.getOutputStream()));
        System.out.println("Client Connected" + user.getLocalAddress() +"\n" + user.getInetAddress());
    }


    // ansluter till en host.
    //  koppplar reader och printer för kommunikation genom socket.
    public void connectToServer()throws IOException{

        if(!server) {//Används när det finns en risk när det kastas exceptions, så fångas det, d.vs om något går fel så vill
            //vill vi kasta det istället. Om det inte fångas så fånga felet.
            //Om det som står så i try inte funkar, så kör catch med, så fångas den typen av exeception. Vi vill inte att det ska krascha
            //utan att vi vill fånga det istället.
            try { //Om man får ett error i try som man skickar, så kan man catcha det och sedan skrivs koden ut i det catchblocket.
                //Ungefär som en if sats, skillnaden är att här testas/körs bägge (om try inte funkar).
                user = new Socket("localhost", 49152);
                in = new InputStreamReader(user.getInputStream());
                br = new BufferedReader(in);
                bw = new BufferedWriter( new PrintWriter(user.getOutputStream()));
                connected = true;
                System.out.println("Connected to server");


            } catch (ConnectException e) {//get.class för att få klassen.
                e.printStackTrace();
                System.out.println("connection refused");
            }

        }
    }
    // metod för att skicka meddelande till anslutningen
    public void sendMessage(String message) throws IOException {

        if(user.isConnected()) {
            bw.write(message);
            bw.newLine();
            bw.flush();
        }
    }


    // metod för att ta emot meddelande från anslutnigen
    public String reciveMessage()throws IOException{
        String message = "Client Disconnected";
        if(user.isConnected()) {
            try {
                message = br.readLine();
            } catch (ConnectException e) {
                System.out.println("Client disconnected");
                e.printStackTrace();
                message = "Connection Error";
                //TODO lägg till kontroll av anslutning om inget meddelande kan tas emot
            }
        }

        return message;
    }

    // metod för att stänga anslutningen
    public void closeConnection() throws IOException{
        try {
            user.close();
            if(server){
                serverSocket.close();
            }
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
