# language: pt
@regressivo
Funcionalidade: Governança ambiental da empresa
  A API registra empresas e controla quem pode alterar o cadastro.
  Os cenários cobrem o caminho feliz e as falhas de validação, acesso e recurso inexistente.

  Cenário: Cadastrar empresa com dados válidos
    Dado um administrador autenticado
    Quando ele cadastra uma empresa válida
    Então a API responde 201
    E o campo "nome" está preenchido
    E o contrato "empresa.json" é respeitado
    E o cabeçalho Location aponta para o recurso

  Cenário: Recusar empresa sem nome
    Dado um administrador autenticado
    Quando ele envia um cadastro de empresa sem nome
    Então a API responde 400
    E o campo "message" vale "Erro de validação nos campos"
    E o contrato "erro.json" é respeitado

  Cenário: Usuário comum não cadastra empresa
    Dado um usuário comum autenticado
    Quando ele cadastra uma empresa válida
    Então a API responde 403
    E o corpo vem vazio

  Cenário: Consultar empresa inexistente
    Dado um administrador autenticado
    Quando ele busca a empresa de id 999999
    Então a API responde 404
    E o contrato "erro.json" é respeitado

  Cenário: Entrar com credenciais válidas
    Quando o administrador entra com e-mail e senha
    Então a API responde 200
    E o campo "tipo" vale "Bearer"
    E o campo "token" está preenchido
    E o contrato "auth.json" é respeitado
