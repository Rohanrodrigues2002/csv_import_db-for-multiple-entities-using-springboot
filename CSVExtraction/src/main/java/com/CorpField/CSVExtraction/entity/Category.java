package com.CorpField.CSVExtraction.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class Category {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private boolean isDeleted = false;
    private LocalDateTime createdAt;
    private List<Brand> brands;

}
