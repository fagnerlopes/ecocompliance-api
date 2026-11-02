# Testes automatizados — EcoCompliance

Projeto Maven separado da API. O Cucumber descreve o comportamento em Gherkin e o Rest Assured envia a requisição HTTP para a API que está no ar. O JSON Schema confere o contrato da resposta.

## Subir a API

Na raiz do repositório da EcoCompliance:

```bash
docker compose up --build -d
curl http://localhost:8080/up
```

O Compose sobe o Oracle e a API. O healthcheck do banco precisa ficar saudável antes da API responder.

## Executar os testes

```bash
cd testes-automatizados
chmod +x scripts/executar.sh
./scripts/executar.sh
```

O script espera `GET /up` e então roda `mvn clean test`. O `TestRunner` executa só os cenários com a tag `@regressivo`.

Outra base, se a API não estiver na porta 8080:

```bash
API_BASE_URL=http://localhost:8081 mvn clean test
```

O relatório HTML sai em `target/cucumber-reports.html`.

## O que a suíte cobre

`features/governanca.feature` tem os caminhos feliz e de falha do cadastro: 201, 400, 403, 404 e o login 200.

`features/contrato.feature` percorre as APIs de saúde, ambiente, autenticação, empresas, licenças, auditorias, emissões, compensações, agendamentos, relatório e indicadores. Cada resposta com corpo confere status, um campo do JSON e o schema em `src/test/resources/schemas/`. DELETE responde 204 sem corpo.

Usuários criados pela API na subida: `admin@ecocompliance.com` / `admin123` e `user@ecocompliance.com` / `user123`.

## Pipeline

O workflow `.github/workflows/testes-automatizados.yml` sobe o Compose, espera `/up`, roda `mvn clean test` e publica o HTML do Cucumber. O job de evidência só conclui se a suíte passar.
