package matheus.bcc.dentalfx.util;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import javafx.application.Platform;
import javafx.concurrent.Task;
import matheus.bcc.dentalfx.db.entidades.Paciente;
import matheus.bcc.dentalfx.db.util.SingletonDB;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.File;
import java.sql.ResultSet;

public class GeradorPDF {
    public static void imprimirFicha(Paciente paciente) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                gerarPDF(paciente);
                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> Alerta.exibirErro("Erro ao gerar ficha", getException().getMessage()));
            }
        };

        new Thread(task).start();
    }

    private static void gerarPDF(Paciente paciente) {
        String dest = "src/main/resources/fichas/" + paciente.getNome().replace(" ", "").toLowerCase() + paciente.getCpf().replace(".", "").replace("-", "") + ".pdf";
        PdfWriter writer;
        try {
            writer = new PdfWriter(dest);

            // Criando o documento  PDF
            PdfDocument pdf = new PdfDocument(writer);

            // Criando o Document
            Document doc = new Document(pdf);

            //Definindo uma fonte grande
            PdfFont fonteTitulo = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            //Definindo um texto
            Text titulo = new Text("Nome da Empresa");
            titulo.setFont(fonteTitulo);
            titulo.setFontSize(48);
            //Definindo um parágrafo
            Paragraph paragraph = new Paragraph(titulo);
            //Adicionando o parágrafo
            doc.add(paragraph);

            // Criando a tabela com 3 colunas
            float[] pointColumnWidths = {150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);

            // Adicionando celulas na tabela
            table.addCell(new Cell().add("Nome").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Idade").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Profissão").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Jeniffer da Silva"));
            table.addCell(new Cell().add("28"));
            table.addCell(new Cell().add("Programadora"));
            table.addCell(new Cell().add("Samanta Barros"));
            table.addCell(new Cell().add("21"));
            table.addCell(new Cell().add("Médica"));

            // Adicionando a tabela no documento
            doc.add(table);

            // Fechando o documento
            doc.close();
            System.out.println("documento criado..");

            //abrindo e mostrando o PDF
            Desktop.getDesktop().open(new File(dest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void gerarRelatorio(String sql, String relat, String titulo) {
        try {
            //sql para obter os dados para o relatorio
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            //implementação da interface JRDataSource para DataSource ResultSet
            JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
            //chamando o relatório
            String jasperPrint = JasperFillManager.fillReportToFile(relat,null, jrRS);
            JasperViewer viewer = new JasperViewer(jasperPrint, false, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);//maximizado
            viewer.setTitle(titulo);//titulo do relatório
            viewer.setVisible(true);
        }
        catch (Exception e) {
            Alerta.exibirErro("Erro", e.getMessage());
        }
    }
}
