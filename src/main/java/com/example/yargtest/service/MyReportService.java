package com.example.yargtest.service;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.loaders.impl.SqlDataLoader;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.Report;
import com.haulmont.yarg.structure.ReportBand;
import com.haulmont.yarg.structure.ReportOutputType;
import com.haulmont.yarg.structure.impl.BandBuilder;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyReportService {

    private final DataSource dataSource;

    public MyReportService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generateReport(int i) throws IOException {

        ReportBuilder reportBuilder = new ReportBuilder();
        ReportTemplateBuilder reportTemplateBuilder = new ReportTemplateBuilder()
                .documentPath("D:\\playground\\yargtest\\src\\main\\resources\\templates\\customer.docx")
                .documentName("customer.docx")
                .outputType(ReportOutputType.pdf)
                .readFileFromPath();
        reportBuilder.template(reportTemplateBuilder.build());
        BandBuilder bandBuilder = new BandBuilder();
        ReportBand customer= bandBuilder.name("Customer")
                .query("Customer", "select * from customer where id= ${id}", "sql")
                .build();
        ReportBand customerLoan = bandBuilder.name("CustomerLoan")
                .query("CustomerLoan", "select * from customer_loan where customer_id= ${id}", "sql")
                .build();
        reportBuilder.band(customer);
        reportBuilder.band(customerLoan);
        Report report = reportBuilder.build();

        Reporting reporting = new Reporting();
        reporting.setFormatterFactory(new DefaultFormatterFactory());
        reporting.setLoaderFactory(
                new DefaultLoaderFactory().setSqlDataLoader(new SqlDataLoader(dataSource)));

        RunParams runParams = new RunParams(report);
        runParams.param("id", (long) i);
        ReportOutputDocument reportOutputDocument = reporting.runReport(runParams
                , new FileOutputStream("D:\\playground\\yargtest\\src\\main\\resources\\templates\\reports\\customer_" + i + ".pdf"));
    }
}
