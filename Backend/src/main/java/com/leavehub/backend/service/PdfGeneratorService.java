package com.leavehub.backend.service;

import com.leavehub.backend.entity.LeaveRequest;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGeneratorService {

    public byte[] generateLeaveRequestPdf(LeaveRequest request) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("CERERE DE CONCEDIU", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("\n"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            document.add(new Paragraph("Subsemnatul/a, " + request.getEmployee().getName() + ","));
            document.add(new Paragraph("Angajat în cadrul departamentului: " +
                    (request.getEmployee().getDepartment() != null ? request.getEmployee().getDepartment().getDepartmentName() : "N/A")));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Vă rog să-mi aprobați cererea de " + request.getLeaveType().getName() +
                    " pentru perioada " + request.getStartDate().format(formatter) + " - " + request.getEndDate().format(formatter) + "."));

            document.add(new Paragraph("Număr zile lucrătoare: " + request.getWorkingDays()));
            document.add(new Paragraph("Status cerere: " + request.getStatus()));

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Data emiterii: " + request.getCreatedAt().format(formatter)));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la generarea PDF-ului", e);
        }

        return out.toByteArray();
    }
}