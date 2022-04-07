package model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class AssistantFactoryTest extends TestCase {

    @Test
    void getAssistantDeckTest(){
        AssistantDeck testdeck = new AssistantDeck();
        AssistantFactory testfactory = new AssistantFactory();

        testdeck = testfactory.getAssistantDeck();

        //testing first card of AssistantType=ONE
        Assistant testassistant;
        testassistant = testdeck.popAssistant(AssistantType.ONE);
        assertEquals(AssistantType.ONE, testassistant.getType());
        assertEquals(1,testassistant.getRank());
        assertEquals(1,testassistant.getMovements());

        //testing second card of AssistantType=ONE
        assertEquals(2, testdeck.popAssistant(AssistantType.ONE).getRank());

        for(int i=0;i<7;i++){
            testdeck.popAssistant(AssistantType.ONE);
        }
        //testing last card of AssistantType=ONE
        assertEquals(10, testdeck.popAssistant(AssistantType.ONE).getRank());

        //testing first card of AssistantType=TWO;
        testassistant = testdeck.popAssistant(AssistantType.TWO);
        assertEquals(AssistantType.TWO, testassistant.getType());
        assertEquals(1,testassistant.getRank());
        assertEquals(1,testassistant.getMovements());

        //testing third card of AssistantType=THREE
        testassistant = testdeck.popAssistant(AssistantType.THREE);
        testassistant = testdeck.popAssistant(AssistantType.THREE);
        testassistant = testdeck.popAssistant(AssistantType.THREE);
        assertEquals(AssistantType.THREE, testassistant.getType());
        assertEquals(3, testassistant.getRank());
        assertEquals(2, testassistant.getMovements());

    }
}
