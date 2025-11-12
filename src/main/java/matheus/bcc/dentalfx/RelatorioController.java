package matheus.bcc.dentalfx;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import matheus.bcc.dentalfx.db.entidades.Dentista;
import matheus.bcc.dentalfx.db.entidades.Pessoa;
import matheus.bcc.dentalfx.db.repositorios.PessoaDAL;
import matheus.bcc.dentalfx.util.Alerta;
import matheus.bcc.dentalfx.util.FXUtils;
import matheus.bcc.dentalfx.util.JasperUtils;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RelatorioController implements Initializable {
    public ComboBox<Pessoa> cb_dentista;
    public DatePicker dp_inicial;
    public DatePicker dp_final;
    public Button bt_relatorio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCB();
        dp_inicial.setValue(LocalDate.now());
        dp_final.setValue(LocalDate.now());
    }

    private void configurarCB() {
        PessoaDAL pessoaDAL = new PessoaDAL();
        List<Pessoa> dentistas = pessoaDAL.get("", new Dentista());
        dentistas.sort(Comparator.comparing(Pessoa::getNome));
        FXUtils.configurarComboBox(cb_dentista, dentistas, "Selecione um dentista", Pessoa::getNome);
    }

    public void onGerarRelatorio(ActionEvent actionEvent) {
        if (validarCampos()) {
            Dentista dentista = (Dentista) cb_dentista.getSelectionModel().getSelectedItem();
            LocalDate dataInicial = dp_inicial.getValue();
            LocalDate dataFinal = dp_final.getValue();

            Map<String, Object> parametros = new HashMap<>();

            parametros.put("P_DENTISTA_ID", dentista.getId());
            parametros.put("P_DATA_INICIAL", java.sql.Date.valueOf(dataInicial));
            parametros.put("P_DATA_FINAL", java.sql.Date.valueOf(dataFinal));

            JasperUtils.gerarRelatorio("rel_consultas.jasper", "Relatório de Consultas", parametros);
        } else
            Alerta.exibirErro("Erro", "Todos os campos devem ser preenchidos corretamente.");
    }

    private boolean validarCampos() {
        return cb_dentista.getSelectionModel().getSelectedItem() != null && (dp_inicial.getValue().isBefore(dp_final.getValue()) || dp_inicial.getValue().isEqual(dp_final.getValue()));
    }
}
