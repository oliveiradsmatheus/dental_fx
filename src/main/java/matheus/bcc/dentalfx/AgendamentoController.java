package matheus.bcc.dentalfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class AgendamentoController implements Initializable {
    public DatePicker dp_dia_consulta;
    public ComboBox<Pessoa> cb_dentista;
    public TableView table_view;
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
        configurarCalendario();
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
        Horario horario = (Horario) table_view.getSelectionModel().getSelectedItem();

        if (horario == null)
            Alerta.exibirErro("Erro", "Selecione um horario");
        else if (horario.getPaciente() != null)
            Alerta.exibirErro("Erro", "Horário indisponível");
        else {
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
        }
    }

    public void onCancelarAgendamento(ActionEvent actionEvent) {
        if (Alerta.exibirConfirmacao("testess","teste"))
            Alerta.exibirErro("", "Confirmado");
        else
            Alerta.exibirErro("", "Não Confirmado");
    }

    private void configurarCalendario() {
        dp_dia_consulta.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        });
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
        GeradorRelatorios.gerarRelatorio("SELECT * FROM material ORDER BY mat_desc", "rel_materiais.jasper","Relação de materiais");
    }

    public void onRelProcedimento(ActionEvent actionEvent) {
        GeradorRelatorios.gerarRelatorio("SELECT * FROM procedimento ORDER BY pro_desc", "rel_procedimentos.jasper","Relação de pacientes");
    }

    public void onRelPacientes(ActionEvent actionEvent) {
        GeradorRelatorios.gerarRelatorio("SELECT * FROM paciente ORDER BY pac_cidade", "rel_pacientes.jasper","Relação de pacientes");
    }

    public void onRelAgenda(ActionEvent actionEvent) {
        String sql = """
                SELECT c.con_relato, c.con_horario, c.pac_id, c.con_efetivado, c.den_nome, p.pac_nome FROM (
                	SELECT c.con_relato, c.con_horario, c.pac_id, c.con_efetivado, d.den_nome FROM consulta c
                	JOIN dentista d ON c.den_id = d.den_id
                	WHERE con_data = CURRENT_DATE
                ) c
                JOIN paciente p ON p.pac_id = c.pac_id
                """;
        GeradorRelatorios.gerarRelatorio(sql, "rel_agenda.jasper","Agenda do dia");
    }

    public void onRelAtendimento(ActionEvent actionEvent) {
        // individual. atendimento de um paciente.
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
