import java.util.ArrayList;

public class Player {
    private String name;
    private SchoolBoard schoolBoard;
    private ArrayList<Assistant> assistants = new ArrayList<Assistant>(0);
    private ArrayList<Coin> coins = new ArrayList<Coin>(0);

    protected void playTurn(){
    }

    protected Player(String name){
        this.name=name;
    }
    protected String getName(){
        return name;
    }

}
