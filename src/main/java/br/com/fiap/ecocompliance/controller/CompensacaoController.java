package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.CompensacaoRequestDTO;
import br.com.fiap.ecocompliance.dto.CompensacaoResponseDTO;
import br.com.fiap.ecocompliance.service.CompensacaoService;
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

@Tag(name = "Compensações", description = "Ações de compensação ambiental: tipo, descrição, quantidade e data.")
@RestController
@RequestMapping("/compensacoes")
@RequiredArgsConstructor
public class CompensacaoController {

    private final CompensacaoService compensacaoService;

    @Operation(summary = "Listar compensações", description = "Página de compensações. Padrão: 20 itens, ordenados por id.")
    @GetMapping
    public ResponseEntity<Page<CompensacaoResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(compensacaoService.listar(pageable));
    }

    @Operation(summary = "Buscar compensação por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<CompensacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compensacaoService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar compensação", description = "Exige ADMIN. Responde 201 com Location.")
    @PostMapping
    public ResponseEntity<CompensacaoResponseDTO> criar(@RequestBody @Valid CompensacaoRequestDTO dto) {
        CompensacaoResponseDTO criada = compensacaoService.criar(dto);
        return ResponseEntity.created(URI.create("/compensacoes/" + criada.id())).body(criada);
    }

    @Operation(summary = "Atualizar compensação", description = "Exige ADMIN. Substitui tipo, descrição, quantidade e data.")
    @PutMapping("/{id}")
    public ResponseEntity<CompensacaoResponseDTO> atualizar(@PathVariable Long id,
                                                             @RequestBody @Valid CompensacaoRequestDTO dto) {
        return ResponseEntity.ok(compensacaoService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir compensação", description = "Exige ADMIN. Responde 204.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        compensacaoService.excluir(id);
    }
}
