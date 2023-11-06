import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class HashMining {
    private static long startTime;

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

            if (numThreads == 1) {
                SingleThreadedMiner miner = new SingleThreadedMiner(algorithm, data, leadingZeros, solutionFound);
                miner.start();
                try {
                    miner.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (numThreads == 2) {
                MultiThreadedMiner miner1 = new MultiThreadedMiner(algorithm, data, leadingZeros, solutionFound);
                MultiThreadedMiner miner2 = new MultiThreadedMiner(algorithm, data, leadingZeros, solutionFound);

                miner1.start();
                miner2.start();

                try {
                    miner1.join();
                    miner2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
}
