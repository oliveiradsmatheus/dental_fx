package matheus.bcc.dentalfx.util;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import javafx.application.Platform;
import javafx.concurrent.Task;
import matheus.bcc.dentalfx.db.dto.ConsultaDTO;
import matheus.bcc.dentalfx.db.entidades.Paciente;

import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeradorPDF {
    public static void imprimirFicha(Paciente paciente,  List<ConsultaDTO> consultas) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                gerarPDF(paciente, consultas);
                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> Alerta.exibirErro("Erro ao gerar ficha", getException().getMessage()));
            }
        };

        new Thread(task).start();
    }

    private static void gerarPDF(Paciente paciente, List<ConsultaDTO> consultas) {
        String destino = "src/main/resources/fichas/" + paciente.getNome().replace(" ", "").toLowerCase() + paciente.getCpf().replace(".", "").replace("-", "") + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            PdfFont fonteTitulo = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            PdfFont fonteBold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            PdfFont fonteNormal = PdfFontFactory.createFont(FontConstants.HELVETICA);

            Text titulo = new Text("Ficha do Paciente");
            titulo.setFont(fonteTitulo).setFontSize(24);
            doc.add(new Paragraph(titulo).setTextAlignment(TextAlignment.CENTER));

            doc.add(new Paragraph(" ").setFontSize(10));
            doc.add(new Paragraph("Dados Pessoais").setFont(fonteBold).setFontSize(16));

            Table tblDados = new Table(UnitValue.createPercentArray(new float[]{1, 3})).useAllAvailableWidth();

            adicionarDado(tblDados, "Nome Completo:", paciente.getNome(), fonteBold, fonteNormal);
            adicionarDado(tblDados, "CPF:", paciente.getCpf(), fonteBold, fonteNormal);
            adicionarDado(tblDados, "Telefone:", paciente.getFone(), fonteBold, fonteNormal);
            adicionarDado(tblDados, "E-mail:", paciente.getEmail(), fonteBold, fonteNormal);

            String endereco = String.format("%s, %s - %s, %s/%s",
                    paciente.getRua(), paciente.getNumero(), paciente.getBairro(),
                    paciente.getCidade(), paciente.getUf());
            adicionarDado(tblDados, "Endereço:", endereco, fonteBold, fonteNormal);
            adicionarDado(tblDados, "CEP:", paciente.getCep(), fonteBold, fonteNormal);

            doc.add(tblDados);

            doc.add(new Paragraph(" ").setFontSize(5));
            doc.add(new Paragraph("Histórico Médico Relevante")
                    .setFont(fonteBold)
                    .setFontSize(16));

            String historico = (paciente.getHistorico() == null || paciente.getHistorico().isEmpty())
                    ? "Nenhum histórico médico relevante informado."
                    : paciente.getHistorico();
            doc.add(new Paragraph(historico).setFont(fonteNormal));

            doc.add(new Paragraph(" ").setFontSize(10));

            doc.add(new Paragraph("Histórico de Consultas")
                    .setFont(fonteBold)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            Table tblConsultas = new Table(UnitValue.createPercentArray(new float[]{2, 2, 3, 5}))
                    .useAllAvailableWidth()
                    .setMarginTop(10);

            tblConsultas.addHeaderCell(new Cell().add(new Paragraph("Data")).setBackgroundColor(Color.LIGHT_GRAY).setFont(fonteBold));
            tblConsultas.addHeaderCell(new Cell().add(new Paragraph("Horário")).setBackgroundColor(Color.LIGHT_GRAY).setFont(fonteBold));
            tblConsultas.addHeaderCell(new Cell().add(new Paragraph("Dentista")).setBackgroundColor(Color.LIGHT_GRAY).setFont(fonteBold));
            tblConsultas.addHeaderCell(new Cell().add(new Paragraph("Relato")).setBackgroundColor(Color.LIGHT_GRAY).setFont(fonteBold));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (ConsultaDTO consulta : consultas) {
                tblConsultas.addCell(new Cell().add(new Paragraph(consulta.getData().format(dtf))).setFont(fonteNormal));
                tblConsultas.addCell(new Cell().add(new Paragraph(formatarHorario(consulta.getHorario()))).setFont(fonteNormal));
                tblConsultas.addCell(new Cell().add(new Paragraph(consulta.getNomeDentista())).setFont(fonteNormal));
                tblConsultas.addCell(new Cell().add(new Paragraph(consulta.getRelato())).setFont(fonteNormal));
            }

            if (!consultas.isEmpty())
                doc.add(tblConsultas);
            else {
                Paragraph aviso = new Paragraph("Paciente não possui consultas registradas.")
                        .setFont(fonteNormal)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(10);
                doc.add(aviso);
            }


            doc.close();
            Desktop.getDesktop().open(new File(destino));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String formatarHorario(int sequencia) {
        return (sequencia + 8) + ":00";
    }

    private static void adicionarDado(Table table, String rotulo, String valor, PdfFont fonteRotulo, PdfFont fonteValor) {
        table.addCell(new Cell().add(new Paragraph(rotulo))
                .setFont(fonteRotulo)
                .setBorder(Border.NO_BORDER)
                .setPaddingRight(10));
        table.addCell(new Cell().add(new Paragraph(valor != null ? valor : ""))
                .setFont(fonteValor)
                .setBorder(Border.NO_BORDER));
    }
}
