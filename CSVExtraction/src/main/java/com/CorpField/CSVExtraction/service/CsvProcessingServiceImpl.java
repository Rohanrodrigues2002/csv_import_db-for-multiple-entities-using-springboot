package com.CorpField.CSVExtraction.service;

import com.CorpField.CSVExtraction.dto.CsvRowRequestDTO;
import com.CorpField.CSVExtraction.dto.CsvRowResponseDTO;
import com.CorpField.CSVExtraction.facade.CsvProcessingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CsvProcessingServiceImpl implements CsvProcessingService {

    @Autowired
    private CsvProcessingFacade csvProcessingFacade;

    @Override
    public CsvRowResponseDTO insertCsvRows(List<CsvRowRequestDTO> reqDtoList) {
        List<CsvRowResponseDTO> responseList = new ArrayList<>();

        try {
            for (CsvRowRequestDTO reqDto : reqDtoList) {
                // Validate single CSV row
                Map<String, String[]> msg = csvProcessingFacade.validateAndPrepareCsv(reqDto.toString());

                if (msg != null && !msg.isEmpty()) {
                    // Add failure response
                    responseList.add(new CsvRowResponseDTO(
                            reqDto.getFlavourName(),
                            reqDto.getBrandName(),
                            reqDto.getCategoryName(),
                            false,
                            "Validation Failed",
                            null,
                            null
                    ));
                    continue;
                }

                // Insert row into DB
                csvProcessingFacade.insertCsvRow(reqDto);

                // Add success response
                responseList.add(new CsvRowResponseDTO(
                        reqDto.getFlavourName(),
                        reqDto.getBrandName(),
                        reqDto.getCategoryName(),
                        true,
                        "Inserted Successfully",
                        null,
                        null
                ));
            }

            // Return overall response
            return new CsvRowResponseDTO(HttpStatus.OK, "CSV rows processed", responseList);

        } catch (Exception e) {
            // Return error response
            return new CsvRowResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing CSV: " + e.getMessage(),
                    null);
        }
    }
}
