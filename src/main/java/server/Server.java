package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private int numOfPlayers = 0;
    private boolean advancedSettings;

    private boolean gameReady = false;

    public synchronized void lobby(Connection connection, String name){
        waitingConnection.put(name, connection);

        if (waitingConnection.size() == numOfPlayers){
            /*create game and other classes*/

            gameReady = true;
        }
        else{
            try {
                this.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void deregisterConnection(Connection connection) {
        /*remove connection from waiting connections map*/
        for (String name: waitingConnection.keySet()){
            if (waitingConnection.get(name) == connection)
                waitingConnection.remove(name);
        }
    }

    public void run(){
        int connections = 0;

        System.out.println("Server listening on port: " + port);

        while(true){
            try {
                /*receive new connection request*/
                Socket socket = serverSocket.accept();
                System.out.println("Connection number: " + (connections+1));

                /*create connection for client*/
                Connection connection = new Connection(socket, this);

                if (connections == 0){
                    Scanner in = new Scanner(socket.getInputStream());
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    try {
                        /*allows client configurate the game settings since he is the first one*/
                        out.print("config");
                        numOfPlayers = Integer.parseInt(in.nextLine());
                        if ( numOfPlayers < 2 || numOfPlayers > 4)
                            throw new IllegalArgumentException();
                        advancedSettings = Boolean.parseBoolean(in.nextLine());

                        connections++;
                        new Thread(connection).run();
                    }
                    catch (NumberFormatException e){
                        out.print("Invalid parameter. Try again !");
                        /*disconnect client*/
                        //client may have sent wrong parameter if it was cracked
                        //the check is done also on the client side
                        connection.close();
                    }
                    catch(IllegalArgumentException e){
                        out.print("Invalid parameter. Try again !");
                        connection.close();
                    }

                }

            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    public boolean getGameReady(){
        return gameReady;
    }

}
