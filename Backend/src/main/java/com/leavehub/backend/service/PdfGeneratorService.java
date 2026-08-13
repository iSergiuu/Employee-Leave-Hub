package com.leavehub.backend.service;

import com.leavehub.backend.entity.LeaveRequest;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGeneratorService {

    public byte[] generateLeaveRequestPdf(LeaveRequest request) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font smallItalic = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);

            PdfPCell leftCell = new PdfPCell(new Phrase("Leave Hub S.R.L.", boldFont));
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            String emitDate = request.getCreatedAt() != null ? request.getCreatedAt().format(formatter) : LocalDate.now().format(formatter);
            PdfPCell rightCell = new PdfPCell(new Phrase("Data emiterii: " + emitDate, normalFont));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);
            document.add(headerTable);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(Color.LIGHT_GRAY);
            document.add(new Chunk(ls));
            document.add(new Paragraph("\n"));

            Paragraph title = new Paragraph("CERERE DE CONCEDIU", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n\n"));

            String deptName = (request.getEmployee().getDepartment() != null)
                    ? request.getEmployee().getDepartment().getDepartmentName()
                    : "N/A";

            Paragraph p1 = new Paragraph();
            p1.add(new Chunk("Subsemnatul/a, ", normalFont));
            p1.add(new Chunk(request.getEmployee().getName(), boldFont));
            p1.add(new Chunk(", angajat(ă) în cadrul departamentului ", normalFont));
            p1.add(new Chunk(deptName, boldFont));
            p1.add(new Chunk(",", normalFont));
            document.add(p1);

            document.add(new Paragraph("\n"));

            Paragraph p2 = new Paragraph();
            p2.add(new Chunk("Vă rog să-mi aprobați cererea de ", normalFont));
            p2.add(new Chunk(request.getLeaveType().getName(), boldFont));
            p2.add(new Chunk(" pentru perioada de mai jos:", normalFont));
            document.add(p2);

            document.add(new Paragraph("\n"));

            Paragraph details = new Paragraph();
            details.setIndentationLeft(20);
            details.add(new Chunk("Data de început: ", normalFont));
            details.add(new Chunk(request.getStartDate().format(formatter) + "\n", boldFont));
            details.add(new Chunk("Data de sfârșit: ", normalFont));
            details.add(new Chunk(request.getEndDate().format(formatter) + "\n", boldFont));
            details.add(new Chunk("Număr zile lucrătoare: ", normalFont));
            details.add(new Chunk(String.valueOf(request.getWorkingDays()) + "\n", boldFont));

            String statusRo = translateStatus(request.getStatus());
            details.add(new Chunk("Status cerere în sistem: ", normalFont));
            details.add(new Chunk(statusRo, boldFont));

            document.add(details);

            document.add(new Paragraph("\n\n\n\n"));

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);

            PdfPCell empSig = new PdfPCell(new Phrase("Semnătura Angajatului\n\n........................................", boldFont));
            empSig.setBorder(Rectangle.NO_BORDER);
            empSig.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell mgrSig = new PdfPCell(new Phrase("Aprobare Manager / HR\n\n........................................", boldFont));
            mgrSig.setBorder(Rectangle.NO_BORDER);
            mgrSig.setHorizontalAlignment(Element.ALIGN_CENTER);

            signatureTable.addCell(empSig);
            signatureTable.addCell(mgrSig);
            document.add(signatureTable);

            document.add(new Paragraph("\n\n\n"));
            Paragraph footer = new Paragraph("Document generat automat de platforma Leave Hub.", smallItalic);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la generarea PDF-ului", e);
        }

        return out.toByteArray();
    }

    public byte[] generateDepartmentReportPdf(List<LeaveRequest> requests, String departmentName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            PdfPCell leftCell = new PdfPCell(new Phrase("Leave Hub S.R.L.", headerFont));
            leftCell.setBorder(Rectangle.NO_BORDER);
            PdfPCell rightCell = new PdfPCell(new Phrase("Data generării: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);
            document.add(headerTable);

            LineSeparator ls = new LineSeparator();
            ls.setLineColor(Color.LIGHT_GRAY);
            document.add(new Chunk(ls));
            document.add(new Paragraph("\n"));

            Paragraph title = new Paragraph("RAPORT LUNAR CONCEDII", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Departament: " + departmentName, headerFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(new Paragraph("\n\n"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 3f, 3.5f, 1.5f, 2f}); // Dimensiuni relative pentru coloane

            String[] headers = {"Nume Angajat", "Tip Concediu", "Perioada", "Zile", "Status"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new Color(240, 240, 240)); // Gri deschis
                cell.setPadding(6);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (LeaveRequest req : requests) {
                table.addCell(new PdfPCell(new Phrase(req.getEmployee().getName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(req.getLeaveType().getName(), normalFont)));

                String period = req.getStartDate().format(fmt) + " - " + req.getEndDate().format(fmt);
                table.addCell(new PdfPCell(new Phrase(period, normalFont)));

                PdfPCell daysCell = new PdfPCell(new Phrase(String.valueOf(req.getWorkingDays()), normalFont));
                daysCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(daysCell);

                PdfPCell statusCell = new PdfPCell(new Phrase(translateStatus(req.getStatus()), normalFont));
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(statusCell);
            }

            document.add(table);

            // Footer
            document.add(new Paragraph("\n\n\n"));
            Paragraph footer = new Paragraph("Document generat automat de platforma Leave Hub.", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la generarea raportului", e);
        }

        return out.toByteArray();
    }

    private String translateStatus(String status) {
        if (status == null) return "N/A";
        switch (status.toUpperCase()) {
            case "APPROVED": return "APROBAT";
            case "PENDING": return "ÎN AȘTEPTARE";
            case "REJECTED": return "RESPINS";
            case "CANCELLED": return "ANULAT";
            default: return status;
        }
    }
}