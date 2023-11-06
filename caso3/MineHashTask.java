import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

class MineHashTask extends Thread {
    private String algorithm;
    private String data;
    private int leadingZeros;
    private int threadId;
    static AtomicBoolean solutionFound = new AtomicBoolean(false);

    public static AtomicBoolean getSolutionFound() {
        return solutionFound;
    }

    public MineHashTask(String algorithm, String data, int leadingZeros, int threadId) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String prefix = "0".repeat(leadingZeros);

            if (threadId == 2) {
                int i = 7;
                while(!solutionFound.get() && i > 0){
                    generateHashes(md, prefix, data, i);
                    i--;
                }
            }
            else{
                int i = 1;
                while (!solutionFound.get() && i <= 7) {
                    generateHashes(md, prefix, data, i);
                    i++;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void generateHashes(MessageDigest md, String prefix, String current, int length) {
        if (solutionFound.get()) {
            return;
        }
        
        int dist = 26/length;
        char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        
        char[] rta = new char[length];
        for (int i = 0; i < length; i++) {
            rta[i] = letters[i*dist];
        }

        String startingV = new String(rta);
        
        boolean done = false;
        while (!solutionFound.get() && !done) {
            int i = length-1;
            boolean found = false;
            while(i >= 0 && !found){
                if (rta[i] != letters[letters.length-1]) {
                    rta[i] = letters[Arrays.binarySearch(letters, rta[i])+1];
                    found = true;
                } else {
                    rta[i] = letters[0];
                }
                i--;
            }
            String rtaStr = new String(rta);
            String valueToHash = current + rtaStr;
            byte[] hashBytes = md.digest(valueToHash.getBytes());
            String hash = bytesToBinary(hashBytes);
    
            if (hash.startsWith(prefix)) {
                solutionFound.set(true);
                System.out.println("Thread " + threadId + " encontró una solución: " + rtaStr + " -> " + hash);
            }
            done = startingV.equals(new String(rta));
        }
    }


    private String bytesToBinary(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                result.append((b >> i) & 1);
            }
        }
        return result.toString();
    }

}
