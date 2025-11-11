package matheus.bcc.dentalfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.entidades.Material;
import matheus.bcc.dentalfx.db.repositorios.MaterialDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MaterialTableController implements Initializable {
    public TableColumn<Material, Integer> col_id;
    public TableColumn<Material, String> col_desc;
    public TableColumn<Material, Double> col_preco;
    public TextField tf_pesquisa;
    public TableView<Material> table_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTabela("");
    }

    private void configurarTabela() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_desc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_preco.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }

    private void carregarTabela(String filtro) {
        MaterialDAL dal = new MaterialDAL();
        List<Material> materialList = dal.get(filtro);
        // É preciso habilitar a reflexão para que o javaFX possa acessar o package entidades. Vide module-info.java
        table_view.setItems(FXCollections.observableArrayList(materialList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(mat_desc) LIKE '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onNovoMaterial(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.FORM_MATERIAL, "Cadastrar Material", "icone", true);
        carregarTabela("");
    }

    public void onAlterar(ActionEvent actionEvent) {
        Material material = table_view.getSelectionModel().getSelectedItem();
        if (material != null) {
            GerenciadorTelas.carregar(new Stage(),
                    Constantes.FORM_MATERIAL,
                    "Alterar Material", "icone",
                    true,
                    (MaterialFormController controller) -> controller.carregarMaterial(material)
            );
            carregarTabela("");
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Material material = table_view.getSelectionModel().getSelectedItem();
            if (Alerta.exibirConfirmacao("Apagar material", "Deseja realmente apagar o material " + material.getDescricao() + "?"))
                new MaterialDAL().apagar(material);
        }
        carregarTabela("");
    }

    public void onFechar(ActionEvent actionEvent) {
        tf_pesquisa.getScene().getWindow().hide();
    }
}
