[![Travis](https://img.shields.io/travis/rtpaulino/xy-inc.svg)](https://travis-ci.org/rtpaulino/xy-inc) [![Codecov](https://img.shields.io/codecov/c/github/rtpaulino/xy-inc.svg)](https://codecov.io/gh/rtpaulino/xy-inc)

# xy-inc

Através do XY-Inc você poderá **cadastrar modelos de dados simples** e
utilizá-los através de interface RESTful.

## Tecnologias

- **Spring Boot**

- **Gradle** (dependências, build, etc.)

- **Groovy**

  Escolhi *Groovy* ao invés de *Java* puro por ser menos *verbose* e ter recursos que agilizam a codificação

- **Spock** (testes)

- **JaCoCo** (cobertura)

## Banco de Dados

Para facilitar a utilização, não configurei um banco de dados específico. Assim o Spring irá subir um banco *H2*
em memória (volátil). Se desejar manter os dados persistidos, basta:

1. Configurar DataSource com H2 pesistido; ou

2. Configurar DataSource para um *SGDB* que dê suporte acesso via JDBC; (o código deve funcionar igualmente sem problemas)

Não recomendo a utilização do *H2* para nenhum sistema. Ele foi utilizado nesse projeto apenas por conveniência.

## Arquitetura

O sistema pode ser dividido em três camadas principais:

- **Controllers**

    A camada dos *Controllers* é responsável por:
    - Definição das URL's e Métodos (GET/POST/PUT/POST) disponíveis;
    - Receber requisição REST;
    - Repassar para a camada dos *Services*
    - Retornar a resposta adequada para o cliente; (Definir Status Code, Http Headers, Body, etc.)

- **Services**

    A camada dos *Services* concentra as regras de negócio e o funcionamento da aplicação.
    Esta camada recebe os pedidos da camanda de *Controllers* e utiliza a camada de *Models*
    para acessar os dados persistidos.

- **Models**

    A camda de *Models* é a camanda onde é definida as entidades de pesistência de banco. Utilizei **JPA**
    para definir as entidades e `CrudRepository` para facilitar a utilização e persistência dos dados,
    bem como criação de *Queries*.

Como o sistema é inteiramente REST não há necessidade de uma camada de visualização.

### Tratamento de Erros

O tratamento de erros é centralizado através da classe `GlobalExceptionHandler`.

Foram criadas diversas classes de exceção específicas para facilitar a identificação e tratamento
dos erros.

### Considerações a respeito do modelo de dados

Ao ler o problema pensei em duas formas de resolvê-lo:

1. Criar estruturas de tabelas dinamicamente de acordo com as especificações informadas pelo usuário; ou

2. Criar uma estrutura de tabelas única que comporte os diversos tipos de modelo possíveis;

Acredito que a opção número 1 é a melhor opção se desejarmos criar um serviço que futuramente permita
criação de consultas personlizadas com índices sobre colunas, etc. No entanto desconsiderei essa opção
no presente projeto por conta da restrição de tempo. Essa solução necessitaria que fosse tratado diversas
situações:
- Ao mudar a estrutura, se já houver dados, o que fazer? Perder os dados?
- Como converter os dados no caso de mudança de formato da coluna?
- Seria necessário criar diversas chamadas SQL para cada mudança (ALTER TABLE)
- Não seria possível utilizar as facilidades do Spring e JPA. Tornar-se-ia necessário criar Queries dinâmicas
para cada um dos modelos criados.

Por esses motivos acima citados, implementei a segunda opção. Mesmo assim, também necessitei decidir
entre duas abordagens:

1. Criar uma tabela única para guardar os dados com uma coluna do tipo CLOB contendo os dados
de acordo com a estrutura definida pelo usuário; ou

2. Criar uma tabela onde cada linha corresponde a um dado de coluna e relacioná-los com uma mesma linha
de dados. Nessa tabela poderíamos ter uma coluna para cada tipo de dado possível.

Implementei a opção número 1 pois considerei ser mais simples; no entanto, percebo também que a opção
dois, apesar de mais complicada (teria que consultar diversas linhas de tabela e agregar os valores em uma
linha apenas), poderia permitir um armazenamento dos dados de acordo com o tipo de dado e ainda permitiria
futuramente consultas mais complexas com índices.

A opção 1 também tem a vantagem que, na mudança de estrutura, não é necessário alterar os dados. Quando o
dado for acessado, então posso enviar no formato da definição do momento.

Também considerei a utilização de banco de dados NoSQL (como Cassandra). No entanto, não me pareceu
adequado para a evolução do produto, visto que bancos NoSQL muitas vezes apresentam restrições intrínsecas
quando deseja-se fazer buscas mais complexas, bem como agragações de dados.

Por exemplo, no Cassandra, a própria busca por "todas as linhas" é extremamente não recomendada.

Por fim, os dados são armazenados no formato *JSON* na coluna *CLOB*. Se, por exemplo, utilizássemos
um banco como o *PostgreSQL*, poderíamos nos utilizar da coluna de tipo *JSON* e, dessa forma, permitir
consultas com filtros nesses dados. Muito provavelmente essa forma já atenderia a futura demandas para o projeto.
