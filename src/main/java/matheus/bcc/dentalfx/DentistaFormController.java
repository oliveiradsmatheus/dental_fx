package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DentistaFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_nome;
    public TextField tf_cro;
    public TextField tf_fone;
    public TextField tf_email;

    private Dentista dentista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarFormulario();
    }

    private void configurarFormulario() {
        Platform.runLater(() -> tf_nome.requestFocus());
        MaskFieldUtil.foneField(tf_fone);
        MaskFieldUtil.numericField(tf_cro);
        MaskFieldUtil.maxField(tf_cro, 5);
    }

    public void carregarDentista(Dentista dentista) {
        this.dentista = dentista;
        if (this.dentista != null)
            preencherFormulario();
    }

    private void preencherFormulario() {
        tf_id.setText("" + dentista.getId());
        tf_nome.setText(dentista.getNome());
        tf_cro.setText("" + dentista.getCro());
        tf_fone.setText(dentista.getFone());
        tf_email.setText(dentista.getEmail());
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            Dentista dentista = construirDentista();
            if (salvarDentista(dentista)) {
                Alerta.exibirAlerta("Sucesso", "Dentista salvo com sucesso.");
                tf_id.getScene().getWindow().hide();
            } else
                Alerta.exibirErro("Erro", "Não foi possível salvar o dentista.");
        } else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos.");
    }

    private Dentista construirDentista() {
        Dentista dentista = new Dentista(
                tf_nome.getText(),
                Integer.parseInt(tf_cro.getText()),
                tf_fone.getText(),
                tf_email.getText()
        );

        if (!tf_id.getText().isEmpty())
            dentista.setId(Integer.parseInt(tf_id.getText()));

        return dentista;
    }

    private boolean salvarDentista(Dentista dentista) {
        PessoaDAL dal = new PessoaDAL();
        boolean sucesso;

        if (dentista.getId() > 0) {
            sucesso = dal.alterar(dentista);
            if (!sucesso)
                Alerta.exibirErro("Erro ao alterar", "Erro ao alterar o dentista!");
        } else {
            sucesso = dal.gravar(dentista);
            if (!sucesso)
                Alerta.exibirErro("Erro ao gravar", "Erro ao gravar o dentista!");
        }

        return sucesso;
    }

    private boolean validarCampos() {
        return !tf_nome.getText().isEmpty() && !tf_cro.getText().isEmpty() && !tf_fone.getText().isEmpty() && !tf_email.getText().isEmpty();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}
