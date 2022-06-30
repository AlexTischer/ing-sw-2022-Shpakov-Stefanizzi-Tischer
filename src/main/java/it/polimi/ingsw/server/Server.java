package it.polimi.ingsw.server;

import it.polimi.ingsw.modelChange.*;
import it.polimi.ingsw.server.controller.CharacterDeck;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.Player;

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
    private List<VirtualView> virtualViews = new ArrayList<VirtualView>();
    private Game game;
    private int numOfPlayers = 0;
    private boolean advancedSettings;
    private int numOfConnections = 0;
    private boolean gameReady = false;

    public Server(int port) throws IOException{
        this.port=port;
        serverSocket = new ServerSocket(port);
    }


    //synchronized because no clients can be added simultaneously
    public synchronized void addClient(Connection connection, String name) throws InterruptedException {
        if(!gameReady) {
            addToLobby(connection, name);
            System.out.println("Client " + name + " added ");
        }
        else if(game.getGameBoard().isGameOn()){
            boolean found = false;
            //find a virtual view corresponding to disconnected client
            for(VirtualView v : virtualViews){
                if (v.getPlayer().getName().equals(name) && !v.isConnectionActive()){
                    found=true;

                    v.attachConnection(connection);
                    connection.attachView(v);
                    synchronized (game) {
                        v.changePlayerStatus(true);

                        //send GameBoardChange to the reconnected client
                        v.update(new GameBoardChange(game.getGameBoard(), game.getPlayers()));

                        //notify other clients that this player has been reconnected
                        //there is no problem if the same client receives connectionStatusChange containing it himself
                        changeConnectionStatus(new ConnectionStatusChange(v.getClientName(), true));
                    }
                    System.out.println("Client " + name + " has been reconnected!");
                }
            }
            //the game is already started and this player isn't the one that has been disconnected
            //decline him
            if(!found) {
                connection.send(new EndOfGameChange(null) );
                //set name to null in order to not send connectionStatusChange
                //neither removeFromLobby client that wasn't connected before
                connection.setName(null);
                connection.close();
            }
        }
    }

    public LobbyChange createLobbyChange(){
        return new LobbyChange(waitingConnection.keySet().stream().toList());
    }

    private void addToLobby(Connection connection, String name) throws InterruptedException{

        if (waitingConnection.keySet().contains(name)) {
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
                createGame();
            }
        }
    }

    private void createGame() throws InterruptedException{
        game = Game.getInstanceOfGame();

        virtualViews = new ArrayList<VirtualView>(waitingConnection.keySet().size());

        //attach connection and controller to each virtual view
        for(String name: waitingConnection.keySet()){
            VirtualView view = new VirtualView();

            view.attachConnection(waitingConnection.get(name));
            waitingConnection.get(name).attachView(view);
            view.attachGame(game);
            virtualViews.add(view);
        }

        game.init(waitingConnection.keySet().stream().toList(), advancedSettings, new CharacterDeck());
        //refill islands with 1 students each
        game.getGameBoard().refillIslands();

        //attach player to each virtual view
        for (Player p: game.getPlayers()){
            for (VirtualView view: virtualViews ){
                if (p.getName().equals(view.getClientName())){
                    view.attachPlayer(p);
                }
            }
        }

        synchronized (game) {
            //launches thread that will wait for client actions
            //e.g. thread will wait until all players insert assistant card
            game.launchGame();

            //waits until game thread comes in waiting for useAssistant command state
            game.wait();

            //add all virtual views as observers to gameBoard in order to send modelChange
            for (VirtualView client : virtualViews) {
                game.getGameBoard().addObserver(client);
            }

            //first send start and only then send gameBoardChange to all clients
            for (VirtualView client : virtualViews) {
                client.sendStart();
            }

            //send general modelChange to all clients
            game.getGameBoard().sendGameBoardChange();
        }
    }

    /*only for addToLobby*/
    public void removeFromLobby(Connection connection) {
        /*remove connection from waiting virtualViews map*/

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
        gameReady = false;
        System.out.println("Server: I deregistered connection");

    }

    /*receives ConnectionStatusChange of that particular client that became inactive or active*/
    public void changeConnectionStatus(ModelChange playerConnectionStatus){
        for (VirtualView client: virtualViews){
            //need to notify only active virtualViews
            if (client.isConnectionActive())
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
                //socket.setSoTimeout(10*1000);

                System.out.println("Connection number: " + (numOfConnections +1));

                if (numOfConnections == 0){

                    Object fromClient = new Object();

                    try {
                        //if it is not a string , ClassCastException gets raised
                        fromClient = socketIn.readObject();
                        //server sends config message only after it received ping from client
                        if (fromClient.equals("ping")) {
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
                System.out.println("Connection created");

            } catch (IOException e){
                System.err.println("Connection error!" + e);
            }
        }
    }

    public boolean isGameReady(){
        return gameReady;
    }

}
