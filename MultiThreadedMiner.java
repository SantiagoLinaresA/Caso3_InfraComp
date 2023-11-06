import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiThreadedMiner extends Thread {
    private String algorithm;
    private String data;
    private int leadingZeros;
    private AtomicBoolean solutionFound;

    public MultiThreadedMiner(String algorithm, String data, int leadingZeros, AtomicBoolean solutionFound) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.solutionFound = solutionFound;
    }

    @Override
    public void run() {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String prefix = "0".repeat(leadingZeros);

            for (int i = 1; i <= 7; i++) {
                if (solutionFound.get()) {
                    return; // Solución encontrada por otro hilo
                }

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

        if (length == 0) {
            for (char c = 'a'; c <= 'z'; c++) {
                String valueToHash = data + current + c;  // Aquí se usa la variable data
                byte[] hashBytes = md.digest(valueToHash.getBytes());
                String hash = bytesToHex(hashBytes);

                if (hash.startsWith(prefix)) {
                    solutionFound.set(true);
                    System.out.println("Solución encontrada: " + valueToHash + " -> " + hash);
                }
            }
        } else {
            for (char c = 'a'; c <= 'z'; c++) {
                generateHashes(md, prefix, current + c, length - 1);
            }
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
