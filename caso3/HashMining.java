import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class HashMining {
    private static Thread thread2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al programa de minería de hash.");
        System.out.println("Por favor, ingrese los siguientes parámetros:");

        System.out.print("Algoritmo (por ejemplo, SHA-256): ");
        String algorithm = scanner.nextLine();

        System.out.print("Datos a hashear: ");
        String data = scanner.nextLine();

        System.out.print("Cantidad de ceros iniciales requeridos: ");
        int leadingZeros = scanner.nextInt();

        System.out.print("Número de hilos (1 o 2): ");
        int numThreads = scanner.nextInt();

        if (numThreads != 1 && numThreads != 2) {
            System.out.println("Número de hilos no válido. Debe ser 1 o 2.");
            return;
        }

        System.out.println("Comenzando la minería de hash...");
        MineHash(algorithm, data, leadingZeros, numThreads);
    }

    public static void MineHash(String algorithm, String data, int leadingZeros, int numThreads) {
        long startTime = System.currentTimeMillis();

        MineHashTask task1 = new MineHashTask(algorithm, data, leadingZeros, 1, numThreads);
        Thread thread1 = new Thread(task1);
        thread1.start();

        if (numThreads == 2) {
            MineHashTask task2 = new MineHashTask(algorithm, data, leadingZeros, 2, numThreads);
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

        System.out.println("Tiempo transcurrido: " + elapsedTime + " ms");
    }
}

class MineHashTask implements Runnable {
    private String algorithm;
    private String data;
    private int leadingZeros;
    private int threadId;
    private int numThreads;

    public MineHashTask(String algorithm, String data, int leadingZeros, int threadId, int numThreads) {
        this.algorithm = algorithm;
        this.data = data;
        this.leadingZeros = leadingZeros;
        this.threadId = threadId;
        this.numThreads = numThreads;
    }

    @Override
    public void run() {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String prefix = "0".repeat(leadingZeros);
            String result = mineHash(md, data, prefix);
            if (result != null) {
                System.out.println("Hilo " + threadId + " encontró un hash válido: " + result);
                
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String mineHash(MessageDigest md, String data, String prefix) {
        // Genera valores de hash y verifica si coincide
        for (int i = 0; true; i++) {
            String valueToHash = data + Integer.toString(i);
            byte[] hashBytes = md.digest(valueToHash.getBytes());
            String hash = bytesToHex(hashBytes);
            if (hash.startsWith(prefix)) {
                return hash;
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
