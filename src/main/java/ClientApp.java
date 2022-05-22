import client.Client;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client("192.168.1.23", 46582);

        try {
            client.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
