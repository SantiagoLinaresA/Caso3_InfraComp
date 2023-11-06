import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class HashMining {
    private static Thread thread2;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Bienvenido al programa de minería de hash.");
            System.out.print("Algoritmo (SHA-256 o SHA-512): ");
            String algorithm = scanner.nextLine();
            System.out.print("Cadena C: ");
            String data = scanner.nextLine();
            System.out.print("Cantidad de ceros iniciales requeridos (20, 24, 28, 32 o 36): ");
            int leadingZeros = scanner.nextInt();
            System.out.print("Número de hilos (1 o 2): ");
            int numThreads = scanner.nextInt();

            if (numThreads < 1 || numThreads > 2) {
                System.out.println("Número de hilos no válido. Debe ser 1 o 2.");
                return;
            }

            AtomicBoolean solutionFound = new AtomicBoolean(false);

            System.out.println("Comenzando la minería de hash...");
            MineHash(algorithm, data, leadingZeros, numThreads, solutionFound);
        }
    }

    public static void MineHash(String algorithm, String data, int leadingZeros, int numThreads, AtomicBoolean solutionFound) {
        long startTime = System.currentTimeMillis();

        MineHashTask task1 = new MineHashTask(algorithm, data, leadingZeros, 1, solutionFound);
        Thread thread1 = new Thread(task1);
        thread1.start();

        if (numThreads == 2) {
            MineHashTask task2 = new MineHashTask(algorithm, data, leadingZeros, 2, solutionFound);
            thread2 = new Thread(task2);
            thread2.start();
        }

        try {
            thread1.join();
            if (numThreads == 2) {
                thread2.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        if (!solutionFound.get()) {
            System.out.println("No se encontró una solución en todo el espacio de búsqueda.");
        } else {
            System.out.println("Tiempo transcurrido: " + elapsedTime + " ms");
        }
    }
}

class MineHashTask implements Runnable {
    private String algorithm;
    private String data;
    private int leadingZeros;
    private int threadId;
    private AtomicBoolean solutionFound;

    public MineHashTask(String algorithm, String data, int leadingZeros, int threadId, AtomicBoolean solutionFound) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.threadId = threadId;
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
            String valueToHash = current + new String(rta);
            byte[] hashBytes = md.digest(valueToHash.getBytes());
            String hash = bytesToHex(hashBytes);
    
            if (hash.startsWith(prefix)) {
                solutionFound.set(true);
                System.out.println("Thread " + threadId + " encontró una solución: " + valueToHash + " -> " + hash);
            }
            done = startingV.equals(new String(rta));
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
