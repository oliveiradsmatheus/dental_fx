package matheus.bcc.dentalfx.util;

import matheus.bcc.dentalfx.db.util.SingletonDB;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.ResultSet;

public class GeradorRelatorios {
    public static void gerarRelatorio(String sql, String relat, String titulo) {
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            InputStream caminho = GeradorRelatorios.class.getResourceAsStream("/reports/MyReports/" + relat);
            JasperPrint jasperPrint = JasperFillManager.fillReport(caminho,null, jrRS);
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setTitle(titulo);
            viewer.setVisible(true);
        }
        catch (Exception e) {
            Alerta.exibirErro("Erro", e.getMessage());
        }
    }

}
