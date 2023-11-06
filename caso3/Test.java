import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        int len = 4;
        int dist = 27/4;
        char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char [] find = "hola".toCharArray();

        char[] rta = new char[len];
        for (int i = 0; i < len; i++) {
            rta[i] = letters[i*dist];
        }

        boolean done = false;
        while (!done) {
            if (Arrays.equals(rta, find)) {
                done = true;
            } else {
                boolean incremented = false;
                int i = len-1;
                boolean found = false;
                while(i >= 0 && !found){
                    if (rta[i] != letters[letters.length-1]) {
                        rta[i] = letters[Arrays.binarySearch(letters, rta[i])+1];
                        incremented = true;
                        found = true;
                    } else {
                        rta[i] = letters[0];
                    }
                    i--;
                }
                if (!incremented) {
                    done = true;
                }
            }
            System.out.println(new String(rta));
        }
    }

}
