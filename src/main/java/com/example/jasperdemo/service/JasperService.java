package com.example.jasperdemo.service;

import com.example.jasperdemo.model.ExampleData;
import com.example.jasperdemo.repository.ItemRepository;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import com.example.jasperdemo.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperService implements CommandLineRunner {

    @Autowired
    ItemRepository itemRepository;

    @Value("${spring.datasource.username}")
    private String mailUser;

    public static void main(String[] args) {
        SpringApplication.run(JasperService.class, args);
    }

    @Override
    public void run(String... args) throws Exception {




    }

    public byte[] getItemReport(List<Item> items, String format) throws JRException, FileNotFoundException {
        // Load the JRXML template from the resources
        InputStream inputStream = getClass().getResourceAsStream("/template/report_example.jrxml");
        ExampleData exampleData = ExampleData.builder()
                .id(1L)
                .orderNo("m")
                .description("mm")
                .nameOfReport("mmm")
                .dateOfAnalysis("mmmm")
                .dateOfReceipt("mmmmm")
                .dateOfMeasurement("mmmmm")
                .pageOfReport("mmmmmmm")
                .dateOfMeasurement("mmmmmmm")
                .pageOfReport("mmmmmmmmmm").build();
        List<ExampleData> exampleData1 = new ArrayList<>();
        exampleData1.add(exampleData);

        if (inputStream == null) {
            throw new FileNotFoundException("Could not find the report template.");
        }

        // Compile the report from the JRXML
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        // Set up the data source with the list of items
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(exampleData1);

        // Set parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("title", "Item Report");
        JRDesignStyle jrDesignStyle = new JRDesignStyle();
        /*Set the Encoding to UTF-8 for pdf and embed font to arial*/
        jrDesignStyle.setDefault(true);
        String fontPath = getClass().getResource("/template/arial.ttf").getPath();
        jrDesignStyle.setPdfFontName(fontPath);
        jrDesignStyle.setPdfEncoding("Identity-H");
        jrDesignStyle.setPdfEmbedded(true);


        // Fill the report with data and parameters
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        jasperPrint.addStyle(jrDesignStyle);
        // Export the report to a PDF byte array
        byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        return reportContent;
    }
}
