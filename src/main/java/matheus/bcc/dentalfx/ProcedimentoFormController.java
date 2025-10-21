package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import matheus.bcc.dentalfx.db.entidades.Procedimento;
import matheus.bcc.dentalfx.db.repositorios.ProcedimentoDAL;
import matheus.bcc.dentalfx.util.Erro;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcedimentoFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_descricao;
    public TextField tf_valor;
    public Slider sl_tempo;
    public Button bt_confirmar;
    public Button bt_cancelar;
    public Label lb_tempo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (ProcedimentoTableController.procedimento != null) {
            Procedimento procedimento=ProcedimentoTableController.procedimento;
            tf_descricao.setText(procedimento.getDescricao());
            tf_valor.setText("" + procedimento.getValor());
            tf_id.setText("" + procedimento.getId());
            sl_tempo.setValue(procedimento.getTempo());
            lb_tempo.setText("" + procedimento.getTempo());
        }
        lb_tempo.textProperty().bind(sl_tempo.valueProperty().asString("%.0f")); // Vincula o slider com o label

        Platform.runLater(() -> tf_descricao.requestFocus());
        MaskFieldUtil.monetaryField(tf_valor);
    }

    public void onConfirmar(ActionEvent actionEvent) {
        Procedimento procedimento = new Procedimento(tf_descricao.getText(), sl_tempo.getValue(), Double.parseDouble(tf_valor.getText().replace(",",".")));
        ProcedimentoDAL dal = new ProcedimentoDAL();
        if (!tf_id.getText().isEmpty()) {
            procedimento.setId(Integer.parseInt(tf_id.getText()));
            if (!dal.alterar(procedimento))
                Erro.exibir("Erro ao alterar o procedimento!");
        } else if (!dal.gravar(procedimento))
            Erro.exibir("Erro ao gravar o procedimento!");
        tf_valor.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_valor.getScene().getWindow().hide();
    }

    public void onAlterar(MouseEvent mouseEvent) {
        lb_tempo.setText("" + sl_tempo.getValue());
    }
}
