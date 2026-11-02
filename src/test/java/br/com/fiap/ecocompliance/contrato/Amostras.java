package br.com.fiap.ecocompliance.contrato;

import br.com.fiap.ecocompliance.dto.AgendamentoResponseDTO;
import br.com.fiap.ecocompliance.dto.AuditoriaResponseDTO;
import br.com.fiap.ecocompliance.dto.AuthResponseDTO;
import br.com.fiap.ecocompliance.dto.CompensacaoResponseDTO;
import br.com.fiap.ecocompliance.dto.EmissaoResponseDTO;
import br.com.fiap.ecocompliance.dto.EmpresaResponseDTO;
import br.com.fiap.ecocompliance.dto.IndicadoresSustentabilidadeDTO;
import br.com.fiap.ecocompliance.dto.LicencaResponseDTO;
import br.com.fiap.ecocompliance.dto.RelatorioEmpresaDTO;
import br.com.fiap.ecocompliance.entity.NivelRisco;
import br.com.fiap.ecocompliance.entity.Role;
import br.com.fiap.ecocompliance.entity.StatusAgendamento;
import br.com.fiap.ecocompliance.entity.StatusLicenca;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Amostras {

    public static final LocalDateTime CRIADO_EM = LocalDateTime.of(2026, 10, 10, 18, 30);
    public static final String EMPRESA = "Verde Norte Industria Ltda";

    private Amostras() {
    }

    public static EmpresaResponseDTO empresa() {
        return new EmpresaResponseDTO(
                1L, EMPRESA, "12.345.678/0001-90", "Mineração", "contato@verdenorte.com.br", CRIADO_EM);
    }

    public static LicencaResponseDTO licenca() {
        return new LicencaResponseDTO(
                10L, 1L, EMPRESA, "Licença de Operação", "LO-2026-0042",
                LocalDate.of(2026, 1, 15), LocalDate.of(2027, 1, 15),
                StatusLicenca.ATIVA, "CETESB");
    }

    public static AuditoriaResponseDTO auditoria() {
        return new AuditoriaResponseDTO(
                4L, 1L, EMPRESA, LocalDate.of(2026, 3, 20),
                "Não conformidades no armazenamento de resíduos",
                "A área de triagem estava sem contenção.",
                NivelRisco.ALTO);
    }

    public static EmissaoResponseDTO emissao() {
        return new EmissaoResponseDTO(
                7L, 1L, EMPRESA, new BigDecimal("1280.50"),
                "Caldeira a gás natural", LocalDate.of(2026, 2, 28), "tCO2e");
    }

    public static CompensacaoResponseDTO compensacao() {
        return new CompensacaoResponseDTO(
                3L, 1L, EMPRESA, "Reflorestamento",
                "Plantio de 500 mudas nativas na APP do rio.",
                new BigDecimal("42.00"), LocalDate.of(2026, 4, 2));
    }

    public static AgendamentoResponseDTO agendamento() {
        return new AgendamentoResponseDTO(
                2L, 1L, EMPRESA, "Troca da frota a diesel",
                "Substituir 12 caminhões por veículos elétricos.",
                "Transporte", new BigDecimal("300.00"),
                LocalDate.of(2026, 11, 2), LocalDate.of(2027, 6, 30),
                StatusAgendamento.PENDENTE, CRIADO_EM);
    }

    public static AuthResponseDTO auth() {
        return AuthResponseDTO.of("eyJhbGciOiJIUzI1NiJ9.exemplo", "admin@ecocompliance.com", "Administrador", Role.ADMIN);
    }

    public static IndicadoresSustentabilidadeDTO indicadores() {
        return new IndicadoresSustentabilidadeDTO(
                4L, new BigDecimal("5400.00"), new BigDecimal("800.00"), new BigDecimal("4600.00"),
                6L, 2L, 9L, 3L, 1L, 2L);
    }

    public static RelatorioEmpresaDTO relatorio() {
        return new RelatorioEmpresaDTO(
                empresa(), new BigDecimal("1280.50"), new BigDecimal("42.00"),
                2L, 1L, 3L, NivelRisco.ALTO);
    }

    public static String empresaJson() {
        return """
                {"nome":"Verde Norte Industria Ltda","cnpj":"12.345.678/0001-90","setor":"Mineração","emailResponsavel":"contato@verdenorte.com.br"}
                """;
    }

    public static String licencaJson() {
        return """
                {"empresaId":1,"tipoLicenca":"Licença de Operação","numeroLicenca":"LO-2026-0042","dataEmissao":"%s","dataValidade":"%s","status":"ATIVA","orgaoEmissor":"CETESB"}
                """.formatted(LocalDate.now().minusDays(10), LocalDate.now().plusYears(1));
    }

    public static String auditoriaJson() {
        return """
                {"empresaId":1,"dataAuditoria":"%s","resultado":"Não conformidades no armazenamento de resíduos","observacoes":"A área de triagem estava sem contenção.","nivelRisco":"ALTO"}
                """.formatted(LocalDate.now().minusDays(1));
    }

    public static String emissaoJson() {
        return """
                {"empresaId":1,"quantidadeCo2":1280.50,"fonteEmissao":"Caldeira a gás natural","dataRegistro":"%s","unidadeMedida":"tCO2e"}
                """.formatted(LocalDate.now().minusDays(2));
    }

    public static String compensacaoJson() {
        return """
                {"empresaId":1,"tipoCompensacao":"Reflorestamento","descricao":"Plantio de 500 mudas nativas na APP do rio.","quantidadeCompensada":42.00,"dataAcao":"%s"}
                """.formatted(LocalDate.now().minusDays(2));
    }

    public static String agendamentoJson() {
        return """
                {"empresaId":1,"titulo":"Troca da frota a diesel","descricao":"Substituir 12 caminhões por veículos elétricos.","tipoIniciativa":"Transporte","metaCo2":300.00,"dataInicio":"%s","dataFim":"%s","status":"PENDENTE"}
                """.formatted(LocalDate.now().plusDays(1), LocalDate.now().plusDays(40));
    }

    public static String loginJson() {
        return """
                {"email":"admin@ecocompliance.com","senha":"admin123"}
                """;
    }

    public static String registerJson() {
        return """
                {"nome":"Administrador","email":"admin@ecocompliance.com","senha":"admin123","role":"ADMIN"}
                """;
    }
}
