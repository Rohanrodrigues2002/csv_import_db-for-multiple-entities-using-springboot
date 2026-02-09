package com.CorpField.CSVExtraction.controller;

import com.CorpField.CSVExtraction.dto.CsvRowRequestDTO;
import com.CorpField.CSVExtraction.dto.CsvRowResponseDTO;
import com.CorpField.CSVExtraction.service.CsvProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvProcessingService csvProcessingService;

    @PostMapping("/insertRows")
    public ResponseEntity<CsvRowResponseDTO> insertCsvRows(@RequestBody List<CsvRowRequestDTO> reqDtoList) {
        // Call service
        CsvRowResponseDTO responseDto = csvProcessingService.insertCsvRows(reqDtoList);

        // Return response directly
        return ResponseEntity.ok(responseDto);
    }
}
