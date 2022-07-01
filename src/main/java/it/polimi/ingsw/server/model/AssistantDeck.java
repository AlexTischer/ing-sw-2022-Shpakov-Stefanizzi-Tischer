package it.polimi.ingsw.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  AssistantDeck {
    private Map<AssistantType, ArrayList<Assistant>> assistants;

    public AssistantDeck(){
        assistants = new HashMap<AssistantType, ArrayList<Assistant>>();

        ArrayList<Assistant> assistants1 = new ArrayList<>();
        assistants1.add(new Assistant(1,1,AssistantType.ONE));
        assistants1.add(new Assistant(2,1,AssistantType.ONE));
        assistants1.add(new Assistant(3,2,AssistantType.ONE));
        assistants1.add(new Assistant(4,2,AssistantType.ONE));
        assistants1.add(new Assistant(5,3,AssistantType.ONE));
        assistants1.add(new Assistant(6,3,AssistantType.ONE));
        assistants1.add(new Assistant(7,4,AssistantType.ONE));
        assistants1.add(new Assistant(8,4,AssistantType.ONE));
        assistants1.add(new Assistant(9,5,AssistantType.ONE));
        assistants1.add(new Assistant(10,5,AssistantType.ONE));

        ArrayList<Assistant> assistants2 = new ArrayList<>();
        assistants2.add(new Assistant(1,1,AssistantType.TWO));
        assistants2.add(new Assistant(2,1,AssistantType.TWO));
        assistants2.add(new Assistant(3,2,AssistantType.TWO));
        assistants2.add(new Assistant(4,2,AssistantType.TWO));
        assistants2.add(new Assistant(5,3,AssistantType.TWO));
        assistants2.add(new Assistant(6,3,AssistantType.TWO));
        assistants2.add(new Assistant(7,4,AssistantType.TWO));
        assistants2.add(new Assistant(8,4,AssistantType.TWO));
        assistants2.add(new Assistant(9,5,AssistantType.TWO));
        assistants2.add(new Assistant(10,5,AssistantType.TWO));

        ArrayList<Assistant> assistants3 = new ArrayList<>();
        assistants3.add(new Assistant(1,1,AssistantType.THREE));
        assistants3.add(new Assistant(2,1,AssistantType.THREE));
        assistants3.add(new Assistant(3,2,AssistantType.THREE));
        assistants3.add(new Assistant(4,2,AssistantType.THREE));
        assistants3.add(new Assistant(5,3,AssistantType.THREE));
        assistants3.add(new Assistant(6,3,AssistantType.THREE));
        assistants3.add(new Assistant(7,4,AssistantType.THREE));
        assistants3.add(new Assistant(8,4,AssistantType.THREE));
        assistants3.add(new Assistant(9,5,AssistantType.THREE));
        assistants3.add(new Assistant(10,5,AssistantType.THREE));

        ArrayList<Assistant> assistants4 = new ArrayList<>();
        assistants4.add(new Assistant(1,1,AssistantType.FOUR));
        assistants4.add(new Assistant(2,1,AssistantType.FOUR));
        assistants4.add(new Assistant(3,2,AssistantType.FOUR));
        assistants4.add(new Assistant(4,2,AssistantType.FOUR));
        assistants4.add(new Assistant(5,3,AssistantType.FOUR));
        assistants4.add(new Assistant(6,3,AssistantType.FOUR));
        assistants4.add(new Assistant(7,4,AssistantType.FOUR));
        assistants4.add(new Assistant(8,4,AssistantType.FOUR));
        assistants4.add(new Assistant(9,5,AssistantType.FOUR));
        assistants4.add(new Assistant(10,5,AssistantType.FOUR));

        assistants.put(AssistantType.ONE,assistants1);
        assistants.put(AssistantType.TWO,assistants2);
        assistants.put(AssistantType.THREE,assistants3);
        assistants.put(AssistantType.FOUR,assistants4);


    }

    /*returns assistant card given its type, starting from the lowest rank*/
    public ArrayList<Assistant> popAssistants(AssistantType type){
        return assistants.get(type);
    }
}
