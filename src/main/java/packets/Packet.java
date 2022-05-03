package packets;

import controller.GameForClient;

import java.util.List;

public abstract class Packet {

    private List<Object> parameters;
    private GameForClient game;

    public Packet(Object... parameters) {
        for (Object o : parameters)
            this.parameters.add(o);
    }

    public void setGame(GameForClient game){
        this.game = game;
    }

    public GameForClient getGame(){
        return game;
    }


    public List<Object> getParameters() {
        return parameters;
    }

    public void execute(){

    }
}
