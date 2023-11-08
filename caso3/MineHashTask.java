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

    public static AtomicBoolean getSolutionFound() {
        return solutionFound;
    }

    public MineHashTask(String algorithm, String data, int leadingZeros, int threadId, boolean multiThreaded) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.threadId = threadId;
        this.multiThreaded = multiThreaded;
    }

    @Override
    public void run() {
        try {
            md = MessageDigest.getInstance(algorithm);
            generateHashes("");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void generateHashes(String current) {
        if (solutionFound.get() || current.length() == 7) {
            return;
        }

        if (multiThreaded && current.length() == 0) {
            if (threadId == 1) {
                for (char c = 'a'; c <= 'm'; c++) {
                    checkHash(current+c);
                    generateHashes(current+c);
                }
            } else {
                for (char c = 'n'; c <= 'z'; c++) {
                    checkHash(current+c);
                    generateHashes(current+c);
                }
            }
        }
        else{
            for (char c = 'a'; c <= 'z'; c++) {
                checkHash(current+c);
                generateHashes(current+c);
            }
        }
    }
    
    private void checkHash(String sal){
        String valueToHash = data + sal;
        byte[] hashBytes = md.digest(valueToHash.getBytes());

        if (countZeros(hashBytes)) {
            solutionFound.set(true);
            System.out.println("Thread " + threadId + " encontró una solución: " + sal);
        }
    }

    public boolean countZeros(byte[] hashCode){
        int index = 0;
        int upperBound = -1;
        boolean valid = true;
        boolean multiple = (this.leadingZeros%8==0);

        if(multiple){
            upperBound = (this.leadingZeros/8)-1;
        }
        else{
            upperBound = (this.leadingZeros/8);
        }

        while(index <= upperBound && valid){
            if(index == upperBound && !multiple){
                valid = (String.format("%8s", Integer.toBinaryString(hashCode[index] & 0xFF)).replace(' ', '0')).startsWith("0000");
                index++;
            }
            else{
                valid = (hashCode[index] == (byte)0);
                index++;
            }
        }
        return valid;
    }
}
