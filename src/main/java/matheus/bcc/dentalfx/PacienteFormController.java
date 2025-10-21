package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import matheus.bcc.dentalfx.db.entidades.Paciente;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.util.CEP;
import matheus.bcc.dentalfx.util.Erro;
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
    public Button bt_confirmar;
    public Button bt_cancelar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (PacienteTableController.paciente != null) {
            Paciente paciente = PacienteTableController.paciente;
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
        Platform.runLater(() -> tf_nome.requestFocus());
        MaskFieldUtil.cepField(tf_cep);
        MaskFieldUtil.cpfField(tf_cpf);
        MaskFieldUtil.foneField(tf_fone);
    }

    public void onBuscarCep(KeyEvent keyEvent) {
        if(tf_cep.getText().length() == 9) {
            String json = CEP.consultar(tf_cep.getText(), "json");
            IO.print(json);
            JSONObject obj = new JSONObject(json);
            tf_cidade.setText(obj.getString("localidade"));
            tf_rua.setText(obj.getString("logradouro"));
            tf_bairro.setText(obj.getString("bairro"));
            tf_uf.setText(obj.getString("uf"));
            // finalizar demais informações
        }
    }

    public void onConfirmar(ActionEvent actionEvent) {
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
        PessoaDAL dal = new PessoaDAL();
        if (!tf_id.getText().isEmpty()) {
            paciente.setId(Integer.parseInt(tf_id.getText()));
            if (!dal.alterar(paciente))
                Erro.exibir("Erro ao alterar o paciente!");
        } else if (!dal.gravar(paciente))
            Erro.exibir("Erro ao gravar o paciente!");
        tf_id.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_id.getScene().getWindow().hide();
    }
}
