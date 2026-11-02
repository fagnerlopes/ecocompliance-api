package hook;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import model.Contexto;

public class Hooks {

    @Before
    public void preparar(Scenario cenario) {
        Contexto.limpar();
        System.out.println("Iniciando cenário: " + cenario.getName());
    }

    @After
    public void encerrar(Scenario cenario) {
        System.out.println("Finalizando cenário: " + cenario.getName() + " [" + cenario.getStatus() + "]");
    }
}
