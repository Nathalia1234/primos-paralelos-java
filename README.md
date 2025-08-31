# Desafio 02 - Verificador de Números Primos com Programação Paralela

**Autor(a):** Nathalia Ohana Barigchum Leite

**Disciplina:** Sistemas Distribuídos e Programação Paralela

**Data de Entrega:** 01/09/2025

---

## 1. Objetivo do Projeto

O objetivo deste trabalho é desenvolver e analisar o desempenho de um programa que verifica números primos a partir de um arquivo de entrada (Entrada01.txt).

Foram implementadas **três versões distintas** para fins comparativos:

1.  **Implementação Sequencial:** Utilizando uma única thread.

2.  **Implementação Paralela:** Utilizando 5 threads.

3.  **Implementação Paralela:** Utilizando 10 threads.

O projeto visa demonstrar o ganho de performance (speedup) obtido com a utilização de programação paralela para tarefas computacionalmente intensivas e divisíveis.

## 2. Estrutura do Repositório

O projeto está organizado da seguinte forma:

- **/input**: Contém o arquivo `Entrada01.txt` com a lista de números a serem processados.

- **/output**: Contém os arquivos de saída com a lista de números primos encontrados por cada implementação.

- **/results**: Contém o arquivo `tempos_execucao.csv` com os dados de tempo coletados.

- **/src**: Contém todo o código-fonte Java:

  - `PrimeUtils.java`: Classe utilitária com o método `isPrime()`.

  - `PrimosSequencial.java`: Executável da versão de thread única.

  - `PrimeWorker.java`: Classe `Runnable` que define a tarefa a ser executada pelas threads.

  - `PrimosParalelo5.java`: Executável da versão com 5 threads.

  - `PrimosParalelo10.java`: Executável da versão com 10 threads.

  - `Analise_Desempenho.xlsx`: Planilha com a tabela de tempos e o gráfico comparativo.

  - `executar_testes.bat`: Script de testes para rodar cada arquivo quantas vezes for necessário, somente inserindo no "for" a quantidade necessária para testes.

  - `README.md`: Este relatório.

## 3. Estratégia de Implementação

### 3.1. Versão Sequencial

A abordagem sequencial é a mais simples e serve como nossa linha de base para comparação de desempenho.

- A thread principal (`main`) é responsável por todo o fluxo.

- Ela lê todos os números do arquivo de entrada `input/Entrada01.txt` para uma lista.

- Em seguida, itera sobre essa lista, número por número, chamando a função `PrimeUtils.isPrime()` para cada um.

- Os números identificados como primos são adicionados a uma nova lista de resultados.

- Ao final, a lista de resultados é gravada no arquivo de saída `output/primos_sequencial.txt`

### 3.2. Versão Paralela

Para as versões paralelas, a estratégia adotada foi a de **Divisão Dinâmica de Carga** para distribuir o trabalho entre as threads.

- **Thread Principal (Orquestradora):**

  1.  Lê todos os números para uma lista compartilhada (`allNumbers`).

  2.  Cria um `Map` sincronizado (`primeResults`) para armazenar os primos encontrados de forma segura (thread-safe). O uso de um `Map<Índice, Número>` é crucial para resolver o desafio de **manter a ordem original** dos resultados.

  3.  Cria um contador atômico (`AtomicInteger`) que servirá para que as threads "peguem" o próximo número a ser processado. **Optei por** `Collections.synchronizedMap` e `AtomicInteger ` **por serem mecanismos de alto nível, eficientes e seguros para operações atômicas específicas (acesso ao mapa de resultados e incremento do contador), evitando a complexidade de blocos** `synchronized` **manuais para estas tarefas.**

  4.  Inicia o número de threads (5 ou 10), onde cada thread recebe uma instância da tarefa `PrimeWorker`.

  5.  Após iniciar todas as threads, a thread principal fica em estado de espera, utilizando o método `thread.join()`, aguardando a finalização de todas as workers.

  6.  Quando todas as workers terminam, a thread principal reconstrói a lista de primos na ordem correta a partir do `Map` e a salva no arquivo de saída `primos_paralelo5.txt` ou `primos_paralelo10.txt`.

- **Threads de Trabalho (`PrimeWorker`):**

  - Cada thread executa um loop contínuo.

  - Dentro do loop, a thread pega o índice do próximo número a ser processado de forma atômica. Isso garante que duas threads nunca processem o mesmo número.

  - A verificação de primalidade (`isPrime()`), que é a parte mais custosa, é executada em paralelo por todas as threads.

  - Se um número é primo, ele é adicionado ao `Map` de resultados sincronizado.

Esta abordagem é eficiente porque threads que terminam de processar um número (seja ele pequeno ou grande) podem imediatamente pegar o próximo disponível, garantindo que nenhuma thread fique ociosa enquanto outras ainda têm trabalho a fazer (balanceamento de carga).

## 4. Como Compilar e Executar

1.  **Compilar todos os arquivos:**

    ```bash
    javac src/*.java -d bin
    ```

2.  **Executar a versão Sequencial:**

    ```bash
    java -cp bin PrimosSequencial
    ```

3.  **Executar a versão Paralela com 5 Threads:**

    ```bash
    java -cp bin PrimosParalelo5
    ```

4.  **Executar a versão Paralela com 10 Threads:**
    ```bash
    java -cp bin PrimosParalelo10
    ```

## 5. Análise de Desempenho

Após executar todas as implementações, os tempos de execução foram coletados no arquivo `results/tempos_execucao.csv` e consolidados abaixo.

_Observação: Foi executado cada versão algumas vezes para obter uma média de tempo mais estável e assim criar um gráfico com mais performance._

![alt text](img/tempos_execucao.png)

![alt text](img/tabela_resultados_brutos.png)

**Tabela de Tempos de Execução (Média)**

| Implementação | Nº de Threads | Tempo Médio (ms) | Speedup                 |
| ------------- | ------------- | ---------------- | ----------------------- |
| Sequencial    | 1             | 914,1            | -                       |
| Paralelo      | 5             | 399,6            | Aprox. 2,29 mais rápido |
| Paralelo      | 10            | 364,1            | Aprox. 2,51 mais rápido |

_O **Speedup** é calculado como: (Tempo Sequencial) / (Tempo Paralelo)._

Então:

- 914,1 / 914,1 = 1,00x

- 914,1 / 399,6 = 2,29x

- 914,1 / 364,1 = 2,51x

![alt text](img/grafico_desempenho.png)

### 6. Análise dos Resultados

---

A análise dos dados demonstra dois pontos principais:

- **O Salto Inicial para o Paralelismo**: A transição da execução sequencial (914,1 ms) para a paralela com 5 threads (397,6 ms) gerou um ganho de performance expressivo, resultando em um **speedup de 2,30 vezes**. Isso comprova a eficácia da paralelização.

- **Retornos Decrescentes com Mais Threads**: Ao dobrar o número de workers para 10 threads, o desempenho continuou a melhorar, atingindo uma média de **364,1 ms** e um **speedup de 2,51 vezes**. No entanto, é crucial notar que o ganho adicional foi muito menor que o salto inicial. Passar de 1 para 5 threads reduziu o tempo em mais de 500 ms, enquanto passar de 5 para 10 threads reduziu o tempo em apenas 33,5 ms. Este fenômeno é conhecido como **lei dos retornos decrescentes**.

## 7. Correlação com o Hardware de Teste

A explicação para os retornos decrescentes está diretamente ligada às especificações do hardware. O processador, um **Intel Core i7 8550U**, possui **4 núcleos físicos e 8 threads lógicas**.

- A versão com **5 threads** já consegue ocupar boa parte da capacidade de processamento paralelo do CPU.

- A versão com **10 threads** excede o número de processadores lógicos disponíveis (8). Embora o sistema operacional consiga gerenciar essas threads e extrair um pouco mais de performance, ele o faz à custa de um aumento na sobrecarga (overhead) de troca de contexto, pois precisa revezar mais de uma thread em um mesmo núcleo lógico. Isso explica por que o ganho de desempenho foi pequeno e não linear.

Informações do Hardware testado (meu notebook pessoal):

![alt text](img/especificacoes_hardware.png)

## 8. Conclusão

A análise comprova que a paralelização é uma ferramenta poderosa, mas seus benefícios não escalam infinitamente. Existe um ponto ótimo que depende da arquitetura do hardware e da natureza da tarefa. Para este cenário, aumentar o número de threads de 5 para 10 trouxe uma melhoria marginal, demonstrando que a maior parte do ganho de performance já havia sido obtida ao utilizar um número de threads próximo à capacidade de paralelismo do processador.
