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
        String inputFile = "input/Entrada01.txt";// Caminho do arquivo de entrada
        String outputFile = "output/primos_paralelo10.txt";// Caminho do arquivo de saída
        String resultsFile = "results/tempos_execucao.csv";// Caminho do arquivo de resultados

        System.out.println("Iniciando processamento paralelo com " + NUM_THREADS + " threads...");

        long startTime = System.nanoTime();// Marca o tempo de início

        try {
            // --- 1. LEITURA DO ARQUIVO ---
            Path inputPath = Paths.get(inputFile);// Caminho do arquivo de entrada
            List<Long> allNumbers = Files.lines(inputPath)// Lê todas as linhas do arquivo
                                         .map(Long::parseLong)// Converte cada linha para Long
                                         .collect(Collectors.toList());// Coleta os resultados em uma lista

            // --- 2. PREPARAR RECURSOS COMPARTILHADOS ---
            // Usamos um Map sincronizado para que as threads possam inserir resultados
            // concorrentemente sem corromper a estrutura de dados.
            Map<Integer, Long> primeResults = Collections.synchronizedMap(new HashMap<>());
            // Contador atômico para distribuir o trabalho.
            AtomicInteger nextNumberIndex = new AtomicInteger(0);

            // --- 3. CRIAR E INICIAR AS THREADS ---
            List<Thread> threads = new ArrayList<>();// Lista para armazenar as threads
            for (int i = 0; i < NUM_THREADS; i++) {// Cria e inicia as threads
                // Criamos uma instância do nosso Worker.
                Runnable worker = new PrimeWorker(allNumbers, primeResults, nextNumberIndex);
                // Criamos a Thread, passando o Worker como tarefa.
                Thread thread = new Thread(worker);
                threads.add(thread);// Adiciona a thread à lista
                // O método start() agenda a thread para ser executada pela JVM.
                thread.start();
            }

            // --- 4. ESPERAR TODAS AS THREADS TERMINAREM ---
            // A thread 'main' chama o método join() para cada thread worker.
            // Isso faz com que a 'main' pause e espere a finalização da outra thread.
            for (Thread thread : threads) {// Espera a thread terminar
                thread.join();// Aguarda a finalização da thread
            }

            // --- 5. RECONSTRUIR A ORDEM E ESCREVER O RESULTADO ---
            // Como o Map não garante a ordem de iteração, precisamos reconstruir a lista
            // na ordem correta usando os índices que salvamos.
            List<Long> sortedPrimes = new ArrayList<>();// Lista para armazenar os primos encontrados
            for (int i = 0; i < allNumbers.size(); i++) {// Itera sobre todos os números
                if (primeResults.containsKey(i)) {// Verifica se o índice está presente no mapa
                    sortedPrimes.add(primeResults.get(i));// Adiciona o número primo à lista
                }
            }

            Path outputPath = Paths.get(outputFile); // Caminho do arquivo de saída
            Files.write(outputPath, sortedPrimes.stream()
                                                .map(String::valueOf)// Converte cada número primo para String
                                                .collect(Collectors.toList()));// Coleta os resultados em uma lista

            long endTime = System.nanoTime();// Fim da medição de tempo
            long durationMs = (endTime - startTime) / 1_000_000;// Convertendo para milissegundos

            System.out.println("Processamento paralelo (" + NUM_THREADS + " threads) concluído.");
            System.out.println("Tempo de execução: " + durationMs + " ms");

            // --- 6. GRAVAR TEMPO DE EXECUÇÃO ---
            try (FileWriter fw = new FileWriter(resultsFile, true);// Modo "append"
                 PrintWriter pw = new PrintWriter(fw)) {// Cria um PrintWriter para gravar no arquivo
                pw.println("Paralelo," + NUM_THREADS + "," + durationMs);// Grava os resultados
            }

        } catch (IOException | InterruptedException e) {// Captura exceções de I/O e interrupção
            System.err.println("Ocorreu um erro: " + e.getMessage());// Imprime a mensagem de erro
            e.printStackTrace();// Imprime a stack trace do erro
        }
    }
}