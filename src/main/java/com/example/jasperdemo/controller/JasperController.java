package com.example.jasperdemo.controller;

import com.example.jasperdemo.repository.ItemRepository;
import com.example.jasperdemo.service.JasperService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.repo.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
@Controller
public class JasperController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    JasperService jasperReportService;

    @GetMapping("item-report/{format}")
    public ResponseEntity<ByteArrayResource> getItemReport(@PathVariable String format) throws JRException, IOException {
        // Generate the report content as a byte array
        byte[] reportContent = jasperReportService.getItemReport(itemRepository.findAll(), format);

        // Create a ByteArrayResource from the byte array
        ByteArrayResource resource = new ByteArrayResource(reportContent);

        // Determine the content type based on the format
        MediaType mediaType;
        switch (format.toLowerCase()) {
            case "pdf":
                mediaType = MediaType.APPLICATION_PDF;
                break;
            case "xlsx":
                mediaType = MediaType.APPLICATION_OCTET_STREAM; // Or another appropriate type for Excel
                break;
            case "csv":
                mediaType = MediaType.TEXT_PLAIN; // Or another appropriate type for CSV
                break;
            default:
                mediaType = MediaType.APPLICATION_OCTET_STREAM; // Default type
                break;
        }

        // Build the ResponseEntity
        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"item-report.%s\"", format))
                .body(resource);
    }

}
