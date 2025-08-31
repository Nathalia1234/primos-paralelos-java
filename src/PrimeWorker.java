import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A classe Worker implementa a interface Runnable.
 * Cada instância desta classe representa uma tarefa que será executada por uma thread.
 * Esta é a abordagem recomendada, pois separa o "o quê" (a tarefa) do "quem" (a thread).
 */
public class PrimeWorker implements Runnable {

    // --- ATRIBUTOS COMPARTILHADOS ---
    // final: Garante que a referência a esses objetos não mudará após a criação do worker.
    private final List<Long> numbersToProcess;// Lista de números a serem processados
    private final Map<Integer, Long> primeResults;// Mapa para armazenar os resultados
    private final AtomicInteger nextNumberIndex; // Foi utilizado AtomicInteger para um contador thread-safe.

    /**
     * Construtor do Worker.
     * @param numbersToProcess Lista de números a serem processados.
     * @param primeResults Mapa onde os resultados (primos) serão armazenados.
     * @param nextNumberIndex Contador compartilhado que indica o índice do próximo número a ser pego.
     */


    public PrimeWorker(List<Long> numbersToProcess, Map<Integer, Long> primeResults, AtomicInteger nextNumberIndex) {// Construtor do Worker
        this.numbersToProcess = numbersToProcess;
        this.primeResults = primeResults;
        this.nextNumberIndex = nextNumberIndex;// Construtor do Worker
    }

    /**
     * O método run() contém a lógica que a thread executará.
     */

    @Override
    public void run() {// Método que será executado pela thread
        while (true) {// Laço infinito para processamento contínuo
            // --- DIVISÃO DINÂMICA DE CARGA ---
            // Cada thread, de forma atômica, pega o próximo índice disponível e o incrementa.
            // Isso garante que nenhum número será processado por mais de uma thread.
            int currentIndex = nextNumberIndex.getAndIncrement();

            // Se o índice pego for maior ou igual ao tamanho da lista, não há mais trabalho.
            if (currentIndex >= numbersToProcess.size()) {
                break; // A thread encerra sua execução.
            }

            long numberToCheck = numbersToProcess.get(currentIndex);// Pega o número a ser verificado

            // A verificação de primalidade é a parte mais demorada, e acontece fora de qualquer
            // bloco sincronizado, permitindo que todas as threads a executem em paralelo.
            if (PrimeUtils.isPrime(numberToCheck)) {
                // O mapa 'primeResults' é sincronizado, então a inserção é thread-safe.
                // Armazenamos o primo encontrado junto com seu índice original para manter a ordem.
                primeResults.put(currentIndex, numberToCheck);
            }
        }
    }
}