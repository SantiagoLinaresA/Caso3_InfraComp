import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;

class MineHashTask extends Thread {
    private String algorithm;
    private String data;
    private int leadingZeros;
    private int threadId;
    static AtomicBoolean solutionFound = new AtomicBoolean(false);
    private boolean multiThreaded;
    MessageDigest md;
    String prefix;

    public static AtomicBoolean getSolutionFound() {
        return solutionFound;
    }

    public MineHashTask(String algorithm, String data, int leadingZeros, int threadId, boolean multiThreaded) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.threadId = threadId;
        
    }

    @Override
    public void run() {
        try {
            md = MessageDigest.getInstance(algorithm);
            prefix = "0".repeat(leadingZeros);

            generateHashes("");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void generateHashes(String current) {
        if (solutionFound.get() || current.length() == 7) {
            return;
        }

        if (multiThreaded) {
            if (threadId == 1) {
                if (current.length()%2 == 0){
                    for (char c = 'a'; c <= 'm'; c++) {
                        checkHash(current+c);
                        generateHashes(current+c);
                        checkHash(current+c);
                        generateHashes(current+c);
                    }
                } else {
                    for (char c = 'n'; c <= 'z'; c++) {
                        checkHash(current+c);
                        generateHashes(current+c);
                    }
                }
            } else {
                if (current.length()%2 == 0){
                    for (char c = 'n'; c <= 'z'; c++) {
                        checkHash(current+c);
                        generateHashes(current+c);
                    }
                } else {
                    for (char c = 'a'; c <= 'm'; c++) {
                        checkHash(current+c);
                        generateHashes(current+c);
                    }
                }
            }
        }
        else{
            for (char c = 'n'; c <= 'z'; c++) {
                checkHash(current+c);
                generateHashes(current+c);
            }
        }
    }
    
    private void checkHash(String sal){
        String valueToHash = data + sal;
        byte[] hashBytes = md.digest(valueToHash.getBytes());
        String hash = bytesToBinary(hashBytes);

        if (hash.startsWith(prefix)) {
            solutionFound.set(true);
            System.out.println("Thread " + threadId + " encontró una solución: " + sal + " -> " + hash);
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
