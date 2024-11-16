package com.example.jasperdemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExampleData {
    private Long id;
    private String customerAdress;
    private String orderNo;
    private String nameOfReport;
    private String description;
    private String dateOfMeasurement;
    private String dateOfReceipt;
    private String dateOfAnalysis;
    private String pageOfReport;
}
