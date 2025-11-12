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
import matheus.bcc.dentalfx.db.dto.ConsultaDTO;
import matheus.bcc.dentalfx.db.entidades.Paciente;
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.repositorios.ConsultaDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PacienteTableController implements Initializable {
    public TextField tf_pesquisa;
    public TableView<Pessoa> table_view;
    public TableColumn<Pessoa, String> col_id;
    public TableColumn<Pessoa, String> col_nome;
    public TableColumn<Pessoa, String> col_cpf;
    public TableColumn<Pessoa, String> col_cidade;
    public TableColumn<Pessoa, String> col_fone;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTabela("");
    }

    private void configurarTabela() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_cpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        col_cidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        col_fone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        MaskFieldUtil.foneField(col_fone);
    }

    private void carregarTabela(String filtro) {
        PessoaDAL dal = new PessoaDAL();
        List<Pessoa> pessoaList = dal.get(filtro, new Paciente());
        // É preciso habilitar a reflexão para que o javaFX possa acessar o package entidades. Vide module-info.java
        table_view.setItems(FXCollections.observableArrayList(pessoaList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(pac_nome) LIKE '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onNovoPaciente(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.FORM_PACIENTE, "Cadastrar Paciente", "icone", true);
        carregarTabela("");
    }

    public void onAlterar(ActionEvent actionEvent) {
        Paciente paciente = (Paciente) table_view.getSelectionModel().getSelectedItem();
        if (paciente != null) {
            GerenciadorTelas.carregar(
                    new Stage(),
                    Constantes.FORM_PACIENTE,
                    "Alterar Paciente", "icone",
                    true,
                    (PacienteFormController controller) -> controller.carregarPaciente(paciente)
            );
            carregarTabela("");
        }
    }

    public void onApagar(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Paciente paciente = (Paciente) table_view.getSelectionModel().getSelectedItem();
            if (Alerta.exibirConfirmacao("Apagar cliente","Deseja realmente apagar o paciente " + paciente.getNome() + "?"))
                new PessoaDAL().apagar(paciente);
        }
        carregarTabela("");
    }

    public void onImprimirFicha(ActionEvent actionEvent) {
        if (table_view.getSelectionModel().getSelectedItem() != null) {
            Paciente paciente = (Paciente) table_view.getSelectionModel().getSelectedItem();
            ConsultaDAL consultaDAL = new ConsultaDAL();
            List<ConsultaDTO> listaConsultas = consultaDAL.buscarConsultasPorPaciente(paciente.getId());
            GeradorPDF.imprimirFicha(paciente, listaConsultas);
        }
    }

    public void onFechar(ActionEvent actionEvent) {
        tf_pesquisa.getScene().getWindow().hide();
    }
}
