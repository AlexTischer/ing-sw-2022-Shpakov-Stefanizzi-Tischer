public class IslandGroup {
    private int numOfRedStudents = 0;
    private int numOfBlueStudents = 0;
    private int numOfYellowStudents = 0;
    private int numOfGreenStudents = 0;
    private int numOfPinkStudents = 0;
    private Island[] islands;

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

    public void changeTowersColor(TowerColor towerColor){
        for (Island i : islands){
            i.setTowerColor(towerColor);
        }
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
        int n = 0;
        for (Island i : islands){
            if (i.getHasTower()) n++;
        }
        return n;
    }

    public TowerColor getTowersColor(){
        return islands[0].getTowerColor();
    }
}