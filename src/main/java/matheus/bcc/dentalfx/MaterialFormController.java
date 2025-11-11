package matheus.bcc.dentalfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import matheus.bcc.dentalfx.db.entidades.Material;
import matheus.bcc.dentalfx.db.repositorios.MaterialDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MaterialFormController implements Initializable {
    public TextField tf_id;
    public TextField tf_descricao;
    public TextField tf_preco;

    private Material material;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarFormulario();
    }

    private void configurarFormulario() {
        Platform.runLater(() -> tf_descricao.requestFocus());
        MaskFieldUtil.monetaryField(tf_preco);
    }

    public void carregarMaterial(Material material) {
        this.material = material;
        if (this.material != null)
            preencherFormulario();
    }

    private void preencherFormulario() {
        tf_descricao.setText(material.getDescricao());
        tf_preco.setText("" + material.getPreco());
        tf_id.setText("" + material.getId());
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            Material material = construirMaterial();
            if (salvarMaterial(material)) {
                Alerta.exibirAlerta("Sucesso", "Material salvo com sucesso.");
                tf_id.getScene().getWindow().hide();
            } else
                Alerta.exibirErro("Erro", "Não foi possível salvar o material.");
        } else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos.");
    }

    private Material construirMaterial() {
        Material material = new Material(
                tf_descricao.getText(),
                Double.parseDouble(tf_preco.getText().replace(",","."))
        );

        if (!tf_id.getText().isEmpty())
            material.setId(Integer.parseInt(tf_id.getText()));

        return material;
    }

    private boolean salvarMaterial(Material material) {
        MaterialDAL dal = new MaterialDAL();
        boolean sucesso;

        if (material.getId() > 0) {
            sucesso = dal.alterar(material);
            if (!sucesso)
                Alerta.exibirErro("Erro ao alterar", "Erro ao alterar o material!");
        } else {
            sucesso = dal.gravar(material);
            if (!sucesso)
                Alerta.exibirErro("Erro ao gravar", "Erro ao gravar o material!");
        }

        return sucesso;
    }

    private boolean validarCampos() {
        return !tf_descricao.getText().isEmpty() && !tf_preco.getText().isEmpty();
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_preco.getScene().getWindow().hide();
    }
}
