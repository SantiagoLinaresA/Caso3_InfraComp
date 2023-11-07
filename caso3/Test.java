import java.util.Arrays;
import java.awt.Toolkit;

public class Test {
    public static void main(String[] args) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= 7; i++) {
            generateStrings(alphabet, i, "");
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
    }

    public static void generateStrings(String alphabet, int length, String current) {
        if (current.length() == length) {
        } else {
            for (char c = 'a'; c <= 'z'; c++) {
                generateStrings(alphabet, length, current+c);
            }
        }
    }
}
