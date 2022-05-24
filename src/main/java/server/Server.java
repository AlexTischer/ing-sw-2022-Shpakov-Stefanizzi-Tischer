package server;

import modelChange.LobbyChange;
import modelChange.ModelChange;
import server.controller.CharacterDeck;
import server.controller.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private List<VirtualView> playingClients = new ArrayList<VirtualView>();
    private int numOfPlayers = 0;
    private boolean advancedSettings;
    private int numOfConnections = 0;
    private boolean gameReady = false;
    //public Connection currentConnection;

    public Server(int port) throws IOException{
        this.port=port;
        serverSocket = new ServerSocket(port);
    }

    public void addToLobby(Connection connection, String name){
//        System.out.println("Server says: before if: " + waitingConnection.keySet().stream().toList());
        if (waitingConnection.keySet().stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(Collectors.toList()).contains(name.toLowerCase(Locale.ROOT))) {
            System.out.println("Server says: name already used");
            throw new IllegalArgumentException();
        }
        else {
            waitingConnection.put(name, connection);

            //notify other clients that list of clients in addToLobby has changed
            ModelChange lobbyChange = new LobbyChange(waitingConnection.keySet().stream().toList());
            for (String n : waitingConnection.keySet()) {
                waitingConnection.get(n).send(lobbyChange);
            }

            if (waitingConnection.size() == numOfPlayers) {
                gameReady = true;
                //createGame();
            }
        }
//        System.out.println("Server says: after else:" + waitingConnection.keySet().stream().toList());
    }

    private void createGame(){
        //TODO debug because there is an error
        Game game = Game.getInstanceOfGame();

        playingClients = new ArrayList<VirtualView>(waitingConnection.keySet().size());

        //attach connection and controller to each virtual view
        for(String name: waitingConnection.keySet()){
            VirtualView client = new VirtualView();

            client.attachConnection(waitingConnection.get(name));
            client.attachGame(game);
            playingClients.add(client);
        }

        //init only after i have connected virtual views to game in order to send gameboard change
        //initializes controller and automatically launches thread
        //that will wait in game phases until the end of player move
        //e.g. thread will wait until all players insert assistant card
        game.init(waitingConnection.keySet().stream().toList(), advancedSettings, new CharacterDeck());

        //add all virtual views as observers to gameBoard in order to send modelChange
        for(VirtualView client: playingClients){
            game.getGameBoard().addObserver(client);
        }

        //send modelChange to all clients
        game.getGameBoard().sendGameBoardChange();
    }

    /*only for addToLobby*/
    public void deregisterConnectionFromLobby(Connection connection) {
        /*remove connection from waiting playingClients map*/

        //save oldKeys because map may be modified during execution
        Set<String> oldKeys = new TreeSet<String>(waitingConnection.keySet());

        for (String name: oldKeys){
            if (waitingConnection.get(name) == connection) {
                waitingConnection.remove(name);
                System.out.println("Server: I deregistered connection named:" + name);
            }
        }

        ModelChange lobbyChange = new LobbyChange(waitingConnection.keySet().stream().toList());

        for (String name: waitingConnection.keySet()){
            waitingConnection.get(name).send(lobbyChange);
        }

        numOfConnections--;
        System.out.println("Server: I deregistered connection");

    }

    /*receives ConnectionStatusChange of that particular client that became inactive or active*/
    public void changeConnectionStatus(ModelChange playerConnectionStatus){
        for (VirtualView client: playingClients){
            client.update(playerConnectionStatus);
        }
    }

    public void run(){
        System.out.println("Server listening on port: " + port);
        //server sends strings to each client with writeUTF() until it gets added to lobby
        //then it sends objects with writeObject() in connection class

        //continue to accept clients until there is space for them in the game
        while(true){
            try {
                /*receive new connection request*/
                Socket socket = serverSocket.accept();
                ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
                socketOut.flush();
                ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

                //if server doesn't receive any message from client in 10 sec, then socket gets closed
                socket.setSoTimeout(10*1000);

                System.out.println("Connection number: " + (numOfConnections +1));

                if (numOfConnections == 0){

                    Object fromClient = new Object();

                    try {
                        //if it is not a string , ClassCastException gets raised
                        fromClient = socketIn.readObject();
                        //server sends config message only after it received ping from client
                        if (fromClient.equals("ping")) {
                            System.out.println("Server received ping from first client");
                            //client sent ping message, server responds with pong
                            socketOut.writeUTF("pong");
                            socketOut.flush();
                            socketOut.reset();
                        }
                    }
                    catch(ClassNotFoundException | ClassCastException e){
                        System.out.println("Error received from first client");
                    }

                    /*allows client configurate the game settings since he is the first one*/
                    socketOut.writeUTF("config");
                    socketOut.flush();
                    socketOut.reset();

                    boolean numOfPlayersCorrect = false;

                    while (!numOfPlayersCorrect) {
                        try {
                            //exits while only after executing try correctly
                            try {
                                fromClient = socketIn.readObject();
                                //server waits an integer from client and not string
                                //if the string was sent, then ClassCastException is raised
                                numOfPlayers = (int) fromClient;
                                System.out.println("Server received from client: " + numOfPlayers);

                                if (numOfPlayers < 2 || numOfPlayers > 4)
                                    throw new IllegalArgumentException();

                                numOfPlayersCorrect = true;
                            } catch (ClassNotFoundException | ClassCastException e) {
                                try {
                                    //if it is not a string , ClassCastException gets raised
                                    if (fromClient.equals("ping")) {
                                        //client sent ping message, server responds with pong
                                        socketOut.writeUTF("pong");
                                        socketOut.flush();
                                        socketOut.reset();
                                    }
                                    else {
                                        //throw exception asking client to insert again
                                        throw new IllegalArgumentException();
                                    }
                                } catch (ClassCastException e2) {
                                    throw new IllegalArgumentException();
                                }
                            }
                        }
                        catch (IllegalArgumentException e) {
                            socketOut.writeUTF("Incorrect number of players value. Please try again");
                            socketOut.flush();
                            socketOut.reset();
                            //the check is done also on the client side
                        }
                    }

                    socketOut.writeUTF("ok");
                    socketOut.flush();
                    socketOut.reset();

                    //advanced settings equals true if the string is equal to "true" and false if string is equal to "false"
                    boolean settingsCorrect = false;

                    while (!settingsCorrect){
                        try{
                            fromClient = socketIn.readObject();
                            if ( fromClient.equals("true") ){
                                advancedSettings = true;
                                settingsCorrect = true;
                            }
                            else if (fromClient.equals("false") ){
                                advancedSettings = false;
                                settingsCorrect = true;
                            }
                            else if ( fromClient.equals("ping") ){
                                //client sent ping message, server responds with pong
                                socketOut.writeUTF("pong");
                                socketOut.flush();
                                socketOut.reset();
                            }
                            else{
                                throw new IllegalArgumentException();
                            }
                        }
                        catch (ClassNotFoundException | ClassCastException | IllegalArgumentException e){
                            socketOut.writeUTF("Incorrect advanced settings value. Please try again");
                            socketOut.flush();
                            socketOut.reset();
                            //the check is done also on the client side
                        }
                    }

                    System.out.println("Server received from first client advanced settings: " + advancedSettings);
                    socketOut.writeUTF("ok");
                    socketOut.flush();
                    socketOut.reset();
                }

                numOfConnections++;
                new Thread(new Connection(socket, socketIn, socketOut, this)).start();
                System.out.println("Server created");

            } catch (IOException e){
                System.err.println("Connection error!" + e);
            }
        }
    }

    public boolean isGameReady(){
        return gameReady;
    }
}
