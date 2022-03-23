import java.util.*;
import java.lang.Math;

public class AssistantDeck {
    private Map<AssistantType, List<Assistant>> assistants;

    public AssistantDeck() {
        assistants = new HashMap<AssistantType, List<Assistant>>();

        /*adds 10 Assistant cards per AssistantType inside assistants Map*/
        for(AssistantType type: AssistantType.values()){

            assistants.put(type, new ArrayList<Assistant>(10));

            for ( int rank = 1 ; rank <= 10; rank++){
               assistants.get(type).add(rank-1, new Assistant(rank, (rank+1)/2, type));
            }

        }
    }

    /*returns assistant card given its type, starting from the lowest rank*/
    public Assistant popAssistant(AssistantType type){
        return assistants.get(type).remove(0);
    }


}
