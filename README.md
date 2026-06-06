# Junor Script Engine
esta é uma simulação de um "motor de scripts" em java

### ATENÇÃO:
isto não é nada muito sério, é apenas um hobby de um teste de um "motor de scripts"

## Arquivos

junor - é o arquivo puro e solto, onde os comandos são escritos.

jmtdt (Junor Meta Data) - é o manifesto do CJunor onde diz a versão e o author.

CJunor (Compiled Junor) - contendo o manifest.jmtdt e o main.junor, é o script junor compilado, onde o interpretador pode rodar

## Funções

JEE (Junor Executer Environment) - ele é quem inicia tudo, se um arquivo .CJunor for associado a o executável (em releases, o exe 64 bits X64) ele automaticamente começa a executar o junor script pelo JFEE

JFEE (Junor Files Executer Enivironment) - ele é responsável por ler e executar as ações de um arquivo .CJunor (iniciado pelo comando run ou se um arquivo .CJunor for aberto com o exe)

Junor Commands - ele é o responsável pela interpretação e execução dos comandos

para saber todos os comandos, digite help no terminal com a JEE rodando.
