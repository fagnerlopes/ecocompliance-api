# language: pt
@regressivo
Funcionalidade: Contrato HTTP das APIs
  Cada cenário envia uma requisição real para a API no ar.
  A resposta é conferida pelo status, por um campo do JSON e pelo JSON Schema.

  Cenário: Saúde da API
    Quando eu listo "/up"
    Então a API responde 200
    E o campo "status" vale "up"
    E o contrato "up.json" é respeitado

  Cenário: Ambiente publicado
    Quando eu listo "/ambiente"
    Então a API responde 200
    E o campo "aplicacao" vale "ecocompliance"
    E o contrato "ambiente.json" é respeitado

  Cenário: Recusar login sem senha
    Quando eu entro sem senha
    Então a API responde 400
    E o contrato "erro.json" é respeitado

  Cenário: Cadastrar usuário e receber o token
    Quando eu cadastro um usuário
    Então a API responde 201
    E o campo "tipo" vale "Bearer"
    E o contrato "auth.json" é respeitado

  Cenário: Contrato das empresas
    Dado um administrador autenticado
    Quando ele cadastra uma empresa válida
    Então a API responde 201
    E o contrato "empresa.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "empresa.json" é respeitado
    Quando eu listo "/empresas"
    Então a API responde 200
    E a página respeita o contrato "empresa.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "empresa.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato das licenças
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro uma licença que vence em 15 dias
    Então a API responde 201
    E o contrato "licenca.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "licenca.json" é respeitado
    Quando eu listo "/licencas"
    Então a API responde 200
    E a página respeita o contrato "licenca.json"
    Quando eu listo "/licencas/vencendo"
    Então a API responde 200
    E a lista respeita o contrato "licenca.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "licenca.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato das auditorias
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro uma auditoria
    Então a API responde 201
    E o campo "nivelRisco" vale "ALTO"
    E o contrato "auditoria.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "auditoria.json" é respeitado
    Quando eu listo "/auditorias"
    Então a API responde 200
    E a página respeita o contrato "auditoria.json"
    Quando eu listo "/auditorias/risco/ALTO"
    Então a API responde 200
    E a lista respeita o contrato "auditoria.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "auditoria.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato das emissões
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro uma emissão
    Então a API responde 201
    E o campo "unidadeMedida" vale "tCO2e"
    E o contrato "emissao.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "emissao.json" é respeitado
    Quando eu listo "/emissoes"
    Então a API responde 200
    E a página respeita o contrato "emissao.json"
    Quando eu listo "/emissoes/empresa/{empresaId}"
    Então a API responde 200
    E a lista respeita o contrato "emissao.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "emissao.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato das compensações
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro uma compensação
    Então a API responde 201
    E o campo "tipoCompensacao" vale "Reflorestamento"
    E o contrato "compensacao.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "compensacao.json" é respeitado
    Quando eu listo "/compensacoes"
    Então a API responde 200
    E a página respeita o contrato "compensacao.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "compensacao.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato dos agendamentos de redução
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro um agendamento
    Então a API responde 201
    E o campo "status" vale "PENDENTE"
    E o contrato "agendamento.json" é respeitado
    Quando eu consulto o recurso cadastrado
    Então a API responde 200
    E o contrato "agendamento.json" é respeitado
    Quando eu listo "/agendamentos-reducao"
    Então a API responde 200
    E a página respeita o contrato "agendamento.json"
    Quando eu listo "/agendamentos-reducao/empresa/{empresaId}"
    Então a API responde 200
    E a lista respeita o contrato "agendamento.json"
    Quando eu listo "/agendamentos-reducao/status/PENDENTE"
    Então a API responde 200
    E a lista respeita o contrato "agendamento.json"
    Quando eu atualizo o recurso cadastrado
    Então a API responde 200
    E o contrato "agendamento.json" é respeitado
    Quando eu excluo o recurso cadastrado
    Então a API responde 204
    E o corpo vem vazio

  Cenário: Contrato do relatório da empresa
    Dado um administrador autenticado
    E uma empresa cadastrada
    Quando eu cadastro uma auditoria
    Então a API responde 201
    Quando eu gero o relatório da empresa
    Então a API responde 200
    E o campo "maiorNivelRisco" vale "ALTO"
    E o contrato "relatorio.json" é respeitado

  Cenário: Contrato dos indicadores de sustentabilidade
    Dado um administrador autenticado
    Quando eu consulto os indicadores
    Então a API responde 200
    E o campo "totalEmpresas" está preenchido
    E o contrato "indicadores.json" é respeitado
