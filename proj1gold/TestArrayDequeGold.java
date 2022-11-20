import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDequeGold {
    static StudentArrayDeque<Integer> stu = new StudentArrayDeque<>();
    static ArrayDequeSolution<Integer> sol = new ArrayDequeSolution<>();

    /**
     * test add,get,and size changed by add
     */
    @Test
    public void testGetAndAdd() {
        for (int i = 0; i < 100; i++) {
            Integer randomIndex;
            double random01 = StdRandom.uniform();
            if (random01 < 0.5) {
                stu.addFirst(i);
                sol.addFirst(i);
                assertEquals(sol.get(0), stu.get(0));
            } else {
                stu.addLast(i);
                sol.addLast(i);
                assertEquals(sol.get(sol.size() - 1), stu.get(stu.size() - 1));
            }
            randomIndex = StdRandom.uniform(sol.size());
            assertEquals(sol.get(randomIndex), stu.get(randomIndex));
            assertEquals(sol.size(), stu.size());
        }
    }

    @Test
    public void testRemove() {
        String message = "";
        double randomSel;
        for (int i = 0; i < 10; i++) {
            randomSel = StdRandom.uniform();
            if (randomSel < 0.5) {
                stu.addFirst(i);
                sol.addFirst(i);
                message += "addFirst(" + i + ")\n";
            } else {
                stu.addLast(i);
                sol.addLast(i);
                message += "addLast(" + i + ")\n";
            }
        }

        for (int i = 0; i < 10; i++) {
            assertEquals(sol.size(), stu.size());
            double random01 = StdRandom.uniform();
            if (random01 < 0.5) {
                Integer solFirst = sol.removeFirst();
                Integer stuFirst = stu.removeFirst();
                message += "removeFirst()\n";
                assertEquals(message, solFirst, stuFirst);
            } else {
                Integer solLast = sol.removeLast();
                Integer stuLast = stu.removeLast();
                message += "removeLast()\n";
                assertEquals(message, solLast, stuLast);
            }
        }
    }
}