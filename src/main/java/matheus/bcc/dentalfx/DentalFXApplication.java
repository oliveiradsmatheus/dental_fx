package matheus.bcc.dentalfx;

import javafx.application.Application;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;
import matheus.bcc.dentalfx.util.Inicializador;

public class DentalFXApplication extends Application {
    @Override
    public void start(Stage stage) {
        if (Inicializador.verificarConexao())
            GerenciadorTelas.carregar(stage, Constantes.MENU, Constantes.TITULO_APP, "icone", false);
    }
}
