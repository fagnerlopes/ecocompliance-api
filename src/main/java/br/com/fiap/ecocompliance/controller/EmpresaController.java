package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.EmpresaRequestDTO;
import br.com.fiap.ecocompliance.dto.EmpresaResponseDTO;
import br.com.fiap.ecocompliance.service.EmpresaService;
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

@Tag(name = "Empresas", description = "Cadastro raiz. Licenças, auditorias, emissões e compensações apontam para a empresa.")
@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @Operation(summary = "Listar empresas", description = "Devolve uma página de empresas. Padrão: 20 itens, ordenados por id. Exige USER ou ADMIN.")
    @GetMapping
    public ResponseEntity<Page<EmpresaResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(empresaService.listar(pageable));
    }

    @Operation(summary = "Buscar empresa por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.buscarPorId(id));
    }

    @Operation(summary = "Cadastrar empresa", description = "Exige ADMIN. Responde 201 com o cabeçalho Location. CNPJ repetido responde 409.")
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criar(@RequestBody @Valid EmpresaRequestDTO dto) {
        EmpresaResponseDTO criada = empresaService.criar(dto);
        return ResponseEntity.created(URI.create("/empresas/" + criada.id())).body(criada);
    }

    @Operation(summary = "Atualizar empresa", description = "Exige ADMIN. Substitui nome, CNPJ, setor e e-mail.")
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid EmpresaRequestDTO dto) {
        return ResponseEntity.ok(empresaService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir empresa", description = "Exige ADMIN. Responde 204. A exclusão em cascata remove licenças, auditorias, emissões e compensações. Agendamento vinculado impede a exclusão e responde 409.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        empresaService.excluir(id);
    }
}
