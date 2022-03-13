public class Island {
    private int numOfRedStudents = 0;
    private int numOfBlueStudents = 0;
    private int numOfYellowStudents = 0;
    private int numOfGreenStudents = 0;
    private int numOfPinkStudents = 0;
    private int numOfIslands = 1;
    private int numOfTowers = 0;
    private TowerColor towersColor = TowerColor.NULL;

    public void addStudent(Color color) {
        switch (color) {
            case RED:
                numOfRedStudents++;
                break;
            case BLUE:
                numOfBlueStudents++;
                break;
            case YELLOW:
                numOfYellowStudents++;
                break;
            case GREEN:
                numOfGreenStudents++;
                break;
            case PINK:
                numOfPinkStudents++;
                break;
        }
    }

    public void setTowersColor(TowerColor towerColor){
        this.towersColor=towerColor;
    }

    public int getNumOfStudents(Color color) {
        switch (color) {
            case RED:
                return numOfRedStudents;
            case BLUE:
                return numOfBlueStudents;
            case YELLOW:
                return numOfYellowStudents;
            case GREEN:
                return numOfGreenStudents;
            case PINK:
                return numOfPinkStudents;
            default:
                return 0;
        }
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public TowerColor getTowersColor(){
        return towersColor;
    }

    public void createTower(TowerColor towerColor){
        if(numOfIslands==1 && numOfTowers==0){
            numOfTowers++;
            setTowersColor(towerColor);
        }
    }
}