import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mjasano.myquestplugin.Quest;
import org.junit.jupiter.api.Test;

public class QuestTest {

    @Test
    public void testGetId() {
        Quest quest = new Quest(1, "Test Quest", "This is a test quest", "Gold, XP", "Kill 5 rats");
        assertEquals(1, quest.getId());
    }

    @Test
    public void testGetName() {
        Quest quest = new Quest(1, "Test Quest", "This is a test quest", "Gold, XP", "Kill 5 rats");
        assertEquals("Test Quest", quest.getName());
    }

    @Test
    public void testGetDescription() {
        Quest quest = new Quest(1, "Test Quest", "This is a test quest", "Gold, XP", "Kill 5 rats");
        assertEquals("This is a test quest", quest.getDescription());
    }

    @Test
    public void testGetRewards() {
        Quest quest = new Quest(1, "Test Quest", "This is a test quest", "Gold, XP", "Kill 5 rats");
        assertEquals("Gold, XP", quest.getRewards());
    }

    @Test
    public void testGetRequirements() {
        Quest quest = new Quest(1, "Test Quest", "This is a test quest", "Gold, XP", "Kill 5 rats");
        assertEquals("Kill 5 rats", quest.getRequirements());
    }
}
