/**
 * Classe utilitária com métodos relacionados a números primos.
 * Isolar essa lógica aqui permite que todas as outras classes a reutilizem,
 * evitando duplicação de código.
 */
public class PrimeUtils {

    /**
     * Verifica se um número é primo.
     * * @param number O número a ser verificado.
     * @return true se o número for primo, false caso contrário.
     */
    public static boolean isPrime(long number) {// Verifica se um número é primo
        // Números menores ou iguais a 1 não são primos.
        if (number <= 1) {
            return false;
        }
        
        // O teste de primalidade pode ser otimizado verificando divisores
        // apenas até a raiz quadrada do número. Se um número 'n' tem um divisor
        // maior que sua raiz quadrada, ele necessariamente terá um outro divisor
        // menor que sua raiz quadrada.
        for (long i = 2; i * i <= number; i++) {// Verifica possíveis divisores
            
            if (number % i == 0) { // Se o número for divisível por 'i', então não é primo.
                return false; // O número não é primo.
            }
        }
        
        // Se o loop terminar sem encontrar divisores, o número é primo.
        return true; 
    }
}