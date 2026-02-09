package com.CorpField.CSVExtraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CsvRowResponseDTO {
    private String flavourName;
    private String brandName;
    private String categoryName;
    private boolean success;
    private String message;

    private HttpStatus status;
    private List<CsvRowResponseDTO> data;

    public CsvRowResponseDTO(HttpStatus status, String message, List<CsvRowResponseDTO> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
