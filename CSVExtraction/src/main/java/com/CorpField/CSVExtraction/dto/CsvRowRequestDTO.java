package com.CorpField.CSVExtraction.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CsvRowRequestDTO {
    private String flavourName;
    private String flavourDescription;
    private boolean flavourActive;
    private boolean flavourDeleted;
    private String flavourCreatedAt;
    private String flavourUpdatedAt;


    private String brandName;
    private String brandDescription;
    private boolean brandActive;
    private boolean brandDeleted;

    private String categoryName;
    private String categoryDescription;
    private boolean categoryActive;
    private boolean categoryDeleted;
    private String categoryCreatedAt;


}
