import server.Server;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new Server(46582);
            server.run();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}