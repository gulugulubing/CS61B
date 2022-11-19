public class Palindrome {

    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public static boolean isPalindrome(String word) {
        Deque d = Palindrome.wordToDeque(word);
        return isEndsEqual(d);
    }

    private static boolean isEndsEqual(Deque d) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        if ((d.removeFirst() == d.removeLast()) && isEndsEqual(d)) {
            return true;
        } else {
            return false;
        }
    }

    /** The method will return true according to the character comparison test*/
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque d = Palindrome.wordToDeque(word);
        return isEndsEqual(d, cc);
    }

    private static boolean isEndsEqual(Deque d, CharacterComparator cc) {
        if (d.size() == 0 || d.size() == 1) {
            return true;
        }
        if (cc.equalChars((Character) d.removeFirst(), (Character) d.removeLast()) && isEndsEqual(d, cc)) {
            return true;
        } else {
            return false;
        }
    }
}
