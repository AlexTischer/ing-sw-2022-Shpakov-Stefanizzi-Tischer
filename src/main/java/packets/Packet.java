package packets;

import server.controller.GameForClient;

import java.util.List;

public abstract class Packet {

    private List<Object> parameters;

    public Packet(Object... parameters) {
        for (Object o : parameters)
            this.parameters.add(o);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void execute(GameForClient game){}
}
