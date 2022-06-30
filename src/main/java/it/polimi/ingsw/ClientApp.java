package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {

        String ip = "127.0.0.1";
        int port = 46582;

        if(args.length>0) {
            ip = args[0];

            if (args.length>1) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Argument" + args[1] + " must be an integer.");
                    System.exit(1);
                }
            }
        }

        Client client = new Client(ip, port);

        try {
            client.run();
        }
        catch (IOException e){
            if(!client.getFirstConnection()){
                System.err.println("There is no server listening at " + ip + ":" + port);
                System.exit(1);
            }
            e.printStackTrace();
        }
    }
}