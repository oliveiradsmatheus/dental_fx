package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import matheus.bcc.dentalfx.db.entidades.Usuario;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Erro;

import java.net.URL;
import java.util.ResourceBundle;

public class UsuarioFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_nome;
    public TextField tf_nivel;
    public TextField tf_senha;
    public Button bt_confirmar;
    public Button bt_cancelar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (UsuarioTableController.usuario != null) {
            Usuario usuario = UsuarioTableController.usuario;
            tf_id.setText("" + usuario.getId());
            tf_nome.setText(usuario.getNome());
            tf_nivel.setText("" + usuario.getNivel());
            tf_senha.setText(usuario.getSenha());
        }
        Platform.runLater(() -> tf_nome.requestFocus());
    }

    public void onConfirmar(ActionEvent actionEvent) {
        Usuario usuario = new Usuario(
                tf_nome.getText(),
                Integer.parseInt(tf_nivel.getText()),
                tf_senha.getText()
        );
        PessoaDAL dal = new PessoaDAL();
        if(!tf_id.getText().isEmpty()) {
            usuario.setId(Integer.parseInt(tf_id.getText()));
            if (!dal.alterar(usuario))
                Erro.exibir("Erro ao alterar o usuário!");
        } else if (!dal.gravar(usuario))
            Erro.exibir("Erro ao gravar o usuário!");
        tf_id.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}
