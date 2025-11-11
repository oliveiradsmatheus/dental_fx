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
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.entidades.Usuario;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.Constantes;
import matheus.bcc.dentalfx.util.GerenciadorTelas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UsuarioTableController implements Initializable {
    public TextField tf_pesquisa;
    public TableView<Pessoa> table_view;
    public TableColumn<Pessoa, String> col_id;
    public TableColumn<Pessoa, String> col_nome;
    public TableColumn<Pessoa, String> col_nivel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTabela("");
    }

    private void configurarTabela() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_nivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
    }

    private void carregarTabela(String filtro) {
        PessoaDAL dal = new PessoaDAL();
        List<Pessoa> pessoaList = dal.get(filtro, new Usuario());
        // É preciso habilitar a reflexão para que o javaFX possa acessar o package entidades. Vide module-info.java
        table_view.setItems(FXCollections.observableArrayList(pessoaList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(pac_nome) LIKE '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onNovoUsuario(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.FORM_USUARIO, "Cadastrar Usuário", "icone", true);
        carregarTabela("");
    }

    public void onAlterar(ActionEvent actionEvent) {
        Usuario usuario = (Usuario) table_view.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            GerenciadorTelas.carregar(new Stage(),
                    Constantes.FORM_USUARIO,
                    "Alterar Usuario", "icone",
                    true,
                    (UsuarioFormController controller) -> controller.carregarUsuario(usuario)
            );
            carregarTabela("");
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Usuario usuario = (Usuario) table_view.getSelectionModel().getSelectedItem();
            if (Alerta.exibirConfirmacao("Apagar usuário", "Deseja realmente apagar o usuário " + usuario.getNome() + "?"))
                new PessoaDAL().apagar(usuario);
        }
        carregarTabela("");
    }

    public void onFechar(ActionEvent actionEvent) {
        tf_pesquisa.getScene().getWindow().hide();
    }
}
