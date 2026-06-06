# Junor Script Engine
esta é uma simulação de um "motor de scripts" em java

### ATENÇÃO:
isto não é nada muito sério, é apenas um hobby de um teste de um "motor de scripts"

## Arquivos

junor - é o arquivo puro e solto, onde os comandos são escritos.

jmtdt (Junor Meta Data) - é o manifesto do CJunor onde diz a versão e o author.

CJunor (Compiled Junor) - contendo o manifest.jmtdt e o main.junor, é o script junor compilado, onde o interpretador pode rodar

## Comandos

até o momento, os comandos disponiveis são:

strVar(name=\"variable_name\", value=[\"variable_value_text\"]) - cria uma váreavel do tipo String

intVar(name=\"variable_name\", value=[variable_value_int]) - cria uma váreavel do tipo Integer

boolVar(name=\"variable_name\", value=[variable_value_false_and_true]) - cria uma váreavel do tipo Boolean

show(\"text\") - escreve um texto no console

run([\"archive_cjunor_location\"]) - roda arquivos .junor

input(type=(int|str), target=\"var_name\", msg=\"text\") - cria um input para o usuário digitar (integer ou string)

createCJunor(CJunorLocation=[\"cjunor_archive_destination\"], originalJunor=[\"junor_file_location\"], scriptAuthor=(\"text\")) - compila o junor para .CJunor

if(check={val op val}, then={cmd}) elif<check={val op val}, then={cmd}> else=<cmd> - checagem de condição (if/elif/else)

calc(target=(\"var_name\"), calc=(num1, oper, num2), print=(true|false)) - calcula um inteiro com o outro

for(var=(\"var_name\"), range=(start to end), do={cmd}) - cria um loop do tipo for

while(check={val op val}, do={cmd}) - cria um loop do tipo while

func(name=(\"func_name\"), body={cmd|cmd}) - cria uma função

call(name=(\"func_name\")) - chama uma função

exit - (no modo interativo) sai do modo interativo para o terminal normal

help - lista os comandos disponíveis da Junor

## Funções

JEE (Junor Executer Environment) - ele é quem inicia tudo, se um arquivo .CJunor for associado a o executável (em releases, o exe 64 bits X64) ele automaticamente começa a executar o junor script pelo JFEE

JFEE (Junor Files Executer Enivironment) - ele é responsável por ler e executar as ações de um arquivo .CJunor (iniciado pelo comando run ou se um arquivo .CJunor for aberto com o exe)

Junor Commands - ele é o responsável pela interpretação e execução dos comandos
