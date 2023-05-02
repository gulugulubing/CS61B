public class OffByN implements CharacterComparator {
    private int diff;
    public OffByN(int N) {
        diff = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        if ((x - y == diff) || (x - y == -diff)) {
            return true;
        }
        return false;
    }
}
