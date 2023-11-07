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
    private boolean multiThreaded;

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
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String prefix = "0".repeat(leadingZeros);

            for (int i = 1; i <= 7; i++) {
                generateHashes(md, prefix, "", i);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void generateHashes(MessageDigest md, String prefix, String current, int length) {
        if (solutionFound.get()) {
            return;
        }

        if (current.length() == length) {
            String valueToHash = data + current;
            byte[] hashBytes = md.digest(valueToHash.getBytes());
            String hash = bytesToBinary(hashBytes);
    
            if (hash.startsWith(prefix)) {
                solutionFound.set(true);
                System.out.println("Thread " + threadId + " encontró una solución: " + current + " -> " + hash);
            }
        } else {

            if (multiThreaded) {
                if (threadId == 1) {
                    if (current.length() == 3) {
                        return;
                    }
                } else {
                    if (current.length() == 4) {
                        return;
                    }
                }
            }
            for (char c = 'a'; c <= 'z'; c++) {
                generateHashes(md, prefix, current+c, length);
            }
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
