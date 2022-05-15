package server;

import modelChange.LobbyChange;
import modelChange.ModelChange;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private List<Connection> playingConnections = new ArrayList<Connection>();
    private int numOfPlayers = 0;
    private boolean advancedSettings;

    private boolean gameReady = false;

    public Server(int port) throws IOException{
        serverSocket = new ServerSocket(port);
    }

    public void lobby(Connection connection, String name){

        if (waitingConnection.keySet().contains(name))
            throw new IllegalArgumentException();

        waitingConnection.put(name, connection);

        //notify other clients that list of clients in lobby has changed
        ModelChange lobbyChange = new LobbyChange(waitingConnection.keySet().stream().toList());
        for (String n: waitingConnection.keySet()){
            waitingConnection.get(n).send(lobbyChange);
        }

        if (waitingConnection.size() == numOfPlayers){
            /*create game and other classes*/

            gameReady = true;
        }
    }

    /*only for lobby*/
    public void deregisterConnectionFromLobby(Connection connection) {
        /*remove connection from waiting playingConnections map*/
        for (String name: waitingConnection.keySet()){
            if (waitingConnection.get(name) == connection)
                waitingConnection.remove(name);
        }

        for (String name: waitingConnection.keySet()){
            waitingConnection.get(name).send(new LobbyChange(waitingConnection.keySet().stream().toList()));
        }
    }

    /*receives ConnectionStatusChange of that particular client that became inactive or active*/
    public void changeConnectionStatus(ModelChange playerConnectionStatus){
        for (Connection c: playingConnections){
            c.send(playerConnectionStatus);
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
                        //equals true unless string is equal to "false"
                        advancedSettings = Boolean.parseBoolean(in.nextLine());
                    }
                    catch (NumberFormatException e){
                        out.print("Invalid parameter. Try again !");
                        /*disconnect client*/
                        //client may have sent wrong parameter if it was cracked
                        //the check is done also on the client side
                        connection.close("Connection closed from server side, malicious client\nInvalid parameter provided");
                    }
                    catch(IllegalArgumentException e){
                        out.print("Invalid parameter. Try again !\n"+e.toString());
                        connection.close("Connection closed from server side, malicious client\nInvalid parameter provided");
                    }

                }
                connections++;
                new Thread(connection).start();

            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    public boolean isGameReady(){
        return gameReady;
    }
}
