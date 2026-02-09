package com.CorpField.CSVExtraction.facade;

import com.CorpField.CSVExtraction.dao.CsvProcessingDao;

import com.CorpField.CSVExtraction.dto.CsvRowRequestDTO;
import com.CorpField.CSVExtraction.entity.Brand;
import com.CorpField.CSVExtraction.entity.Category;

import com.CorpField.CSVExtraction.entity.Flavour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CsvProcessingFacade {

    @Autowired
    private CsvProcessingDao csvProcessingDao;



    public Map<String, String[]> validateAndPrepareCsv(String csvFilePath) {
        Map<String, String[]> errorMessages = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\CSV_Extraction\\excelforextraction.csv"))) {
            String line;
            int rowNum = 0;

            while ((line = br.readLine()) != null) {
                rowNum++;
                if (rowNum == 1) continue;

                String[] values = line.split("\t");

                Long categoryId = Long.parseLong(values[15].trim());
                String categoryName = values[19].trim();
                String categoryDescription = values[16].trim();
                boolean categoryActive = values[17].trim().equals("1");
                boolean categoryDeleted = values[18].trim().equals("1");
                String categoryCreatedAt = values[15].trim(); // adjust format if needed

                Long brandId = Long.parseLong(values[0].trim());
                String brandName = values[2].trim();
                String brandDescription = values[4].trim();
                boolean brandActive = values[1].trim().equals("1");
                boolean brandDeleted = values[3].trim().equals("1");
                Long brandCategoryId = Long.parseLong(values[5].trim());

                Long flavourId = Long.parseLong(values[6].trim());
                String flavourName = values[10].trim();
                String flavourDescription = values[9].trim();
                String flavourCreatedAt = values[7].trim();
                String flavourUpdatedAt = values[11].trim();
                boolean flavourActive = values[13].trim().equals("1");
                boolean flavourDeleted = values[8].trim().equals("1");
                Long flavourBrandId = Long.parseLong(values[12].trim());

                // ---------- VALIDATIONS ----------
                if (csvProcessingDao.checkCategoryDuplicate(categoryName) > 0) {
                    errorMessages.put("category_" + rowNum,
                            new String[]{"Category '" + categoryName + "' already exists."});
                    continue;
                }

                if (csvProcessingDao.checkBrandDuplicate(brandName, String.valueOf(categoryId)) > 0) {
                    errorMessages.put("brand_" + rowNum,
                            new String[]{"Brand '" + brandName + "' already exists in category '" + categoryName + "'."});
                    continue;
                }

                if (csvProcessingDao.checkFlavourDuplicate(flavourName, String.valueOf(brandId)) > 0) {
                    errorMessages.put("flavour_" + rowNum,
                            new String[]{"Flavour '" + flavourName + "' already exists for brand '" + brandName + "'."});
                    continue;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
                LocalDateTime categoryCreatedDate = LocalDateTime.parse(categoryCreatedAt, formatter);



                // ---------- PREPARE ENTITIES ----------
                Category categoryEntity = new Category();
                categoryEntity.setId(categoryId);
                categoryEntity.setName(categoryName);
                categoryEntity.setDescription(categoryDescription);
                categoryEntity.setActive(categoryActive);
                categoryEntity.setDeleted(categoryDeleted);
                categoryEntity.setCreatedAt(categoryCreatedDate); // use parsed LocalDateTime

                Brand brandEntity = new Brand();
                brandEntity.setId(brandId);
                brandEntity.setBrandName(brandName);
                brandEntity.setDescription(brandDescription);
                brandEntity.setActive(brandActive);
                brandEntity.setDeleted(brandDeleted);
                brandEntity.setCategory(categoryEntity); // link to category

// Parse flavour dates
                LocalDateTime flavourCreatedDate = LocalDateTime.parse(flavourCreatedAt, formatter);
                LocalDateTime flavourUpdatedDate = LocalDateTime.parse(flavourUpdatedAt, formatter);

                Flavour flavourEntity = new Flavour();
                flavourEntity.setId(flavourId);
                flavourEntity.setFlavourName(flavourName);
                flavourEntity.setDescription(flavourDescription);
                flavourEntity.setActive(flavourActive);
                flavourEntity.setDeleted(flavourDeleted);
                flavourEntity.setCreatedAt(String.valueOf(flavourCreatedDate));
                flavourEntity.setUpdatedAt(String.valueOf(flavourUpdatedDate));
                flavourEntity.setBrand(brandEntity); // link to brand

            }

        } catch (Exception e) {
            errorMessages.put("file_read_error", new String[]{e.getMessage()});
        }

        return errorMessages.isEmpty() ? null : errorMessages;
    }
    public void insertCsvRow(CsvRowRequestDTO reqDto) {

        try {
            // --- 1. Prepare DateTimeFormatter for parsing CSV dates ---
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

            // --- 2. Prepare CategoryEntity ---
            Category categoryEntity = new Category();
            categoryEntity.setName(reqDto.getCategoryName());
            categoryEntity.setDescription(reqDto.getCategoryDescription());
            categoryEntity.setActive(reqDto.isCategoryActive());
            categoryEntity.setDeleted(reqDto.isCategoryDeleted());

            if (reqDto.getCategoryCreatedAt() != null && !reqDto.getCategoryCreatedAt().isEmpty()) {
                categoryEntity.setCreatedAt(LocalDateTime.parse(reqDto.getCategoryCreatedAt(), formatter));
            }

            // --- 3. Insert Category using DAO ---
            Category savedCategory = csvProcessingDao.insertCategory(categoryEntity);

            // --- 4. Prepare BrandEntity ---
            Brand brandEntity = new Brand();
            brandEntity.setBrandName(reqDto.getBrandName());
            brandEntity.setDescription(reqDto.getBrandDescription());
            brandEntity.setActive(reqDto.isBrandActive());
            brandEntity.setDeleted(reqDto.isBrandDeleted());
            brandEntity.setCategory(savedCategory); // link to category

            // --- 5. Insert Brand using DAO ---
            Brand savedBrand = csvProcessingDao.insertBrand(brandEntity);

            // --- 6. Prepare FlavourEntity ---
            Flavour flavourEntity = new Flavour();
            flavourEntity.setFlavourName(reqDto.getFlavourName());
            flavourEntity.setDescription(reqDto.getFlavourDescription());
            flavourEntity.setActive(reqDto.isFlavourActive());
            flavourEntity.setDeleted(reqDto.isFlavourDeleted());
            flavourEntity.setBrand(savedBrand); // link to brand

            if (reqDto.getFlavourCreatedAt() != null && !reqDto.getFlavourCreatedAt().isEmpty()) {
                flavourEntity.setCreatedAt(reqDto.getFlavourCreatedAt());
            }

            if (reqDto.getFlavourUpdatedAt() != null && !reqDto.getFlavourUpdatedAt().isEmpty()) {
                flavourEntity.setUpdatedAt(reqDto.getFlavourUpdatedAt());
            }

            // --- 7. Insert Flavour using DAO ---
            csvProcessingDao.insertFlavour(flavourEntity, savedBrand.getId());


        } catch (Exception e) {
            // handle or log exception according to company standards
            throw new RuntimeException("Error inserting CSV row", e);
        }
    }


}
