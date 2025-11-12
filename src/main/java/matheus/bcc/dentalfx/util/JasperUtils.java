package matheus.bcc.dentalfx.util;

import matheus.bcc.dentalfx.db.util.SingletonDB;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

public class JasperUtils {
    public static void gerarRelatorio(String sql, String relat, String titulo) {
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            InputStream caminho = JasperUtils.class.getResourceAsStream("/reports/MyReports/" + relat);
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

    public static void gerarRelatorio(String relat, String titulo, Map<String, Object> parametros) {
        try {
            Connection conexao = SingletonDB.getConexao().getConnection();
            InputStream caminho = JasperUtils.class.getResourceAsStream("/reports/MyReports/" + relat);
            JasperPrint jasperPrint = JasperFillManager.fillReport(caminho,parametros, conexao);
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
