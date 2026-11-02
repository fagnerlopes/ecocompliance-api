# language: pt
Funcionalidade: Governança ambiental da empresa
  A API registra empresas e controla quem pode alterar o cadastro.
  Os cenários cobrem o caminho feliz e as falhas de validação, acesso e recurso inexistente.

  Cenário: Cadastrar empresa com dados válidos
    Dado um administrador autenticado
    Quando ele cadastra a empresa "Verde Norte Industria Ltda" com CNPJ "12.345.678/0001-90"
    Então a API responde 201
    E o corpo contém o nome "Verde Norte Industria Ltda"
    E o contrato JSON da empresa é respeitado

  Cenário: Recusar empresa sem nome
    Dado um administrador autenticado
    Quando ele envia um cadastro de empresa sem nome
    Então a API responde 400
    E o corpo informa erro de validação

  Cenário: Usuário comum não cadastra empresa
    Dado um usuário autenticado
    Quando ele cadastra a empresa "Verde Norte Industria Ltda" com CNPJ "12.345.678/0001-90"
    Então a API responde 403

  Cenário: Consultar empresa inexistente
    Dado um usuário autenticado
    Quando ele busca a empresa de id 999
    Então a API responde 404
    E a mensagem informa que o recurso não foi encontrado

  Cenário: Entrar com credenciais válidas
    Quando o usuário entra com o e-mail "admin@ecocompliance.com" e a senha "admin123"
    Então a API responde 200
    E o corpo contém um token Bearer
