package matheus.bcc.dentalfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.repositorios.MaterialDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.repositorios.ProcedimentoDAL;
import matheus.bcc.dentalfx.util.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class DentistaController implements Initializable {
    public DatePicker dp_dia_consulta;
    public ComboBox<Material> cb_materiais;
    public ComboBox<Procedimento> cb_procedimentos;
    public TableView<Horario> tv_horarios;
    public TableColumn<Horario, String> col_hora;
    public TableColumn<Horario, String> col_paciente;
    public TableView tv_materiais;
    public TableColumn col_material;
    public TableColumn col_qtde_material;
    public TableView<Procedimento> tv_procedimentos;
    public TableColumn col_procedimento;
    public TableColumn col_qtde_procedimento;
    public TextArea text_area;

    private Dentista dentista;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarDentista();
        configurarCalendario();
        configurarCBMateriais();
        configurarCBProcedimentos();
        configurarTabelas();
        dp_dia_consulta.setValue(LocalDate.now());
        carregarTabela();
    }

    public void onTrocouData(ActionEvent actionEvent) {
        carregarTabela();
    }

    public void onAdicionarMaterial(ActionEvent actionEvent) {
        Material material = cb_materiais.getSelectionModel().getSelectedItem();

        //tv_materiais;

        //LocalDate data = dp_dia_consulta.getValue();
        /*Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        if (dentista != null && data != null) {
            AgendaDAL agendaDAL = new AgendaDAL();
            Agenda agenda = agendaDAL.carregarAgenda(dentista, data);
            if (agenda != null && agenda.getHorarioList() != null)
                tv_horarios.setItems(FXCollections.observableArrayList(agenda.getHorarioList()));
            else
                limparTabela();
        } else
            limparTabela();*/
    }

    public void onSubtrairMaterial(ActionEvent actionEvent) {
    }

    public void onAdicionarProcedimento(ActionEvent actionEvent) {
    }

    public void onSubtrairProcedimento(ActionEvent actionEvent) {
    }

    public void onEfetivar(ActionEvent actionEvent) {
    }

    public void onCancelar(ActionEvent actionEvent) {
    }

    private void carregarDentista() {
        PessoaDAL pessoaDAL = new PessoaDAL();
        dentista = (Dentista) pessoaDAL.get(Sessao.getInstancia().getUsuario().getDenId(), new Dentista());
    }

    private void configurarCalendario() {
        dp_dia_consulta.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    if (GerenciadorTemas.isDarkMode())
                        setStyle("-fx-background-color: #444444;");
                    else
                        setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        });
    }

    private void configurarTabelas() {
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
        LocalDate data = dp_dia_consulta.getValue();
        if (dentista != null && data != null) {
            AgendaDAL agendaDAL = new AgendaDAL();
            Agenda agenda = agendaDAL.carregarAgenda(dentista, data);
            if (agenda != null && agenda.getHorarioList() != null)
                tv_horarios.setItems(FXCollections.observableArrayList(agenda.getHorarioList()));
            else
                limparTabela();
        } else
            limparTabela();
    }

    private void limparTabela() {
        tv_horarios.setItems(FXCollections.observableArrayList());
    }

    private void configurarCBMateriais() {
        MaterialDAL materialDAL = new MaterialDAL();
        List<Material> materiais = materialDAL.get("");
        materiais.sort(Comparator.comparing(Material::getDescricao));
        FXUtils.configurarComboBox(cb_materiais, materiais, "Selecione um material", Material::getDescricao);
    }

    private void configurarCBProcedimentos() {
        ProcedimentoDAL procedimentoDAL = new ProcedimentoDAL();
        List<Procedimento> procedimentos = procedimentoDAL.get("");
        procedimentos.sort(Comparator.comparing(Procedimento::getDescricao));
        FXUtils.configurarComboBox(cb_procedimentos, procedimentos, "Selecione um procedimento", Procedimento::getDescricao);
    }

    public void onHorario(MouseEvent mouseEvent) {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        text_area.setText(horario.getPaciente().getHistorico());
    }

    public void onTrocarTema(ActionEvent actionEvent) {
        GerenciadorTemas.trocarTema();
    }

    public void onSobre(ActionEvent actionEvent) {
        Sobre.exibir("Sobre o aplicativo DentalFX");
    }

    public void onTopicos(ActionEvent actionEvent) throws Exception {
        GerenciadorTelas.carregar(new Stage(), Constantes.AJUDA, "DentalFX - Ajuda", "icone", true);
    }
}
