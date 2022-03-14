import java.util.ArrayList;

public class Team {
    private ArrayList<Player> players = new ArrayList<Player>(1);
    private int nextPlayer;
    private TowerColor towerColor;

    public Team(ArrayList<String> names, TowerColor towerColor){
        this.towerColor=towerColor;
    }
    public void playTurn(){
    }
}