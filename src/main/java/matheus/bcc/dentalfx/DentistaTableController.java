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
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DentistaTableController implements Initializable {
    public TextField tf_pesquisa;
    public TableView<Pessoa> table_view;
    public TableColumn<Pessoa, String> col_id;
    public TableColumn<Pessoa, String> col_nome;
    public TableColumn<Pessoa, String> col_cro;
    public TableColumn<Pessoa, String> col_email;
    public TableColumn<Pessoa, String> col_fone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTabela("");
    }

    private void configurarTabela() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cro.setCellValueFactory(new PropertyValueFactory<>("cro"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_fone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        MaskFieldUtil.foneField(col_fone);
    }

    private void carregarTabela(String filtro) {
        PessoaDAL dal = new PessoaDAL();
        List<Pessoa> pessoaList = dal.get(filtro, new Dentista());
        // É preciso habilitar a reflexão para que o javaFX possa acessar o package entidades. Vide module-info.java
        table_view.setItems(FXCollections.observableArrayList(pessoaList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(pac_nome) LIKE '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onNovoDentista(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.FORM_DENTISTA, "Cadastrar Dentista", "icone", true);
        carregarTabela("");
    }

    public void onAlterar(ActionEvent actionEvent) {
        Dentista dentista = (Dentista) table_view.getSelectionModel().getSelectedItem();
        if (dentista != null) {
            GerenciadorTelas.carregar(new Stage(),
                    Constantes.FORM_DENTISTA,
                    "Alterar Dentista", "icone",
                    true,
                    (DentistaFormController controller) -> controller.carregarDentista(dentista)
            );
            carregarTabela("");
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Dentista dentista = (Dentista) table_view.getSelectionModel().getSelectedItem();
            if (Alerta.exibirConfirmacao("Apagar dentista", "Deseja realmente apagar o dentista " + dentista.getNome() + "?"))
                new PessoaDAL().apagar(dentista);
        }
        carregarTabela("");
    }

    public void onFechar(ActionEvent actionEvent) {
        tf_pesquisa.getScene().getWindow().hide();
    }
}
