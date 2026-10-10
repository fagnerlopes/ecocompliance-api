package br.com.fiap.ecocompliance.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AgendamentoReducaoCarbonoTest {

    @Test
    @DisplayName("prePersist deve preencher criação e status pendente quando vazios")
    void prePersist_quandoCamposVazios_preencheDefaults() throws Exception {
        AgendamentoReducaoCarbono agendamento = new AgendamentoReducaoCarbono();

        invocarPrePersist(agendamento);

        assertThat(agendamento.getDataCriacao()).isNotNull();
        assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.PENDENTE);
    }

    @Test
    @DisplayName("prePersist deve preservar criação e status já informados")
    void prePersist_quandoCamposInformados_preserva() throws Exception {
        LocalDateTime criadaEm = LocalDateTime.of(2026, 1, 10, 8, 0);
        AgendamentoReducaoCarbono agendamento = AgendamentoReducaoCarbono.builder()
                .dataCriacao(criadaEm)
                .status(StatusAgendamento.EM_ANDAMENTO)
                .build();

        invocarPrePersist(agendamento);

        assertThat(agendamento.getDataCriacao()).isEqualTo(criadaEm);
        assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.EM_ANDAMENTO);
    }

    private static void invocarPrePersist(AgendamentoReducaoCarbono agendamento) throws Exception {
        Method prePersist = AgendamentoReducaoCarbono.class.getDeclaredMethod("prePersist");
        prePersist.setAccessible(true);
        prePersist.invoke(agendamento);
    }
}
