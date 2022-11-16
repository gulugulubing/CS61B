import static org.junit.Assert.*;

import org.junit.Test;

public class HorribleSteveTest {
    @Test
    public void testisSameNumber() {
        assertTrue(Flik.isSameNumber(126, 126));
        assertTrue(Flik.isSameNumber(127, 127));
        assertTrue(Flik.isSameNumber(128, 128));
    }
}
