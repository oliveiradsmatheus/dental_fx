package matheus.bcc.dentalfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.entidades.Agenda;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.entidades.Horario;
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AgendamentoController implements Initializable {
    public DatePicker dp_dia_consulta;
    public ComboBox<Pessoa> cb_dentista;
    public TableView table_view;
    public TableColumn<Horario, String> col_hora;
    public TableColumn<Horario, String> col_paciente;

    public static int indice;
    public static Dentista dentista;
    public static LocalDate data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarCB();
        configurarTabela();
        dp_dia_consulta.setValue(LocalDate.now());
    }

    public void onPaciente(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_PACIENTE, "Gerenciamento de Pacientes", "icone", true);
    }

    public void onDentista(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_DENTISTA, "Gerenciamento de Dentistas", "icone", true);
        configurarCB();
    }

    public void onMaterial(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_MATERIAL, "Gerenciamento de Material", "icone", true);
    }

    public void onProcedimento(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_PROCEDIMENTO, "Gerenciamento de Procedimentos", "icone", true);
    }

    public void onSobre(ActionEvent actionEvent) {
        Sobre.exibir("Sobre o aplicativo DentalFX");
    }

    public void onTrocouData(ActionEvent actionEvent) {
        if (dp_dia_consulta.getValue().isBefore(LocalDate.now())) {
            Erro.exibir("Erro", "Por favor, selecione uma data válida!");
            dp_dia_consulta.setValue(LocalDate.now());
        } else
            carregarTabela();
    }

    public void onTrocouDentista(ActionEvent actionEvent) {
        carregarTabela();
    }

    public void onAgendar(ActionEvent actionEvent) {
        Horario horario = (Horario) table_view.getSelectionModel().getSelectedItem();
        IO.println(table_view.getSelectionModel().getSelectedIndex());

        if (horario == null)
            Erro.exibir("Erro", "Selecione um horario");
        else if (horario.getPaciente() != null)
            Erro.exibir("Erro", "Horário indisponível");
        else {
            indice = table_view.getSelectionModel().getSelectedIndex();
            dentista = (Dentista) cb_dentista.getValue();
            data = dp_dia_consulta.getValue();
            GerenciadorTelas.carregar(new Stage(), Constantes.FORM_AGENDAMENTO, "Agendar Consulta", "icone", true);
            indice = -1;
            dentista = null;
            data = null;
        }
    }

    public void onCancelarAgendamento(ActionEvent actionEvent) {
        if (Confirmacao.exibir("testess","teste"))
            Erro.exibir("", "Confirmado");
        else
            Erro.exibir("", "Não Confirmado");
    }

    private void configurarCB() {
        PessoaDAL pessoaDAL = new PessoaDAL();
        List<Pessoa> dentistas = pessoaDAL.get("", new Dentista());
        cb_dentista.setItems(FXCollections.observableList(dentistas));
        cb_dentista.setPromptText("Selecione um dentista");

        cb_dentista.setCellFactory(p -> new ListCell<>() {
            @Override
            protected void updateItem(Pessoa item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText(item.getNome());
            }
        });

        cb_dentista.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Pessoa item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText(item.getNome());
            }
        });
    }

    private void configurarTabela() {
        col_hora.setCellValueFactory(cellData -> {
            Horario horario = cellData.getValue();
            int hora = horario.getSequencia() + 8;
            String horaFormatada = String.format("%02d:00", hora);
            return new SimpleStringProperty(horaFormatada);
        });

        col_paciente.setCellValueFactory(cellData -> {
            Horario horario = cellData.getValue();
            if (horario != null && horario.getPaciente() != null && horario.getPaciente().getId() > 0)
                return new SimpleStringProperty(horario.getPaciente().getNome());
            else
                return new SimpleStringProperty("Livre");
        });
    }

    private void carregarTabela() {
        Dentista dentista = (Dentista) cb_dentista.getValue();
        LocalDate data = dp_dia_consulta.getValue();
        if (dentista != null && data != null) {
            AgendaDAL agendaDAL = new AgendaDAL();
            Agenda agenda = agendaDAL.carregarAgenda(dentista, data);
            if (agenda != null && agenda.getHorarioList() != null)
                table_view.setItems(FXCollections.observableArrayList(agenda.getHorarioList()));
            else
                limparTabela();
        } else
            limparTabela();
    }

    private void limparTabela() {
        table_view.setItems(FXCollections.observableArrayList());
    }

    public void onTrocarTema(ActionEvent actionEvent) {
        GerenciadorTemas.trocarTema();
    }
}
