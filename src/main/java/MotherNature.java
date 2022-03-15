public class MotherNature {
    private static MotherNature instanceOfMotherNature = null;
    private GameBoard gameBoardInstance = null;

    private MotherNature() {
    }

    public static MotherNature getInstanceOfMotherNature() {
        if (instanceOfMotherNature == null){
            instanceOfMotherNature = new MotherNature();
        }
        return instanceOfMotherNature;
    }

    public Team getConqueror(){
        Team conqueror = null;
        return conqueror;
    }
}
