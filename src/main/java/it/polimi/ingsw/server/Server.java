package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.GameReactivatedException;
import it.polimi.ingsw.exceptions.GameSuspendedException;
import it.polimi.ingsw.modelChange.*;
import it.polimi.ingsw.packets.Packet;
import it.polimi.ingsw.server.controller.CharacterDeck;
import it.polimi.ingsw.server.controller.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


/**
 * This class handles the clients' connections and reconnections.
 * It handles first game configuration.
 * It creates a new {@link Connection} for each client and attaches it a {@link VirtualView},
 * creating it if it's the first client's connection or using an already created one if it's a reconnection.
 * It handles the Lobby.
 * It creates {@link Game} and {@link it.polimi.ingsw.server.model.GameBoard} and initiates them when the Lobby is ready.
 * <ul> Contains:
 *      <li>{@link #port} is the port the Server will listen at</li>
 *      <li>{@link #serverSocket} is the socket the server will accept clients with</li>
 *      <li>{@link #waitingConnection} is a Map linking each connection with the name of its client</li>
 *      <li>{@link #virtualViews} is the list of {@link VirtualView} in game once game has started</li>
 *      <li>{@link #timer} is the Timer used in {@link #startTimer()} to wait for clients to reconnect</li>
 *      <li>{@link #secondsToWait} is the remaining time clients have to reconnect</li>
 *      <li>{@link #isTimerActive} is the boolean representing the status of {@link #timer}</li>
 *      <li>{@link #game} is the instance of {@link Game} handling the current match</li>
 *      <li>{@link #numOfPlayers} is the number of players current match is made for</li>
 *      <li>{@link #advancedSettings} is the boolean representing whether the current match is using advanced settings or not</li>
 *      <li>{@link #numOfConnections} is the number of connection the server has accepted</li>
 *      <li>{@link #gameReady} is a boolean representing whether the lobby is full and the game can be created (or if it already has been done)</li>
 * </ul>
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    private Map<String, Connection> waitingConnection = new HashMap<>();
    private static List<VirtualView> virtualViews = new ArrayList<VirtualView>();
    private static Timer timer;
    private static final int[] secondsToWait = {29};
    private static boolean isTimerActive = false;
    private Game game;
    private int numOfPlayers = 0;
    private boolean advancedSettings;
    private int numOfConnections = 0;
    private boolean gameReady = false;


    public Server(int port) throws IOException{
        this.port=port;
        serverSocket = new ServerSocket(port);
    }

    /**
     *This method is used to add a client to the Lobby or to reconnect it to an ongoing match
     * <ul>
     *     <li>if !{@link #gameReady} calls {@link #addToLobby(Connection, String)}</li>
     *     <li>if {@link #gameReady} checks if any {@link VirtualView} has its {@link Connection} inactive and, if so, checks
     *     if the name of the connecting client is the same as the {@link Player}'s one in the VirtualView</li>
     *     <li>if a matching {@link VirtualView} is found, it calls {@link VirtualView#attachConnection(Connection)}, {@link Connection#attachView(VirtualView)},
     *     {@link VirtualView#changePlayerStatus(boolean)}, {@link VirtualView#update(ModelChange)} and {@link #changeConnectionStatus(ModelChange)}</li>
     *     <li>if a matching {@link VirtualView} is not found, it sends an {@link EndOfGameChange} with null parameter in order to close the exceeding client, and calls {@link Connection#close()}</li>
     * </ul>
     * @param connection the {@link Connection} the client is asking to be added through
     * @param name of the player to add
     * @throws InterruptedException whenever the client cannot be added whether because its name is the same as one of the active {@link Player},
     * or because the game is already started and every player is active.
     */
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

    /**
     * Creates a new and updated {@link LobbyChange}
     * @return {@link LobbyChange} to be sent to clients containing the list of clients in lobby
     */
    public LobbyChange createLobbyChange(){
        return new LobbyChange(waitingConnection.keySet().stream().toList());
    }


    /**
     * This method is used to add a client to the Lobby
     * If Lobby is full, it turns {@link #gameReady} to true and calls {@link #createGame()}
     * @param connection the {@link Connection} the client is asking to be added through
     * @param name of the player to add
     * @throws InterruptedException whenever the client cannot be added whether because its name is already present in Lobby
     */
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

    /**
     * <ul>
     *     <li>Creates game controller {@link Game}</li>
     *     <li>Creates virtual views for each connection and attaches them to each other</li>
     *     <li>Attaches game to each virtual view</li>
     *     <li>Initializes the game by calling {@link Game#init(List, boolean, CharacterDeck)}</li>
     *     <li>Attaches corresponding player to each virtual view</li>
     *     <li>Launches the game in {@link Game#launchGame()}, adds virtual views as Observers to
     *     the GameBoard, </li>
     *     <li>Sends the start command to each client</li>
     *     <li>Sends {@link GameBoardChange} to each client</li>
     * </ul>
     * @throws InterruptedException
     */
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

    /**
     * Removes connection from {@link #waitingConnection}, sends {@link LobbyChange},
     * decreases {@link #numOfConnections} and sets {@link #gameReady} back to false
     *
     * @param connection the {@link Connection} of the disconnected client
     */
    public void removeFromLobby(Connection connection) {

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


    /**Sends {@link ConnectionStatusChange} to every active client to set the status of a player whose client just disconnected or reconnected
     *
     * @param playerConnectionStatus is the {@link ModelChange} that will set a player status on the clients
     */
    public void changeConnectionStatus(ModelChange playerConnectionStatus){
        for (VirtualView client : virtualViews) {
            //need to notify only active virtualViews
            if (client.isConnectionActive())
                client.update(playerConnectionStatus);
        }
    }


    /**
     * Starts a countDown within which disconnected clients can reconnect. Other clients will receive {@link ExceptionChange}
     * containing {@link GameSuspendedException} each second for {@link #secondsToWait} seconds
     */
    public static void startTimer(){
        timer = new Timer();
        isTimerActive = true;
        secondsToWait[0] = 29;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (secondsToWait[0] > 0) {
                    for(VirtualView view: virtualViews){
                        if (view.isConnectionActive()){
                            System.out.println("Server says: " + secondsToWait[0] + " remained!");
                            view.update(new ExceptionChange(
                                    new GameSuspendedException("Waiting for other clients to reconnect! " + secondsToWait[0] + " remained!")
                            ));
                        }
                    }
                    secondsToWait[0]--;
                }
                else {
                    isTimerActive = false;
                    timer.cancel();
                    timer.purge();
                }
            }
        }, 0, 1000);
    }

    /**
     *If {@link #isTimerActive()}, sends {@link ExceptionChange} containing {@link GameReactivatedException} to every active client,
     * calls {@link VirtualView#notifyAll()} to wake any Thread sleeping on it (the one created in {@link VirtualView#sendPacket(Packet)}) and
     * cancels, purges and sets {@link #isTimerActive} to false
     */
    public static void resetTimer(){
        if (isTimerActive){
            System.out.println("Timer was reset");
            for(VirtualView view: virtualViews){
                if (view.isConnectionActive()){
                    synchronized (view) {
                        view.update(new ExceptionChange(new GameReactivatedException("The game was reactivated!")));
                        view.notifyAll();
                    }
                }
            }
            timer.cancel();
            timer.purge();
            isTimerActive = false;
        }
    }

    public static boolean isTimerActive() {
        return isTimerActive;
    }

    public static int getRemainedTime(){
        return secondsToWait[0];
    }

    /**
     * This is the method that the {@link it.polimi.ingsw.ServerApp} main Thread will execute until the end of the game
     * <ul>
     *      <li>It accepts the clients requests on {@link #serverSocket} and creates client sockets, {@link ObjectInputStream} and {@link ObjectOutputStream} </li>
     *      <li>It sets socketTimeOut</li>
     *      <li>It lets the first client to configure the game (temporarily handling Ping-Pong messages)</li>
     *      <li>It increments {@link #numOfConnections}</li>
     *      <li>It runs a new {@link Connection} on a new thread</li>
     * </ul>
     */
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
