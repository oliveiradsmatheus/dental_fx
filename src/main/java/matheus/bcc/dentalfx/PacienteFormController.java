package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import matheus.bcc.dentalfx.db.entidades.Paciente;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.util.CEP;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.MaskFieldUtil;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class PacienteFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_nome;
    public TextField tf_cpf;
    public TextField tf_cep;
    public TextField tf_rua;
    public TextField tf_numero;
    public TextField tf_bairro;
    public TextField tf_cidade;
    public TextField tf_uf;
    public TextField tf_fone;
    public TextField tf_email;
    public TextField tf_historico;

    private Paciente paciente;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarFormulario();
    }

    private void configurarFormulario() {
        Platform.runLater(() -> tf_nome.requestFocus());
        MaskFieldUtil.cepField(tf_cep);
        MaskFieldUtil.cpfField(tf_cpf);
        MaskFieldUtil.foneField(tf_fone);
    }

    public void carregarPaciente(Paciente paciente) {
        this.paciente = paciente;
        if (this.paciente != null)
            preencherFormulario();
    }

    private void preencherFormulario() {
        tf_id.setText("" + paciente.getId());
        tf_nome.setText(paciente.getNome());
        tf_cpf.setText(paciente.getCpf());
        tf_cep.setText(paciente.getCep());
        tf_rua.setText(paciente.getRua());
        tf_numero.setText(paciente.getNumero());
        tf_bairro.setText(paciente.getBairro());
        tf_cidade.setText(paciente.getCidade());
        tf_uf.setText(paciente.getUf());
        tf_fone.setText(paciente.getFone());
        tf_email.setText(paciente.getEmail());
        tf_historico.setText(paciente.getHistorico());
    }

    public void onBuscarCep(KeyEvent keyEvent) {
        if(tf_cep.getText().length() == 9) {
            String json = CEP.consultar(tf_cep.getText(), "json");
            JSONObject obj = new JSONObject(json);
            tf_cidade.setText(obj.getString("localidade"));
            tf_rua.setText(obj.getString("logradouro"));
            tf_bairro.setText(obj.getString("bairro"));
            tf_uf.setText(obj.getString("uf"));
        }
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            Paciente paciente = construirPaciente();
            if (salvarPaciente(paciente)) {
                Alerta.exibirAlerta("Sucesso", "Paciente salvo com sucesso.");
                tf_id.getScene().getWindow().hide();
            } else
                Alerta.exibirErro("Erro", "Não foi possível salvar o paciente.");
        } else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos.");
    }

    private Paciente construirPaciente() {
        Paciente paciente = new Paciente(
                tf_nome.getText(),
                tf_cpf.getText(),
                tf_cep.getText(),
                tf_rua.getText(),
                tf_numero.getText(),
                tf_bairro.getText(),
                tf_cidade.getText(),
                tf_uf.getText(),
                tf_fone.getText(),
                tf_email.getText(),
                tf_historico.getText()
        );

        if (!tf_id.getText().isEmpty())
            paciente.setId(Integer.parseInt(tf_id.getText()));

        return paciente;
    }

    private boolean salvarPaciente(Paciente paciente) {
        PessoaDAL dal = new PessoaDAL();
        boolean sucesso;

        if (paciente.getId() > 0) {
            sucesso = dal.alterar(paciente);
            if (!sucesso)
                Alerta.exibirErro("Erro ao alterar", "Erro ao alterar o paciente!");
        } else {
            sucesso = dal.gravar(paciente);
            if (!sucesso)
                Alerta.exibirErro("Erro ao gravar", "Erro ao gravar o paciente!");
        }

        return sucesso;
    }

    private boolean validarCampos() {
        return !tf_nome.getText().isEmpty() &&
                !tf_cpf.getText().isEmpty() &&
                !tf_cep.getText().isEmpty() &&
                !tf_rua.getText().isEmpty() &&
                !tf_numero.getText().isEmpty() &&
                !tf_bairro.getText().isEmpty() &&
                !tf_cidade.getText().isEmpty() &&
                !tf_uf.getText().isEmpty() &&
                !tf_fone.getText().isEmpty() &&
                !tf_email.getText().isEmpty();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}
