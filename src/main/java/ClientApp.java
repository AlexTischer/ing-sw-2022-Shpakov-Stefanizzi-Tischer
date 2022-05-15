import client.Client;

import java.io.IOException;

public class ClientApp {

    public static void main(String[] args) {
        Client client = new Client("hostname", 0000);

        try {
            client.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
