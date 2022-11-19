public class Palindrome {

    public  Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public  boolean isPalindrome(String word) {
        Deque d = wordToDeque(word);
        return isEndsEqual(d);
    }

    private  boolean isEndsEqual(Deque d) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        if (d.removeFirst() == d.removeLast()) {
            return isEndsEqual(d);
        } else {
            return false;
        }
    }

    /** The method will return true according to the character comparison test*/
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque d = wordToDeque(word);
        return isEndsEqual(d, cc);
    }

    private boolean isEndsEqual(Deque d, CharacterComparator cc) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        char first = (char) d.removeFirst();
        char last = (char) d.removeLast();
        if ((first > 96 && last < 91) || (last > 96 && first < 91)) {
            return false;
        }
        if (cc.equalChars(first, last)) {
            return isEndsEqual(d, cc);
        } else {
            return false;
        }
    }
}
