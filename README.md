# Junor Script Engine
esta é uma simulação de um "motor de scripts" em java

está pronto para se tornar um Juninho? (programador Junor)

### ATENÇÃO:
isto não é nada muito sério, é apenas um hobby de um teste de um "motor de scripts"

## Arquivos

junor - é o arquivo puro e solto, onde os comandos são escritos.

jmtdt (Junor Meta Data) - é o manifesto do CJunor onde diz a versão e o author.

CJunor (Compiled Junor) - contendo o manifest.jmtdt e o main.junor, é o script junor compilado, onde o interpretador pode rodar

CJA (Compiled Junor Application) - contendo o manifest.jtmdt e o main.junor, é o script de aplicação junor! rodando sem nenhuma interface, diferente do CJunor

## Funções

JEE (Junor Executer Environment) - ele é quem inicia tudo, se um arquivo .CJunor for associado a o executável (em releases, o exe 64 bits X64) ele automaticamente começa a executar a Junor Script Engine

JAEE (Junor Application Executer Environment) - ele é reponsável por rodar os arquivos .CJA! rodando como se fosse um CJunor mas sem janela!

Junor Commands - ele é o responsável pela interpretação e execução dos comandos

para saber todos os comandos, digite help no terminal da JEE

### Coloque o JEE.exe para abrir arquivos .CJunor e o JAEE.exe para arquivos .CJA

# VERSÃO V0.0.5

## O que a de novo?

1 - adicionado o JAEE (Junor Application Executer Environment) junto com o JAFEE (Junor Application Files Executer Environment) que roda arquivos .CJA (Compiled Junor Application) sem abrir nenhuma janela!

2 - adicionado o comando createCJunorApp que permite criar uma aplicação junor

## Correções de bugs

1 - corrigido um bug onde se empilhasse um if no bloco then={} (ou em loops e funções também) ele parava de ler a linha no momento que encontrasse um } e bugava o codigo fazendo uma sintax error! mas agora foi adicionado um contador de {}: quando encontra um { ele aumenta numa vareavel int mais 1 ponto, quando encontrasse um } ele diminui 1 ponto, quando o contador chegasse a 0 ele parava, caso nunca chegue, ele dá sintax error.

Desculpe pela demora das atualizações! 😉

### Fim
Ei! se você gostou do meu projetinho e quer dar um apoio para o repositório, por favor, deixe uma estrela para mim! eu ficaria eternamente grato! 😉

