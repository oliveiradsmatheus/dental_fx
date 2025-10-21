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
import matheus.bcc.dentalfx.db.entidades.Procedimento;
import matheus.bcc.dentalfx.db.repositorios.ProcedimentoDAL;
import matheus.bcc.dentalfx.util.Confirmacao;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProcedimentoTableController implements Initializable {
    public TableColumn<Procedimento, Integer> col_id;
    public TableColumn<Procedimento, String> col_desc;
    public TableColumn<Procedimento, Double> col_tempo;
    public TableColumn<Procedimento, Double> col_valor;
    public TextField tf_pesquisa;
    public TableView<Procedimento> table_view;

    public static Procedimento procedimento = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_desc.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_tempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
        col_valor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        carregarTabela("");
    }

    private void carregarTabela(String filtro) {
        ProcedimentoDAL dal = new ProcedimentoDAL();
        List<Procedimento> procedimentoList = dal.get(filtro);
        // É preciso habilitar a reflexão para que o javaFX possa acessar o package entidades. Vide module-info.java
        table_view.setItems(FXCollections.observableArrayList(procedimentoList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(pro_desc) LIKE '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onNovoProcedimento(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.FORM_PROCEDIMENTO, "Cadastrar Procedimento", "icone", true);
        carregarTabela("");
    }

    public void onAlterar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            procedimento = table_view.getSelectionModel().getSelectedItem();
            GerenciadorTelas.carregar(new Stage(), Constantes.FORM_PROCEDIMENTO, "Alterar Procedimento", "icone", true);
            procedimento = null;
            carregarTabela("");
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Procedimento procedimento = table_view.getSelectionModel().getSelectedItem();
            if (Confirmacao.exibir("Deseja realmente apagar paciente " + procedimento.getDescricao() + "?"))
                new ProcedimentoDAL().apagar(procedimento);
        }
        carregarTabela("");
    }

    public void onFechar(ActionEvent actionEvent) {
        tf_pesquisa.getScene().getWindow().hide();
    }
}
