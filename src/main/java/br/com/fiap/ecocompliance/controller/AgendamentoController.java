package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AgendamentoRequestDTO;
import br.com.fiap.ecocompliance.dto.AgendamentoResponseDTO;
import br.com.fiap.ecocompliance.entity.StatusAgendamento;
import br.com.fiap.ecocompliance.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Agendamentos", description = "Planos de redução de carbono. Status: PENDENTE, EM_ANDAMENTO, CONCLUIDO ou CANCELADO. CONCLUIDO e CANCELADO não podem ser editados.")
@RestController
@RequestMapping("/agendamentos-reducao")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @Operation(summary = "Listar agendamentos", description = "Página de planos de redução. Padrão: 20 itens, ordenados por id.")
    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(agendamentoService.listar(pageable));
    }

    @Operation(summary = "Buscar agendamento por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @Operation(summary = "Listar agendamentos da empresa", description = "Todos os planos de uma empresa, sem paginação.")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(agendamentoService.buscarPorEmpresa(empresaId));
    }

    @Operation(summary = "Listar agendamentos por status", description = "Filtra por PENDENTE, EM_ANDAMENTO, CONCLUIDO ou CANCELADO.")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AgendamentoResponseDTO>> buscarPorStatus(@PathVariable StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.buscarPorStatus(status));
    }

    @Operation(summary = "Cadastrar agendamento", description = "Exige ADMIN. Responde 201 com Location. A data fim precisa ser posterior à data início.")
    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        AgendamentoResponseDTO criado = agendamentoService.criar(dto);
        return ResponseEntity.created(URI.create("/agendamentos-reducao/" + criado.id())).body(criado);
    }

    @Operation(summary = "Atualizar agendamento", description = "Exige ADMIN. Status CONCLUIDO ou CANCELADO responde 400 e não é alterado.")
    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(@PathVariable Long id,
                                                            @RequestBody @Valid AgendamentoRequestDTO dto) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir agendamento", description = "Exige ADMIN. Responde 204. Agendamento não é removido em cascata com a empresa.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        agendamentoService.excluir(id);
    }
}
