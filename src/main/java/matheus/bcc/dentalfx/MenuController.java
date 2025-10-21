package matheus.bcc.dentalfx;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;

public class MenuController {
    public void onMaterial(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TELA_MATERIAL, "Materiais", "icone", true);
    }

    public void onProcedimento(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TELA_PROCEDIMENTO, "Materiais", "icone", true);
    }

    public void onUsuario(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TELA_USUARIO, "Materiais", "icone", true);
    }

    public void onDentista(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TELA_DENTISTA, "Materiais", "icone", true);
    }

    public void onPaciente(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TELA_PACIENTE, "Materiais", "icone", true);
    }
}
