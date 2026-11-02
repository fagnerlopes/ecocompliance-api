package br.com.fiap.ecocompliance.bdd;

import br.com.fiap.ecocompliance.config.SecurityConfig;
import br.com.fiap.ecocompliance.controller.AgendamentoController;
import br.com.fiap.ecocompliance.controller.AmbienteController;
import br.com.fiap.ecocompliance.controller.AuditoriaController;
import br.com.fiap.ecocompliance.controller.AuthController;
import br.com.fiap.ecocompliance.controller.CompensacaoController;
import br.com.fiap.ecocompliance.controller.EmissaoController;
import br.com.fiap.ecocompliance.controller.EmpresaController;
import br.com.fiap.ecocompliance.controller.IndicadoresController;
import br.com.fiap.ecocompliance.controller.LicencaController;
import br.com.fiap.ecocompliance.controller.RelatorioController;
import br.com.fiap.ecocompliance.controller.UpController;
import br.com.fiap.ecocompliance.repository.UsuarioRepository;
import br.com.fiap.ecocompliance.security.JwtService;
import br.com.fiap.ecocompliance.service.AgendamentoService;
import br.com.fiap.ecocompliance.service.AuditoriaService;
import br.com.fiap.ecocompliance.service.AuthService;
import br.com.fiap.ecocompliance.service.CompensacaoService;
import br.com.fiap.ecocompliance.service.EmissaoService;
import br.com.fiap.ecocompliance.service.EmpresaService;
import br.com.fiap.ecocompliance.service.IndicadoresService;
import br.com.fiap.ecocompliance.service.LicencaService;
import br.com.fiap.ecocompliance.service.RelatorioService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@CucumberContextConfiguration
@WebMvcTest(controllers = {
        EmpresaController.class,
        LicencaController.class,
        AuditoriaController.class,
        EmissaoController.class,
        CompensacaoController.class,
        AgendamentoController.class,
        AuthController.class,
        AmbienteController.class,
        UpController.class,
        IndicadoresController.class,
        RelatorioController.class
})
@Import(SecurityConfig.class)
public class ConfiguracaoCucumber {

    @MockBean
    EmpresaService empresaService;

    @MockBean
    LicencaService licencaService;

    @MockBean
    AuditoriaService auditoriaService;

    @MockBean
    EmissaoService emissaoService;

    @MockBean
    CompensacaoService compensacaoService;

    @MockBean
    AgendamentoService agendamentoService;

    @MockBean
    AuthService authService;

    @MockBean
    IndicadoresService indicadoresService;

    @MockBean
    RelatorioService relatorioService;

    @MockBean
    JwtService jwtService;

    @MockBean
    UsuarioRepository usuarioRepository;
}
