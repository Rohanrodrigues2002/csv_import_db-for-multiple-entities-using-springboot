package com.CorpField.CSVExtraction.service;

import com.CorpField.CSVExtraction.dto.CsvRowRequestDTO;
import com.CorpField.CSVExtraction.dto.CsvRowResponseDTO;

import java.util.List;

public interface CsvProcessingService {
    CsvRowResponseDTO insertCsvRows(List<CsvRowRequestDTO> reqDtoList);
}
