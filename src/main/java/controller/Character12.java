package controller;

/** calculate influence without moving mother nature*/
public class Character12 extends Character{
    protected int selectedIslandNumber;

    @Override
    public void setSelectedIslandNumber(int selectedIslandNumber) {
        this.selectedIslandNumber = selectedIslandNumber;
    }

    public void execute(){
        game.calculateInfluence(selectedIslandNumber);
    }
}
