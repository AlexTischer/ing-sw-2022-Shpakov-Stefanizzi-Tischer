package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {


        Client client = new Client("127.0.0.1", 46582);

        try {
            client.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}