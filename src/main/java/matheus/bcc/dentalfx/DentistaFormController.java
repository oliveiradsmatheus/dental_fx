package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import matheus.bcc.dentalfx.util.Erro;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DentistaFormController implements Initializable {

    public TextField tf_id;
    public TextField tf_nome;
    public TextField tf_cro;
    public TextField tf_fone;
    public TextField tf_email;
    public Button bt_confirmar;
    public Button bt_cancelar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DentistaTableController.dentista != null) {
            Dentista dentista = DentistaTableController.dentista;
            tf_id.setText("" + dentista.getId());
            tf_nome.setText(dentista.getNome());
            tf_cro.setText("" + dentista.getCro());
            tf_fone.setText(dentista.getFone());
            tf_email.setText(dentista.getEmail());
        }
        Platform.runLater(() -> tf_nome.requestFocus());
        MaskFieldUtil.foneField(tf_fone);
    }

    public void onConfirmar(ActionEvent actionEvent) {
        Dentista dentista = new Dentista(
                tf_nome.getText(),
                Integer.parseInt(tf_cro.getText()),
                tf_fone.getText(),
                tf_email.getText()
        );
        PessoaDAL dal = new PessoaDAL();
        if (!tf_id.getText().isEmpty()) {
            dentista.setId(Integer.parseInt(tf_id.getText()));
            if (!dal.alterar(dentista))
                Erro.exibir("Erro ao alterar o dentista!\n" + SingletonDB.getConexao().getMensagemErro());
        } else if (!dal.gravar(dentista))
            Erro.exibir("Erro ao gravar o dentista!\n" + SingletonDB.getConexao().getMensagemErro());
        tf_id.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}
