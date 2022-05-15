package client;

import client.controller.ClientController;
import client.model.ClientGameBoard;

import java.io.IOException;
import java.net.Socket;

public class Client{

    private Socket socket;
    private ClientGameBoard gameBoard;
    private String ip;
    private int serverPort;

    private ClientController controller;

    public Client(String ip, int port){
        this.ip = ip;
        this.serverPort = port;

        //create view, model, controller since when client receives "config"
        //it already needs to use those components to show strings to the view
        View view = new View();
        controller = new ClientController();
        //model is`not initialized by it will be enough before the start of the game
        ClientGameBoard model = new ClientGameBoard();

        view.attachController(controller);
        controller.attachModel(model);
        controller.attachView(view);
        model.attachView(view);
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, serverPort);

        System.out.println("Connection established with server: " + socket.getRemoteSocketAddress());

        ClientConnection connection = new ClientConnection(socket);
        controller.attachConnection(connection);
        connection.init();
    }
}
