package br.com.fiap.ecocompliance.controller;

import br.com.fiap.ecocompliance.dto.LicencaRequestDTO;
import br.com.fiap.ecocompliance.dto.LicencaResponseDTO;
import br.com.fiap.ecocompliance.service.LicencaService;
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

@Tag(name = "Licenças", description = "Licenças ambientais. Status: ATIVA, VENCIDA, SUSPENSA ou CANCELADA. A validade não pode ser anterior à emissão.")
@RestController
@RequestMapping("/licencas")
@RequiredArgsConstructor
public class LicencaController {

    private final LicencaService licencaService;

    @Operation(summary = "Listar licenças", description = "Página de licenças. Padrão: 20 itens, ordenados por id.")
    @GetMapping
    public ResponseEntity<Page<LicencaResponseDTO>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(licencaService.listar(pageable));
    }

    @Operation(summary = "Buscar licença por id", description = "Retorna 404 quando o id não existe.")
    @GetMapping("/{id}")
    public ResponseEntity<LicencaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(licencaService.buscarPorId(id));
    }

    @Operation(summary = "Listar licenças a vencer", description = "Licenças cuja validade cai nos próximos 30 dias. O status não é recalculado por esta consulta.")
    @GetMapping("/vencendo")
    public ResponseEntity<List<LicencaResponseDTO>> licencasVencendo() {
        return ResponseEntity.ok(licencaService.licencasVencendo());
    }

    @Operation(summary = "Cadastrar licença", description = "Exige ADMIN. Responde 201 com Location. Validade anterior à emissão responde 400. Empresa inexistente responde 404.")
    @PostMapping
    public ResponseEntity<LicencaResponseDTO> criar(@RequestBody @Valid LicencaRequestDTO dto) {
        LicencaResponseDTO criada = licencaService.criar(dto);
        return ResponseEntity.created(URI.create("/licencas/" + criada.id())).body(criada);
    }

    @Operation(summary = "Atualizar licença", description = "Exige ADMIN. Aplica as mesmas regras de data do cadastro.")
    @PutMapping("/{id}")
    public ResponseEntity<LicencaResponseDTO> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid LicencaRequestDTO dto) {
        return ResponseEntity.ok(licencaService.atualizar(id, dto));
    }

    @Operation(summary = "Excluir licença", description = "Exige ADMIN. Responde 204.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        licencaService.excluir(id);
    }
}
