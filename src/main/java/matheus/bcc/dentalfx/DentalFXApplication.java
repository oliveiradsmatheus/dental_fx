package matheus.bcc.dentalfx;

import javafx.application.Application;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.entidades.Usuario;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.util.Inicializador;
import matheus.bcc.dentalfx.util.*;

public class DentalFXApplication extends Application {
    @Override
    public void start(Stage stage) {
        if (Inicializador.verificarConexao()) {
            boolean sucesso = LoginPanel.exibir();
            if (sucesso) {
                AgendaDAL agendaDAL = new AgendaDAL();
                agendaDAL.limparConsultas();

                Sessao sessao = Sessao.getInstancia();
                Usuario usuario = sessao.getUsuario();
                switch (usuario.getNivel()) {
                    case 1: case 2:
                        GerenciadorTelas.carregar(stage, Constantes.TELA_AGENDAMENTO, Constantes.TITULO_APP, "icone", false);
                        break;
                    case 3:
                        GerenciadorTelas.carregar(stage, Constantes.TELA_DENTISTA, Constantes.TITULO_APP, "icone", false);
                        break;
                    default:
                        Alerta.exibirErro("Erro de Login", "Nível de usuário inválido: " + usuario.getNivel());
                }
            }
        }
    }

}
