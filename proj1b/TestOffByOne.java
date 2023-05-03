import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testOffByOne() {
        assertEquals(true, offByOne.equalChars('a', 'b'));
        assertEquals(true, offByOne.equalChars('r', 'q'));
        assertEquals(false, offByOne.equalChars('a', 'e'));
        assertEquals(false, offByOne.equalChars('z', 'a'));
        assertEquals(false, offByOne.equalChars('a', 'a'));
        assertEquals(false, offByOne.equalChars('a', 'A'));
        assertEquals(false, offByOne.equalChars('a', 'B'));
        assertEquals(true, offByOne.equalChars('&', '%'));
    }
}
