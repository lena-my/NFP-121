package allumettes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrategieRapideTest {
    @Test
    public void testToujoursPrendMax() {
        StrategieRapide strategie = new StrategieRapide();
        assertEquals(3, strategie.getPrise(new JeuSimple(13)));
        assertEquals(2, strategie.getPrise(new JeuSimple(2)));
        assertEquals(1, strategie.getPrise(new JeuSimple(1)));
    }
}