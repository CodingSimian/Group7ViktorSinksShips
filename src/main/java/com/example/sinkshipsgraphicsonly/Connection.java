package com.example.sinkshipsgraphicsonly;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;


public class Connection { //Klass för att ansluta sig mellan två olika enheter.

    //properties
    protected boolean server;
    protected boolean connected;
    protected Socket user;
    private ServerSocket serverSocket;
    private InputStreamReader in;
    private BufferedReader br;

    private BufferedWriter bw;
    private int connectionAttempts;

    protected boolean stopConnection;




    // konstruktor
    Connection(){
        server = false;
        connected = false;
        stopConnection = false;
    }


    // öppnar ny serverSocket och väntar på att ta emot on anslutning
    // skapar reader och printer för kommunikation genom socketen.
    public void newServer() throws IOException {
        Timer timer = new Timer();
        if(!connected) {
            try {
                serverSocket = new ServerSocket(49152);
                System.out.println("Server Running");
                server = true;
                if(!stopConnection) {
                    try {
                        user = serverSocket.accept();
                    }catch (SocketException e){}

                }
                System.out.println("Client Connected");
                in = new InputStreamReader(user.getInputStream());
                br = new BufferedReader(in);
                bw = new BufferedWriter( new PrintWriter(user.getOutputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }


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
    public void connectToServer() throws IOException, InterruptedException {
         int i = 0;
        if(!server) {//Används när det finns en risk när det kastas exceptions, så fångas det, d.vs om något går fel så vill
            //vill vi kasta det istället. Om det inte fångas så fånga felet.
            //Om det som står så i try inte funkar, så kör catch med, så fångas den typen av exeception. Vi vill inte att det ska krascha
            //utan att vi vill fånga det istället.
            try { //Om man får ett error i try som man skickar, så kan man catcha det och sedan skrivs koden ut i det catchblocket.
                //Ungefär som en if sats, skillnaden är att här testas/körs bägge (om try inte funkar).

                connectionAttempts ++;
                if(connectionAttempts == 0) {
                    System.out.println("Trying to connect to server");
                }else
                    System.out.println("Trying to connect to server attempt: " + connectionAttempts);

                    user = new Socket("localhost", 49152);
                    in = new InputStreamReader(user.getInputStream());
                    br = new BufferedReader(in);
                    bw = new BufferedWriter(new PrintWriter(user.getOutputStream()));
                    connected = true;
                    System.out.println("Connected to server");




            } catch (ConnectException e) {// fångar upp exception om det inte får kontakt med vald anslutningen, har en delay på 2sek
                Thread.sleep(2000);  // gör därefter ett nytt försök
                if(connectionAttempts > 4){ // Efter 5 mislyckade försök kastar den exception som fångas upp av gameThread som skickar tillbaka programmet till startmeny
                    throw new ConnectException();
                }else {
                    connectToServer();
                }
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
                message = br.readLine(); // lägg till så den avbryter efter en viss tid.
            } catch (SocketException e) {
                System.out.println("Client disconnected");


                //TODO lägg till kontroll av anslutning om inget meddelande kan tas emot
            }
        }

        return message;
    }

    // metod för att stänga anslutningen
    public void closeConnection() throws IOException{
        try {
            if (server) {
                serverSocket.close();
            } else {
                user.close();
            }

        }catch (IOException  | NullPointerException e ){
            Socket a = new Socket("localhost", 49152);
            closeConnection();
        }




    }

    public boolean isConnected(){
        return this.connected;
    }
    public boolean isServer(){
        return this.server;
    }

}
