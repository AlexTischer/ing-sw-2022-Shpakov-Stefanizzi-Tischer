package it.polimi.ingsw.server.model;

import java.io.Serializable;

public class Assistant implements Serializable {
    private int rank;
    private int movements;
    private AssistantType type;
    private String path;

    public Assistant(int rank, int movements, AssistantType type) {
        if(rank <= 0 || rank > 10)
            throw new IllegalArgumentException("Error: Rank can`t be negative or greater than 10");

        if(movements <= 0 || movements > 5)
            throw new IllegalArgumentException("Error: Movements can`t be negative or greater than 5");

        this.rank = rank;
        this.movements = movements;
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public int getMovements() {
        return movements;
    }

    public AssistantType getType() {
        return type;
    }
}
