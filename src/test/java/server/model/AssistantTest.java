package server.model;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import server.model.Assistant;
import server.model.AssistantType;

import static org.junit.jupiter.api.Assertions.*;

public class AssistantTest extends TestCase{

    @Test
    void IllegalRankTest(){
        assertThrows(IllegalArgumentException.class, () -> { new Assistant(0, 1, AssistantType.ONE); });
        assertThrows(IllegalArgumentException.class, () -> { new Assistant(11, 1, AssistantType.ONE); });
    }

    @Test
    void IllegalMovementsTest(){
        assertThrows(IllegalArgumentException.class, () -> { new Assistant(1, 0, AssistantType.ONE); });
        assertThrows(IllegalArgumentException.class, () -> { new Assistant(1, 6, AssistantType.ONE); });
    }

    @Test
    void getRankTest(){
        Assistant testAssistant = new Assistant(1, 1, AssistantType.ONE);

        assertEquals(1, testAssistant.getRank());
    }

    @Test
    void getMovementsTest(){
        Assistant testAssistant = new Assistant(1, 1, AssistantType.ONE);

        assertEquals(1, testAssistant.getMovements());
    }

    @Test
    void getTypeTest(){
        Assistant testAssistant = new Assistant(1, 1, AssistantType.ONE);

        assertEquals(AssistantType.ONE, testAssistant.getType());
    }
}
