package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import matheus.bcc.dentalfx.db.entidades.Procedimento;
import matheus.bcc.dentalfx.db.repositorios.ProcedimentoDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcedimentoFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_descricao;
    public TextField tf_valor;
    public Slider sl_tempo;
    public Label lb_tempo;

    private Procedimento procedimento;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarFormulario();
    }

    private void configurarFormulario() {
        lb_tempo.textProperty().bind(sl_tempo.valueProperty().asString("%.0f")); // Vincula o slider com o label
        Platform.runLater(() -> tf_descricao.requestFocus());
        MaskFieldUtil.monetaryField(tf_valor);
    }

    public void carregarProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
        if (this.procedimento != null)
            preencherFormulario();
    }

    private void preencherFormulario() {
        tf_descricao.setText(procedimento.getDescricao());
        tf_valor.setText("" + procedimento.getValor());
        tf_id.setText("" + procedimento.getId());
        sl_tempo.setValue(procedimento.getTempo());
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            Procedimento procedimento = construirProcedimento();
            if (salvarProcedimento(procedimento)) {
                Alerta.exibirAlerta("Sucesso", "Procedimento salvo com sucesso.");
                tf_id.getScene().getWindow().hide();
            } else
                Alerta.exibirErro("Erro", "Não foi possível salvar o procedimento.");
        } else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos.");
    }

    private Procedimento construirProcedimento() {
        Procedimento procedimento = new Procedimento(
                tf_descricao.getText(),
                sl_tempo.getValue(),
                Double.parseDouble(tf_valor.getText().replace(",","."))
        );

        if (!tf_id.getText().isEmpty())
            procedimento.setId(Integer.parseInt(tf_id.getText()));

        return procedimento;
    }

    private boolean salvarProcedimento(Procedimento procedimento) {
        ProcedimentoDAL dal = new ProcedimentoDAL();
        boolean sucesso;

        if (procedimento.getId() > 0) {
            sucesso = dal.alterar(procedimento);
            if (!sucesso)
                Alerta.exibirErro("Erro ao alterar", "Erro ao alterar o procedimento!");
        } else {
            sucesso = dal.gravar(procedimento);
            if (!sucesso)
                Alerta.exibirErro("Erro ao gravar", "Erro ao gravar o procedimento!");
        }

        return sucesso;
    }

    private boolean validarCampos() {
        return !tf_descricao.getText().isEmpty() && !tf_valor.getText().isEmpty();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_valor.getScene().getWindow().hide();
    }

    public void onAlterar(MouseEvent mouseEvent) {
        lb_tempo.setText("" + sl_tempo.getValue());
    }
}
