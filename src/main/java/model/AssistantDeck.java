package model;

import java.util.*;

public class  AssistantDeck {
    private Map<AssistantType, ArrayList<Assistant>> assistants;

    public AssistantDeck(){
        assistants = new HashMap<AssistantType, ArrayList<Assistant>>();
    }

    /*returns assistant card given its type, starting from the lowest rank*/
    public ArrayList<Assistant> popAssistants(AssistantType type){
        return assistants.get(type);
    }
}
