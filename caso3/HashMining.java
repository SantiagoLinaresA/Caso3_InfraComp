import java.util.Scanner;
import java.awt.Toolkit;

public class HashMining {
    
    static Thread thread2;
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

            System.out.println("Comenzando la minería de hash...");
            MineHash(algorithm, data, leadingZeros, numThreads);
        }
    }

    public static void MineHash(String algorithm, String data, int leadingZeros, int numThreads) {
        long startTime = System.currentTimeMillis();
        
        MineHashTask task1 = new MineHashTask(algorithm, data, leadingZeros, 1);
        Thread thread1 = new Thread(task1);
        thread1.start();
        
        if (numThreads == 2) {
            MineHashTask task2 = new MineHashTask(algorithm, data, leadingZeros, 2);
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

        if (!MineHashTask.getSolutionFound().get()) {
            System.out.println("No se encontró una solución en todo el espacio de búsqueda.");
        } else {
            System.out.println("Tiempo transcurrido: " + elapsedTime + " ms");
        }
        Toolkit.getDefaultToolkit().beep();
    }
}