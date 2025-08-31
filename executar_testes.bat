@echo off
echo Compilando o codigo Java...
javac -d bin src/PrimeUtils.java src/PrimeWorker.java src/PrimosSequencial.java src/PrimosParalelo5.java src/PrimosParalelo10.java


echo.
echo --- Iniciando Bateria de Testes ---
echo.

echo Executando Teste Sequencial 10 vezes...
for /L %%i in (1, 1, 10) do (
    java -cp bin PrimosSequencial
)

echo.
echo Executando Teste Paralelo com 5 Threads 10 vezes...
for /L %%i in (1, 1, 10) do (
    java -cp bin PrimosParalelo5
)

echo.
echo Executando Teste Paralelo com 10 Threads 10 vezes...
for /L %%i in (1, 1, 10) do (
    java -cp bin PrimosParalelo10
)

echo.
echo --- Bateria de Testes Concluida! ---
echo O arquivo 'results/tempos_execucao.csv' foi atualizado com 30 resultados.