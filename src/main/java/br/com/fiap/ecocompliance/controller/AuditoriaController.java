package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.AuditoriaRequestDTO;
import br.com.fiap.ecocompliance.dto.AuditoriaResponseDTO;
import br.com.fiap.ecocompliance.entity.NivelRisco;
import br.com.fiap.ecocompliance.service.AuditoriaService;
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

@Tag(name = "Auditorias", description = "Auditorias ambientais. O risco é BAIXO, MEDIO ou ALTO.")
@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(summary = "Listar auditorias", description = "Página de auditorias. Padrão: 20 itens, ordenados por id.")
    @GetMapping
    public ResponseEntity<Page<AuditoriaResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.listar(pageable));
    }

    @Operation(summary = "Buscar auditoria por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarPorId(id));
    }

    @Operation(summary = "Listar auditorias por risco", description = "Filtra por BAIXO, MEDIO ou ALTO. Um valor fora do enum responde 400.")
    @GetMapping("/risco/{nivelRisco}")
    public ResponseEntity<List<AuditoriaResponseDTO>> buscarPorRisco(@PathVariable NivelRisco nivelRisco) {
        return ResponseEntity.ok(auditoriaService.buscarPorRisco(nivelRisco));
    }

    @Operation(summary = "Cadastrar auditoria", description = "Exige ADMIN. Responde 201 com Location. Data futura responde 400.")
    @PostMapping
    public ResponseEntity<AuditoriaResponseDTO> criar(@RequestBody @Valid AuditoriaRequestDTO dto) {
        AuditoriaResponseDTO criada = auditoriaService.criar(dto);
        return ResponseEntity.created(URI.create("/auditorias/" + criada.id())).body(criada);
    }

    @Operation(summary = "Atualizar auditoria", description = "Exige ADMIN. Substitui data, resultado, observações e risco.")
    @PutMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> atualizar(@PathVariable Long id,
                                                           @RequestBody @Valid AuditoriaRequestDTO dto) {
        return ResponseEntity.ok(auditoriaService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir auditoria", description = "Exige ADMIN. Responde 204.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        auditoriaService.excluir(id);
    }
}
