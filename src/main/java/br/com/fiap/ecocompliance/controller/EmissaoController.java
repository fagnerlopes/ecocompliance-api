package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.EmissaoRequestDTO;
import br.com.fiap.ecocompliance.dto.EmissaoResponseDTO;
import br.com.fiap.ecocompliance.service.EmissaoService;
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

@Tag(name = "Emissões", description = "Emissões de carbono por empresa: quantidade, fonte, data e unidade.")
@RestController
@RequestMapping("/emissoes")
@RequiredArgsConstructor
public class EmissaoController {

    private final EmissaoService emissaoService;

    @Operation(summary = "Listar emissões", description = "Página de emissões. Padrão: 20 itens, ordenados por id.")
    @GetMapping
    public ResponseEntity<Page<EmissaoResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(emissaoService.listar(pageable));
    }

    @Operation(summary = "Buscar emissão por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<EmissaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(emissaoService.buscarPorId(id));
    }

    @Operation(summary = "Listar emissões da empresa", description = "Todas as emissões de uma empresa, sem paginação. Empresa inexistente responde 404.")
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<EmissaoResponseDTO>> buscarPorEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(emissaoService.buscarPorEmpresa(empresaId));
    }

    @Operation(summary = "Cadastrar emissão", description = "Exige ADMIN. Responde 201 com Location. Quantidade precisa ser positiva.")
    @PostMapping
    public ResponseEntity<EmissaoResponseDTO> criar(@RequestBody @Valid EmissaoRequestDTO dto) {
        EmissaoResponseDTO criada = emissaoService.criar(dto);
        return ResponseEntity.created(URI.create("/emissoes/" + criada.id())).body(criada);
    }

    @Operation(summary = "Atualizar emissão", description = "Exige ADMIN. Substitui quantidade, fonte, data e unidade.")
    @PutMapping("/{id}")
    public ResponseEntity<EmissaoResponseDTO> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid EmissaoRequestDTO dto) {
        return ResponseEntity.ok(emissaoService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir emissão", description = "Exige ADMIN. Responde 204.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        emissaoService.excluir(id);
    }
}
