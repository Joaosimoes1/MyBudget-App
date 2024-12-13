package org.example.gps_g22.model.data;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.example.gps_g22.dto.TransactInfo;
import org.example.gps_g22.model.Manager;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

import com.itextpdf.text.pdf.PdfWriter;
import org.example.gps_g22.model.data.stock.Stock;

public class ReportGenerator {

    private ReportGenerator() {
    }//no instances


    public static boolean downloadPDF(List<Object> reportsList, String typeReport, String timeFrameText ) {
        // Obtendo a pasta de Transferências do sistema
        String userHome = System.getProperty("user.home");
        File downloadDir = Paths.get(userHome, "Downloads").toFile();

        // Base do nome do arquivo
        String baseFileName = "Report";
        String extension = ".pdf";
        File pdfFile = new File(downloadDir, baseFileName + extension);

        // Incrementar o nome do arquivo se já existir
        int count = 1;
        while (pdfFile.exists()) {
            pdfFile = new File(downloadDir, baseFileName + "_" + count + extension);
            count++;
        }

        // Criando o documento PDF
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            // Adicionando título ao PDF
            Paragraph title = new Paragraph("Report: " + typeReport, new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // Linha em branco

            Paragraph timeFrameParagraph = new Paragraph(timeFrameText);
            document.add(timeFrameParagraph);
            document.add(new Paragraph("\n")); // Linha em branco

            // Criando a tabela dinâmica baseada no tipo de relatório
            PdfPTable table;
            if ("Stocks".equalsIgnoreCase(typeReport)) {
                table = new PdfPTable(6); // 6 colunas para Stock
                table.addCell(new PdfPCell(new Phrase("Name")));
                table.addCell(new PdfPCell(new Phrase("ID")));
                table.addCell(new PdfPCell(new Phrase("Quantity")));
                table.addCell(new PdfPCell(new Phrase("Price")));
                table.addCell(new PdfPCell(new Phrase("Type")));
                table.addCell(new PdfPCell(new Phrase("Date")));
            } else if ("Transactions".equalsIgnoreCase(typeReport)) {
                table = new PdfPTable(5); // 5 colunas para Transaction
                table.addCell(new PdfPCell(new Phrase("Account")));
                table.addCell(new PdfPCell(new Phrase("NIF")));
                table.addCell(new PdfPCell(new Phrase("Amount")));
                table.addCell(new PdfPCell(new Phrase("Type")));
                table.addCell(new PdfPCell(new Phrase("Date")));
            } else {
                throw new IllegalArgumentException("Unknown report type: " + typeReport);
            }

            // Adicionando dados à tabela
            for (Object report : reportsList) {
                if ("Stocks".equalsIgnoreCase(typeReport) && report instanceof Stock stock) {
                    table.addCell(new PdfPCell(new Phrase(stock.getName())));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(stock.getId()))));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(stock.getAmount()))));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(stock.getPrice()))));
                    table.addCell(new PdfPCell(new Phrase(stock.getType().toString())));
                    table.addCell(new PdfPCell(new Phrase(stock.getDate().toString())));
                } else if ("Transactions".equalsIgnoreCase(typeReport) && report instanceof TransactInfo transaction) {
                    table.addCell(new PdfPCell(new Phrase(transaction.getName())));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(transaction.getNif()))));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(transaction.getAmount()))));
                    table.addCell(new PdfPCell(new Phrase(transaction.getType())));
                    table.addCell(new PdfPCell(new Phrase(transaction.getDateOfTransaction().toString())));
                } else {
                    System.err.println("Unexpected object in list: " + report.getClass().getName());
                }
            }

            // Adicionando a tabela ao documento
            document.add(table);

            System.out.println("PDF created successfully at: " + pdfFile.getAbsolutePath());
            return true; // PDF criado com sucesso
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return false; // Erro ao criar o PDF
        } finally {
            document.close();
        }
    }

    public static boolean downloadCSV(List<Object> reportsList, String typeReport, String timeFrameText) {
        // Obtendo a pasta de Transferências do sistema
        String userHome = System.getProperty("user.home");
        File downloadDir = Paths.get(userHome, "Downloads").toFile();

        // Base do nome do arquivo
        String baseFileName = "Report";
        String extension = ".csv";
        File csvFile = new File(downloadDir, baseFileName + extension);

        // Incrementar o nome do arquivo se já existir
        int count = 1;
        while (csvFile.exists()) {
            csvFile = new File(downloadDir, baseFileName + "_" + count + extension);
            count++;
        }

        // Criando o arquivo CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {


            // Adicionando título ao CSV
            writer.write("Report: " + typeReport);
            writer.newLine();

            // Adicionando a legenda sobre o timeframe


            writer.write(timeFrameText);
            writer.newLine();
            writer.newLine(); // Linha em branco

            // Escrevendo as colunas dependendo do tipo de relatório
            if ("Stocks".equalsIgnoreCase(typeReport)) {
                writer.write("Name,ID,Quantity,Price,Type,Date");
            } else if ("Transactions".equalsIgnoreCase(typeReport)) {
                writer.write("Account,NIF,Amount,Type,Date");
            } else {
                throw new IllegalArgumentException("Unknown report type: " + typeReport);
            }
            writer.newLine();

            // Adicionando dados ao CSV
            for (Object report : reportsList) {
                if ("Stocks".equalsIgnoreCase(typeReport) && report instanceof Stock stock) {
                    writer.write(stock.getName() + ","
                            + stock.getId() + ","
                            + stock.getAmount() + ","
                            + stock.getPrice() + ","
                            + stock.getType() + ","
                            + stock.getDate());
                    writer.newLine();
                } else if ("Transactions".equalsIgnoreCase(typeReport) && report instanceof TransactInfo transaction) {
                    writer.write(transaction.getName() + ","
                            + transaction.getNif() + ","
                            + transaction.getAmount() + ","
                            + transaction.getType() + ","
                            + transaction.getDateOfTransaction());
                    writer.newLine();
                } else {
                    System.err.println("Unexpected object in list: " + report.getClass().getName());
                }
            }

            System.out.println("CSV created successfully at: " + csvFile.getAbsolutePath());
            return true; // CSV criado com sucesso
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Erro ao criar o CSV
        }
    }
}
