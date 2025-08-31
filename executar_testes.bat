REM @echo off
REM Este comando desativa a exibicao dos proprios comandos no terminal.
REM Sem ele, cada linha do script apareceria na tela antes de ser executada. Deixa a saida mais limpa.
@echo off

REM Exibe a mensagem "Compilando o codigo Java..." para o usuario saber o que esta acontecendo.
echo Compilando o codigo Java...

REM Este e o comando que compila todos os seus arquivos .java.
REM javac -> E o programa compilador do Java.
REM -d bin -> E uma flag que diz ao compilador para colocar os arquivos .class resultantes dentro de uma pasta chamada "bin".
REM src/PrimeUtils.java ... -> E a lista de todos os arquivos de codigo-fonte que devem ser compilados.
javac -d bin src/PrimeUtils.java src/PrimeWorker.java src/PrimosSequencial.java src/PrimosParalelo5.java src/PrimosParalelo10.java

REM O comando "echo." e usado para imprimir uma linha em branco, criando um espacamento para melhor legibilidade.
echo.

REM Exibe um titulo para a proxima secao de testes.
echo --- Iniciando Bateria de Testes ---
echo.

REM Informa qual bloco de testes esta comecando.
echo Executando Teste Sequencial 10 vezes...

REM Inicia um laco de repeticao (loop) que vai de 1 a 10.
REM /L -> indica que e um loop numerico.
REM %%i -> e a variavel do loop (como 'int i' em Java). Precisa de dois '%%' dentro de um script .bat.
REM in (1, 1, 10) -> define (inicio, passo, fim). Ou seja, comeca em 1, incrementa de 1 em 1, e para em 10.
REM do (...) -> contem o(s) comando(s) a ser(em) executado(s) em cada iteracao do loop.
for /L %%i in (1, 1, 10) do (
    REM Este comando executa o programa Java.
    REM java -> E a Maquina Virtual Java (JVM), que roda o codigo compilado.
    REM -cp bin -> E a flag de "classpath". Ela diz a JVM onde procurar os arquivos .class (neste caso, na pasta "bin").
    REM PrimosSequencial -> E o nome da classe principal que contem o metodo 'main' a ser executado.
    java -cp bin PrimosSequencial
)

echo.

REM Informa o inicio do proximo bloco de testes.
echo Executando Teste Paralelo com 5 Threads 10 vezes...

REM O mesmo tipo de loop, agora para a versao de 5 threads.
for /L %%i in (1, 1, 10) do (
    REM Executa a classe PrimosParalelo5, que esta na pasta "bin".
    java -cp bin PrimosParalelo5
)

echo.

REM Informa o inicio do ultimo bloco de testes.
echo Executando Teste Paralelo com 10 Threads 10 vezes...

REM E o ultimo loop, para a versao de 10 threads.
for /L %%i in (1, 1, 10) do (
    REM Executa a classe PrimosParalelo10, que esta na pasta "bin".
    java -cp bin PrimosParalelo10
)

echo.

REM Mensagem final para informar que todo o processo foi concluido com sucesso.
echo --- Bateria de Testes Concluida! ---
echo O arquivo 'results/tempos_execucao.csv' foi atualizado com 30 resultados.