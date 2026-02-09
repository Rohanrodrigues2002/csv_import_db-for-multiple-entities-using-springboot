package com.CorpField.CSVExtraction.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flavour {
    private Long id;
    private String flavourName;
    private String description;
    private boolean active = true;
    private boolean deleted = false;
    private String createdAt;
    private String updatedAt;
    private Brand brand;

    // getters and setters
}
