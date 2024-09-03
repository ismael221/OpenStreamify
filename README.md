# Filme
Contexto
A empresa recebe uma nova demanda de uma casa cultural da cidade que conta com uma sala de cinema. O objetivo é montar um site para o estabelecimento, porém há uma seção específica na qual você trabalhará. Além da divulgação das ações do estabelecimento, a casa cultural quer um espaço em que as pessoas possam registrar os movie a que assistiram e avaliar cada um deles (fornecer análises).
 
Concentrando-se nessa funcionalidade, neste momento de avaliações e protótipos, você deverá criar um sistema web que permita cadastrar movie e realizar análises desses movie cadastrados. Para isso, você precisará de duas entidades Model:
 
Filme: id, título, sinopse, gênero e ano de lançamento
Análise: id, movie, análise e nota
 
O cliente está ansioso para ver o projeto e, portanto, para fins de testes iniciais, o desenvolvimento deve ser feito utilizando apenas armazenamento em memória (sem banco de dados). Inclua, no sistema, páginas para cadastrar movie e listar os movie já cadastrados e uma página de detalhes que exibirá todas as informações sobre o movie selecionado, além de permitir adicionar uma análise.
 
Atividade
Usando o NetBens, desenvolva um sistema web Spring MVC sem banco de dados, de acordo com a descrição do contexto. Preste atenção nos passos a seguir:
 
Comece criando um projeto no Spring Initializr.
Defina as entidades Filme e Análise.
Crie classes de controlador para manipular as requisições HTTP (hyper text transfer protocol), como cadastrar um movie assistido e adicionar uma análise.
Implemente as visualizações – páginas HTML (hyper text markup language) – para a interação do usuário, como formulários para cadastrar um movie e adicionar uma análise.
Teste o sistema web sem banco de dados, verificando se as funcionalidades básicas estão funcionando corretamente.
