package matheus.bcc.dentalfx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.dto.ConsultaDTO;
import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.repositorios.*;
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
    public TableView<Atendimento.MatItem> tv_materiais;
    public TableColumn<Atendimento.MatItem, String> col_material;
    public TableColumn<Atendimento.MatItem, Integer>  col_qtde_material;
    public TableView<Atendimento.ProcItem> tv_procedimentos;
    public TableColumn<Atendimento.ProcItem, String> col_procedimento;
    public TableColumn<Atendimento.ProcItem, Integer> col_qtde_procedimento;
    public TextArea text_area;
    public Label label_boas_vindas;

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
        label_boas_vindas.setText("Bem vindo, " + dentista.getNome());
    }

    public void onTrocouData(ActionEvent actionEvent) {
        carregarTabela();
    }

    public void onAdicionarMaterial(ActionEvent actionEvent) {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        Material material = cb_materiais.getSelectionModel().getSelectedItem();

        if (horario != null && horario.getPaciente() != null && material != null) {
            Atendimento atendimento = horario.getAtendimento();
            List<Atendimento.MatItem> items = atendimento.getMaterialList();

            Atendimento.MatItem itemEncontrado = null;
            boolean achou = false;
            int i = 0;

            while (i < items.size() && !achou) {
                Atendimento.MatItem item = items.get(i);
                if (item.material().getId() == material.getId()) {
                    itemEncontrado = item;
                    achou = true;
                }
                i++;
            }

            if (itemEncontrado != null) {
                items.remove(itemEncontrado);
                items.add(new Atendimento.MatItem(material, itemEncontrado.quant() + 1));
            } else
                items.add(new Atendimento.MatItem(material, 1));

            tv_materiais.setItems(FXCollections.observableArrayList(items));
        } else
            Alerta.exibirAlerta("Atenção", "Selecione um horário com paciente e um material.");
    }

    public void onSubtrairMaterial(ActionEvent actionEvent) {
        Atendimento.MatItem itemSelecionado = tv_materiais.getSelectionModel().getSelectedItem();
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null && horario != null && horario.getPaciente() != null) {
            Atendimento atendimento = horario.getAtendimento();
            List<Atendimento.MatItem> items = atendimento.getMaterialList();

            items.remove(itemSelecionado);
            if (itemSelecionado.quant() > 1)
                items.add(new Atendimento.MatItem(itemSelecionado.material(), itemSelecionado.quant() - 1));

            tv_materiais.setItems(FXCollections.observableArrayList(items));
        } else
            Alerta.exibirAlerta("Atenção", "Selecione um material da lista para remover.");
    }

    public void onAdicionarProcedimento(ActionEvent actionEvent) {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        Procedimento procedimento = cb_procedimentos.getSelectionModel().getSelectedItem();

        if (horario != null && horario.getPaciente() != null && procedimento != null) {
            Atendimento atendimento = horario.getAtendimento();
            List<Atendimento.ProcItem> items = atendimento.getProcedimentoList();

            Atendimento.ProcItem itemEncontrado = null;
            boolean achou = false;
            int i = 0;

            while (i < items.size() && !achou) {
                Atendimento.ProcItem item = items.get(i);
                if (item.procedimento().getId() == procedimento.getId()) {
                    itemEncontrado = item;
                    achou = true;
                }
                i++;
            }

            if (itemEncontrado != null) {
                items.remove(itemEncontrado);
                items.add(new Atendimento.ProcItem(procedimento, itemEncontrado.quant() + 1));
            } else
                items.add(new Atendimento.ProcItem(procedimento, 1));

            tv_procedimentos.setItems(FXCollections.observableArrayList(items));
        } else
            Alerta.exibirAlerta("Atenção", "Selecione um horário com paciente e um material.");
    }

    public void onSubtrairProcedimento(ActionEvent actionEvent) {
        Atendimento.ProcItem itemSelecionado = tv_procedimentos.getSelectionModel().getSelectedItem();
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();

        if (itemSelecionado != null && horario != null && horario.getPaciente() != null) {
            Atendimento atendimento = horario.getAtendimento();
            List<Atendimento.ProcItem> items = atendimento.getProcedimentoList();

            items.remove(itemSelecionado);
            if (itemSelecionado.quant() > 1)
                items.add(new Atendimento.ProcItem(itemSelecionado.procedimento(), itemSelecionado.quant() - 1));

            tv_procedimentos.setItems(FXCollections.observableArrayList(items));
        } else
            Alerta.exibirAlerta("Atenção", "Selecione um material da lista para remover.");
    }

    public void onEfetivar(ActionEvent actionEvent) {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        LocalDate data = dp_dia_consulta.getValue();

        if (horario != null && horario.getPaciente() != null) {
            if (horario.getAtendimento() == null)
                horario.setAtendimento(new Atendimento(""));
            horario.getAtendimento().setRelato(text_area.getText());

            AgendaDAL agendaDAL = new AgendaDAL();
            if (agendaDAL.efetivarConsulta(horario, data, dentista)) {
                Alerta.exibirAlerta("Sucesso", "Atendimento efetivado com sucesso!");
                carregarTabela();
            } else
                Alerta.exibirErro("Erro", "Não foi possível efetivar o atendimento.");

        } else
            Alerta.exibirAlerta("Atenção", "Selecione um horário com paciente para efetivar.");
    }

    public void onCancelar(ActionEvent actionEvent) {
        if (validarCancelamento()) {
            int indice = tv_horarios.getSelectionModel().getSelectedIndex();
            LocalDate data = dp_dia_consulta.getValue();

            cancelarConsulta(indice, dentista, data);

            carregarTabela();
        }
    }

    public void cancelarConsulta(int indice, Dentista dentista, LocalDate data) {
        AgendaDAL agendaDAL = new AgendaDAL();
        Agenda agenda =  agendaDAL.carregarAgenda(dentista, data);
        Horario horario = agenda.getHorario(indice);

        horario.setAtendimento(null);
        horario.setPaciente(null);
        agenda.setHorario(indice, horario);

        agendaDAL.salvarAgenda(agenda);
    }

    private boolean validarCancelamento() {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        boolean valido = true;
        if (horario == null) {
            Alerta.exibirErro("Erro", "Selecione um horario");
            valido = false;
        } else if (horario.getPaciente() == null) {
            Alerta.exibirErro("Erro", "Horário não ocupado");
            valido = false;
        }
        return valido;
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

        col_material.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().material().getDescricao()));
        col_qtde_material.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().quant()));

        col_procedimento.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().procedimento().getDescricao()));
        col_qtde_procedimento.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().quant()));
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

        if (horario != null && horario.getPaciente() != null) {
            if (horario.getAtendimento() == null)
                horario.setAtendimento(new Atendimento(""));

            Atendimento atendimento = horario.getAtendimento();
            text_area.setText(horario.getPaciente().getHistorico());

            if (atendimento.getRelato() != null && !atendimento.getRelato().isEmpty())
                text_area.setText(atendimento.getRelato());
            tv_materiais.setItems(FXCollections.observableArrayList(atendimento.getMaterialList()));
            tv_procedimentos.setItems(FXCollections.observableArrayList(atendimento.getProcedimentoList()));

        } else {
            text_area.clear();
            tv_materiais.setItems(FXCollections.observableArrayList());
            tv_procedimentos.setItems(FXCollections.observableArrayList());
        }
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

    public void onImprimirFicha(ActionEvent actionEvent) {
        Horario horario = tv_horarios.getSelectionModel().getSelectedItem();
        if (horario != null) {
            Paciente paciente = horario.getPaciente();
            ConsultaDAL consultaDAL = new ConsultaDAL();
            List<ConsultaDTO> listaConsultas = consultaDAL.buscarConsultasPorPaciente(paciente.getId());
            GeradorPDF.imprimirFicha(paciente, listaConsultas);
        }
    }
}
