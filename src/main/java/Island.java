public class Island {
    private boolean hasTower = false;
    private TowerColor towerColor = TowerColor.NULL;

    protected boolean getHasTower(){
        return hasTower;
    }
    protected TowerColor getTowerColor(){
        return towerColor;
    }
    protected void setTowerColor(TowerColor towerColor){
        if(hasTower){
            this.towerColor=towerColor;
        }
        else{
            this.hasTower=true;
            this.towerColor=towerColor;
        }
    }
}
