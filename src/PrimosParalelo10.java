import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementação Paralela com 10 threads.
 */
public class PrimosParalelo10 {
    
    // Constante para definir o número de threads.
    private static final int NUM_THREADS = 10;

    public static void main(String[] args) {
        // --- CONFIGURAÇÃO ---
        String inputFile = "input/Entrada01.txt";
        String outputFile = "output/primos_paralelo10.txt";
        String resultsFile = "results/tempos_execucao.csv";

        System.out.println("Iniciando processamento paralelo com " + NUM_THREADS + " threads...");
        
        long startTime = System.nanoTime();

        try {
            // --- 1. LEITURA DO ARQUIVO ---
            Path inputPath = Paths.get(inputFile);
            List<Long> allNumbers = Files.lines(inputPath)
                                         .map(Long::parseLong)
                                         .collect(Collectors.toList());

            // --- 2. PREPARAR RECURSOS COMPARTILHADOS ---
            // Usamos um Map sincronizado para que as threads possam inserir resultados
            // concorrentemente sem corromper a estrutura de dados.
            Map<Integer, Long> primeResults = Collections.synchronizedMap(new HashMap<>());
            // Contador atômico para distribuir o trabalho.
            AtomicInteger nextNumberIndex = new AtomicInteger(0);

            // --- 3. CRIAR E INICIAR AS THREADS ---
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < NUM_THREADS; i++) {
                // Criamos uma instância do nosso Worker.
                Runnable worker = new PrimeWorker(allNumbers, primeResults, nextNumberIndex);
                // Criamos a Thread, passando o Worker como tarefa.
                Thread thread = new Thread(worker);
                threads.add(thread);
                // O método start() agenda a thread para ser executada pela JVM.
                thread.start();
            }

            // --- 4. ESPERAR TODAS AS THREADS TERMINAREM ---
            // A thread 'main' chama o método join() para cada thread worker.
            // Isso faz com que a 'main' pause e espere a finalização da outra thread.
            for (Thread thread : threads) {
                thread.join();
            }

            // --- 5. RECONSTRUIR A ORDEM E ESCREVER O RESULTADO ---
            // Como o Map não garante a ordem de iteração, precisamos reconstruir a lista
            // na ordem correta usando os índices que salvamos.
            List<Long> sortedPrimes = new ArrayList<>();
            for (int i = 0; i < allNumbers.size(); i++) {
                if (primeResults.containsKey(i)) {
                    sortedPrimes.add(primeResults.get(i));
                }
            }

            Path outputPath = Paths.get(outputFile);
            Files.write(outputPath, sortedPrimes.stream()
                                                .map(String::valueOf)
                                                .collect(Collectors.toList()));
            
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            System.out.println("Processamento paralelo (" + NUM_THREADS + " threads) concluído.");
            System.out.println("Tempo de execução: " + durationMs + " ms");

            // --- 6. GRAVAR TEMPO DE EXECUÇÃO ---
            try (FileWriter fw = new FileWriter(resultsFile, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("Paralelo," + NUM_THREADS + "," + durationMs);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ocorreu um erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}