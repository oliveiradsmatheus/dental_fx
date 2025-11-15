package matheus.bcc.dentalfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import matheus.bcc.dentalfx.db.entidades.*;
import matheus.bcc.dentalfx.db.repositorios.AgendaDAL;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.db.util.Banco;
import matheus.bcc.dentalfx.util.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class AgendamentoController implements Initializable {
    public DatePicker dp_dia_consulta;
    public ComboBox<Pessoa> cb_dentista;
    public TableView<Horario> table_view;
    public TableColumn<Horario, String> col_hora;
    public TableColumn<Horario, String> col_paciente;
    public MenuItem menu_item_usuario;
    public Menu menu_relatorios;
    public Menu menu_sistema;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Usuario usuario = Sessao.getInstancia().getUsuario();
        if (usuario.getNivel() != 1)
            esconderRecursosAdmin();
        FXUtils.configurarDatePicker(dp_dia_consulta);
        configurarCB();
        configurarTabela();
    }

    public void onPaciente(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_PACIENTE, "Gerenciamento de Pacientes", "icone", true);
    }

    public void onDentista(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_DENTISTA, "Gerenciamento de Dentistas", "icone", true);
        configurarCB();
    }

    public void onUsuario(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.TABELA_USUARIO, "Gerenciamento de Usuários", "icone", true);
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
            Alerta.exibirErro("Erro", "Por favor, selecione uma data válida!");
            dp_dia_consulta.setValue(LocalDate.now());
        } else
            carregarTabela();
    }

    public void onTrocouDentista(ActionEvent actionEvent) {
        carregarTabela();
    }

    public void onAgendar(ActionEvent actionEvent) {
        if (validarAgendamento()) {
            int indice = table_view.getSelectionModel().getSelectedIndex();
            Dentista dentista = (Dentista) cb_dentista.getValue();
            LocalDate data = dp_dia_consulta.getValue();

            GerenciadorTelas.carregar(
                    new Stage(),
                    Constantes.FORM_AGENDAMENTO,
                    "Agendar Consulta", "icone",
                    true,
                    (AgendamentoFormController controller) -> controller.carregarDados(indice, dentista, data)
            );

            carregarTabela();
        }
    }

    private boolean validarAgendamento() {
        Horario horario = table_view.getSelectionModel().getSelectedItem();
        boolean valido = true;
        if (horario == null) {
            Alerta.exibirErro("Erro", "Selecione um horario");
            valido = false;
        } else if (horario.getPaciente() != null) {
            Alerta.exibirErro("Erro", "Horário indisponível");
            valido = false;
        }
        return valido;
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

    public void onCancelarAgendamento(ActionEvent actionEvent) {
        if (validarCancelamento()) {
            int indice = table_view.getSelectionModel().getSelectedIndex();
            Dentista dentista = (Dentista) cb_dentista.getValue();
            LocalDate data = dp_dia_consulta.getValue();

            cancelarConsulta(indice, dentista, data);

            carregarTabela();
        }
    }

    private boolean validarCancelamento() {
        Horario horario = table_view.getSelectionModel().getSelectedItem();
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

    private void configurarCB() {
        PessoaDAL pessoaDAL = new PessoaDAL();
        List<Pessoa> dentistas = pessoaDAL.get("", new Dentista());
        dentistas.sort(Comparator.comparing(Pessoa::getNome));
        FXUtils.configurarComboBox(cb_dentista, dentistas, "Selecione um dentista", Pessoa::getNome);
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

    public void onRelMaterial(ActionEvent actionEvent) {
        JasperUtils.gerarRelatorio("SELECT * FROM material ORDER BY mat_desc", "rel_materiais.jasper","Relação de materiais");
    }

    public void onRelProcedimento(ActionEvent actionEvent) {
        JasperUtils.gerarRelatorio("SELECT * FROM procedimento ORDER BY pro_desc", "rel_procedimentos.jasper","Relação de pacientes");
    }

    public void onRelPacientes(ActionEvent actionEvent) {
        JasperUtils.gerarRelatorio("SELECT * FROM paciente ORDER BY pac_cidade", "rel_pacientes.jasper","Relação de pacientes");
    }

    public void onRelAgenda(ActionEvent actionEvent) {
        LocalDate data = datePicker();
        if (data != null) {
            String sql = String.format("""
                SELECT c.con_relato, c.con_horario, c.pac_id, c.con_efetivado, c.den_nome, p.pac_nome FROM (
                	SELECT c.con_relato, c.con_horario, c.pac_id, c.con_efetivado, d.den_nome FROM consulta c
                	JOIN dentista d ON c.den_id = d.den_id
                	WHERE con_data = '%s'
                ) c
                JOIN paciente p ON p.pac_id = c.pac_id
                """, data);
            JasperUtils.gerarRelatorio(sql, "rel_agenda.jasper","Agenda do dia");
        }
    }

    private LocalDate datePicker() {
        AtomicReference<LocalDate> dataSelecionada = new AtomicReference<>();

        Stage stage = new Stage();
        stage.setTitle("Selecionar Data");
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Selecione a data para o relatório:");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);

        DatePicker datePicker = new DatePicker();
        datePicker.setMaxWidth(Double.MAX_VALUE);

        Button bt_confirmar = new Button("Ver agenda do dia");
        Button bt_cancelar = new Button("Cancelar");

        HBox hboxBotoes = new HBox(32, bt_confirmar, bt_cancelar);
        hboxBotoes.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(32, label, datePicker, hboxBotoes);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinWidth(400);
        vbox.setMinHeight(226);

        bt_confirmar.setDefaultButton(true);
        bt_confirmar.setOnAction(e -> {
            dataSelecionada.set(datePicker.getValue());
            stage.hide();
        });

        bt_cancelar.setCancelButton(true);
        bt_cancelar.setOnAction(e -> {
            dataSelecionada.set(null);
            stage.hide();
        });

        Scene scene = new Scene(vbox);
        GerenciadorTemas.registrar(scene);
        stage.setScene(scene);

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icones/icone.png")));

        stage.showAndWait();
        return dataSelecionada.get();
    }

    public void onRelAtendimento(ActionEvent actionEvent) {
        GerenciadorTelas.carregar(new Stage(), Constantes.RELATORIO, "Gerenciamento de Procedimentos", "icone", true);
    }

    private void esconderRecursosAdmin() {
        menu_relatorios.setVisible(false);
        menu_item_usuario.setVisible(false);
        menu_sistema.setVisible(false);
    }

    public void onBackup(ActionEvent actionEvent) throws Exception {
        String arquivo = "bdutil/backup/sisdental.sql";
        Banco.backup(arquivo, "sisdentaldb");
    }

    public void onRestore(ActionEvent actionEvent) throws Exception {
        String arquivo = "bdutil/backup/sisdental.sql";
        Banco.restaurar(arquivo, "sisdentaldb");
    }

    public void onTopicos(ActionEvent actionEvent) throws Exception {
        GerenciadorTelas.carregar(new Stage(), Constantes.AJUDA, "DentalFX - Ajuda", "icone", true);
    }
}
