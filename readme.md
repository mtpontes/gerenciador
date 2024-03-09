## Projeto Gerenciador
Bem-vindo ao Projeto Gerenciador! Este é um sistema de gerenciamento simples desenvolvido em Java, utilizando Servlets e JSPs. Este README fornece uma visão geral do projeto, sua arquitetura, tecnologias utilizadas, funcionamento dos principais componentes, persistência de dados e recursos estáticos.

## Arquitetura do Projeto

O projeto está organizado em três principais pacotes:
- **br.com.alura.gerenciador.acao:** Contém as classes que representam as diferentes ações executadas pelo sistema.
- **br.com.alura.gerenciador.modelo:** Engloba as classes que definem o modelo de dados da aplicação, como Empresa e Usuario.
- **br.com.alura.gerenciador.servlet:** Compreende os servlets e filtros responsáveis pelo controle e fluxo da aplicação.

## Tecnologias

- **Java Servlets**
- **JSP** 


## Mapeamento de requisições
### ControladorFilter
Responsável por identificar a ação solicitada pelo usuário através do parâmetro de URL "acao", instanciar a classe correspondente utilizando reflection e executar a ação. Após a execução, encaminha ou redireciona conforme necessário.

## Persistência
A classe Banco simula a persistência de dados em memória, mantendo listas de empresas e usuários. Os dados são inicializados no carregamento da aplicação.

## Recursos Estáticos
Os recursos estáticos, como páginas HTML e JSPs, estão localizados no diretório `WebContent` e suas subpastas.