package controller;


/** adds 2 to maximum number of steps */
public class Character8 extends Character{

    @Override
    public boolean moveMotherNature(int steps){
        return (game.currentPlayer.getPlayedAssistant().getMovements()+2>=steps);
    }
}
