package br.com.fiap.ecocompliance.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmpresaTest {

    @Test
    @DisplayName("prePersist deve preencher a data de criação quando ela está vazia")
    void prePersist_quandoCreatedAtNulo_preencheAgora() {
        Empresa empresa = new Empresa();

        empresa.prePersist();

        assertThat(empresa.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("prePersist deve preservar a data de criação já informada")
    void prePersist_quandoCreatedAtInformado_preserva() {
        LocalDateTime criadaEm = LocalDateTime.of(2025, 1, 15, 10, 0);
        Empresa empresa = Empresa.builder().createdAt(criadaEm).build();

        empresa.prePersist();

        assertThat(empresa.getCreatedAt()).isEqualTo(criadaEm);
    }
}
