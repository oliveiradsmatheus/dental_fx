package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import matheus.bcc.dentalfx.db.entidades.Material;
import matheus.bcc.dentalfx.db.repositorios.MaterialDAL;
import matheus.bcc.dentalfx.util.Erro;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MaterialFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_descricao;
    public TextField tf_preco;
    public Button bt_confirmar;
    public Button bt_cancelar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (MaterialTableController.material != null) {
            Material material = MaterialTableController.material;
            tf_descricao.setText(material.getDescricao());
            tf_preco.setText("" + material.getPreco());
            tf_id.setText("" + material.getId());
        }

        Platform.runLater(() -> tf_descricao.requestFocus());
        MaskFieldUtil.monetaryField(tf_preco);
    }

    public void onConfirmar(ActionEvent actionEvent) {
        Material material = new Material(tf_descricao.getText(), Double.parseDouble(tf_preco.getText().replace(",",".")));
        MaterialDAL dal = new MaterialDAL();
        if (!tf_id.getText().isEmpty()) {
            material.setId(Integer.parseInt(tf_id.getText()));
            if (!dal.alterar(material))
                Erro.exibir("Erro ao alterar o material!");
        } else if (!dal.gravar(material))
            Erro.exibir("Erro ao gravar o material!");
        tf_preco.getScene().getWindow().hide();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_preco.getScene().getWindow().hide();
    }
}
