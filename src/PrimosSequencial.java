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
    // ==== 1. A thread main começa a trabalhar. ====
    public static void main(String[] args) {
        // --- CONFIGURAÇÃO ---
        String inputFile = "input/Entrada01.txt"; // arquivo que contém os números a serem verificados (números primos)
        String outputFile = "output/primos_sequencial.txt"; // arquivo que conterá os números primos encontrados
        String resultsFile = "results/tempos_execucao.csv"; // arquivo que registrará os tempos de execução

        System.out.println("Iniciando processamento sequencial..."); 
        
        // --- MEDIÇÃO DE TEMPO (INÍCIO) ---
        // Foi usado o  System.nanoTime() para maior precisão na medição.
        // 3. Inicia um cronômetro (startTime).
        long startTime = System.nanoTime();

        try {
            // --- LEITURA DO ARQUIVO ---
            // 2. Lê todos os números do arquivo input/Entrada01.txt para uma lista.
            Path inputPath = Paths.get(inputFile);// Caminho do arquivo de entrada
            List<Long> numbers = Files.lines(inputPath)// Lê todas as linhas do arquivo
                                      .map(Long::parseLong)// Converte cada linha para Long
                                      .collect(Collectors.toList());// Coleta os resultados em uma lista

            // --- PROCESSAMENTO ---
            // A lista 'primeNumbers' armazenará os primos encontrados.
            // 4. Usa um laço for simples para percorrer a lista, um número de cada vez.
            // 5. Para cada número, ele usa a ferramenta "PrimeUtils.isPrime()".
            // 6. Se o número for primo, ele é adicionado à lista 'primeNumbers'.
            List<Long> primeNumbers = new ArrayList<>();
            // Iteramos sobre cada número da lista lida.
            for (Long number : numbers) {
                // Usamos nosso método utilitário para verificar a primalidade.
                if (PrimeUtils.isPrime(number)) {
                    primeNumbers.add(number);
                }
            }

            // --- ESCRITA DO RESULTADO ---
            // 8. Escreve os números primos encontrados no arquivo de saída.(output/primos_sequencial.txt)
            Path outputPath = Paths.get(outputFile);// Caminho do arquivo de saída
            Files.write(outputPath, primeNumbers.stream()// Converte os números primos para String e escreve no arquivo
                                                .map(String::valueOf)// Converte cada número primo para String
                                                .collect(Collectors.toList()));// Coleta os resultados em uma lista

            // --- MEDIÇÃO DE TEMPO (FIM) ---
            // 7. Quando o laço termina, ele para o cronômetro (endTime).
            long endTime = System.nanoTime();// Fim da medição de tempo
            long durationMs = (endTime - startTime) / 1_000_000; // Convertendo para milissegundos

            System.out.println("Processamento sequencial concluído.");
            System.out.println("Tempo de execução: " + durationMs + " ms");
            
            // --- 4. GRAVAR TEMPO DE EXECUÇÃO ---
            // Gravamos o resultado em um arquivo CSV para análise posterior.
            // O 'true' no FileWriter indica que o arquivo será aberto em modo "append" (adicionar ao final).
            try (FileWriter fw = new FileWriter(resultsFile, true);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.println("Sequencial,1," + durationMs);// Grava o tempo de execução no arquivo CSV
            }

        } catch (IOException e) {// Captura exceções de I/O
            System.err.println("Ocorreu um erro de I/O: " + e.getMessage());// Mensagem de erro
            e.printStackTrace();// Impressão da stack trace
        }
    }
}