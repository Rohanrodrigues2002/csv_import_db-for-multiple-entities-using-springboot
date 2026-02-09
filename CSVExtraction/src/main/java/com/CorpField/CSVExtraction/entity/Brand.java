package com.CorpField.CSVExtraction.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class Brand {
    private Long id;
    private String brandName;
    private String description;
    private boolean active = true;
    private boolean deleted = false;
    private Category category;
    private List<Flavour> flavours;


}
