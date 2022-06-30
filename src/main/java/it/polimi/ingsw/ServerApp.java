package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {

        int port = 46582;

        if(args.length>0){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        }

        try {
            Server server = new Server(port);
            server.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
