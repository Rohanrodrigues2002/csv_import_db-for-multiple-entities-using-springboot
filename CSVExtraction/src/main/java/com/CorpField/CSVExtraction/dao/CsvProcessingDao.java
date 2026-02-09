package com.CorpField.CSVExtraction.dao;

import com.CorpField.CSVExtraction.entity.Brand;
import com.CorpField.CSVExtraction.entity.Category;
import com.CorpField.CSVExtraction.entity.Flavour;
import com.CorpField.CSVExtraction.query.CsvQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CsvProcessingDao {

    @PersistenceContext
    private EntityManager entityManager;

    public int checkCategoryDuplicate(String categoryName) {
        Query query = entityManager.createNativeQuery(CsvQuery.CHECK_CATEGORY_EXIST)
                .setParameter("categoryName", categoryName);
        return convertObjToInteger(query.getSingleResult());
    }

    public int checkBrandDuplicate(String brandName, String categoryName) {
        Query query = entityManager.createNativeQuery(CsvQuery.CHECK_BRAND_EXIST)
                .setParameter("brandName", brandName)
                .setParameter("categoryName", categoryName);
        return convertObjToInteger(query.getSingleResult());
    }

    public int checkFlavourDuplicate(String flavourName, String brandName) {
        Query query = entityManager.createNativeQuery(CsvQuery.CHECK_FLAVOUR_EXIST)
                .setParameter("flavourName", flavourName)
                .setParameter("brandName", brandName);
        return convertObjToInteger(query.getSingleResult());
    }

    // -------------------- INSERT METHODS --------------------
    @Transactional
    public Category insertCategory(Category category) {
        Query query = entityManager.createNativeQuery(CsvQuery.INSERT_CATEGORY)
                .setParameter("name", category.getName())
                .setParameter("description", category.getDescription())
                .setParameter("isActive", category.isActive())
                .setParameter("isDeleted", category.isDeleted())
                .setParameter("createdAt", category.getCreatedAt());
        query.executeUpdate();

        return category;
    }

    @Transactional
    public Brand insertBrand(Brand brand) {
        Query query = entityManager.createNativeQuery(CsvQuery.INSERT_BRAND)
                .setParameter("brand_id", brand.getId())
                .setParameter("brand_name", brand.getBrandName())
                .setParameter("description", brand.getDescription())
                .setParameter("is_deleted", brand.isDeleted())
                .setParameter("active", brand.isActive())
                .setParameter("category_id", brand.getCategory().getId());
        query.executeUpdate();
        return brand;
    }


    @Transactional
    public void insertFlavour(Flavour flavour, Long brandId) {
        Query query = entityManager.createNativeQuery(CsvQuery.INSERT_FLAVOUR)
                .setParameter("flavourName", flavour.getFlavourName())
                .setParameter("description", flavour.getDescription())
                .setParameter("brandId", brandId)
                .setParameter("isActive", flavour.isActive())
                .setParameter("isDeleted", flavour.isDeleted())
                .setParameter("createdAt", flavour.getCreatedAt())
                .setParameter("updatedAt", flavour.getUpdatedAt());
        query.executeUpdate();
    }

    private int convertObjToInteger(Object obj) {
        if (obj instanceof Number) return ((Number) obj).intValue();
        return 0;
    }
}
