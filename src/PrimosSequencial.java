import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação Sequencial para verificação de números primos.
 * Todo o processamento é feito na thread principal (main).
 */
public class PrimosSequencial {

    public static void main(String[] args) {
        // --- CONFIGURAÇÃO ---
        String inputFile = "input/Entrada01.txt";
        String outputFile = "output/primos_sequencial.txt";
        String resultsFile = "results/tempos_execucao.csv";

        System.out.println("Iniciando processamento sequencial...");
        
        // --- MEDIÇÃO DE TEMPO (INÍCIO) ---
        // Usamos System.nanoTime() para maior precisão na medição.
        long startTime = System.nanoTime();

        try {
            // --- 1. LEITURA DO ARQUIVO ---
            Path inputPath = Paths.get(inputFile);
            List<Long> numbers = Files.lines(inputPath)
                                      .map(Long::parseLong)
                                      .collect(Collectors.toList());
            
            // --- 2. PROCESSAMENTO ---
            // A lista 'primeNumbers' armazenará os primos encontrados.
            List<Long> primeNumbers = new ArrayList<>();
            // Iteramos sobre cada número da lista lida.
            for (Long number : numbers) {
                // Usamos nosso método utilitário para verificar a primalidade.
                if (PrimeUtils.isPrime(number)) {
                    primeNumbers.add(number);
                }
            }

            // --- 3. ESCRITA DO RESULTADO ---
            Path outputPath = Paths.get(outputFile);
            Files.write(outputPath, primeNumbers.stream()
                                                .map(String::valueOf)
                                                .collect(Collectors.toList()));
            
            // --- MEDIÇÃO DE TEMPO (FIM) ---
            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000; // Convertendo para milissegundos

            System.out.println("Processamento sequencial concluído.");
            System.out.println("Tempo de execução: " + durationMs + " ms");
            
            // --- 4. GRAVAR TEMPO DE EXECUÇÃO ---
            // Gravamos o resultado em um arquivo CSV para análise posterior.
            // O 'true' no FileWriter indica que o arquivo será aberto em modo "append" (adicionar ao final).
            try (FileWriter fw = new FileWriter(resultsFile, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("Sequencial,1," + durationMs);
            }

        } catch (IOException e) {
            System.err.println("Ocorreu um erro de I/O: " + e.getMessage());
            e.printStackTrace();
        }
    }
}