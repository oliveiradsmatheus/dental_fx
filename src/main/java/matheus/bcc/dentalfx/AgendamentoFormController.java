package matheus.bcc.dentalfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.MaskFieldUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AgendamentoFormController implements Initializable {
    public TextField tf_dentista;
    public TextField tf_data;
    public TextField tf_horario;
    public TextField tf_pesquisa;
    public TableView table_view;
    public TableColumn col_nome;
    public TableColumn col_fone;

    private int indice;
    private Dentista dentista;
    private LocalDate data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarTabela("");
    }

    public void carregarDados(int indice, Dentista dentista, LocalDate data) {
        this.indice = indice;
        this.dentista = dentista;
        this.data = data;
        configurarFormulario();
    }

    private void configurarFormulario() {
        tf_dentista.setText(dentista.getNome());
        tf_dentista.setEditable(false);
        tf_data.setText(data.toString());
        tf_data.setEditable(false);
        int hora = indice + 8;
        String horaFormatada = String.format("%02d:00", hora);
        tf_horario.setText(horaFormatada);
        tf_horario.setEditable(false);
    }

    private void configurarTabela() {
        col_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        col_fone.setCellValueFactory(new PropertyValueFactory<>("fone"));
        MaskFieldUtil.foneField(col_fone);
    }

    private void carregarTabela(String filtro) {
        PessoaDAL dal = new PessoaDAL();
        List<Pessoa> pessoaList = dal.get(filtro, new Paciente());
        table_view.setItems(FXCollections.observableArrayList(pessoaList));
    }

    public void onPesquisar(KeyEvent keyEvent) {
        carregarTabela(" upper(pac_nome) LIKE  '%" + tf_pesquisa.getText().toUpperCase() + "%'");
    }

    public void onConfirmar(ActionEvent actionEvent) {
        if (validarCampos()) {
            AgendaDAL agendaDAL = new AgendaDAL();
            Paciente paciente = (Paciente) table_view.getSelectionModel().getSelectedItem();
            Horario horario = new Horario(indice, paciente);

            Agenda agenda = agendaDAL.carregarAgenda(dentista, data);
            agenda.setHorario(indice, horario);
            agendaDAL.salvarAgenda(agenda);
            table_view.getScene().getWindow().hide();
        }
    }

    private boolean validarCampos() {
        return table_view.getSelectionModel().getSelectedItem() != null;
    }

    public void onCancelar(ActionEvent actionEvent) {
        tf_dentista.getScene().getWindow().hide();
    }
}
