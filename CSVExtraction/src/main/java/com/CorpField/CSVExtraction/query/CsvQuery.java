package com.CorpField.CSVExtraction.query;

import org.springframework.stereotype.Component;

@Component
public class CsvQuery {

    public static final String CHECK_CATEGORY_EXIST = 
        "SELECT COUNT(*) FROM category c WHERE c.name = :categoryName";

    public static final String CHECK_BRAND_EXIST = 
        "SELECT COUNT(*) FROM brand b " +
        "JOIN category c ON b.category_id = c.id " +
        "WHERE b.brand_name = :brandName AND c.name = :categoryName";

    public static final String CHECK_FLAVOUR_EXIST = 
        "SELECT COUNT(*) FROM flavour f " +
        "JOIN brand b ON f.brand_id = b.brand_id " +
        "WHERE f.flavour_name = :flavourName AND b.brand_name = :brandName";

    public static final String INSERT_CATEGORY =
            "INSERT INTO category(name, description, is_active, is_deleted, created_at) " +
                    "VALUES(:name, :description, :isActive, :isDeleted, :createdAt)";

    public static final String INSERT_BRAND =
            "INSERT INTO brand(brand_name, description, is_deleted, active, category_id) " +
                    "VALUES(:brandName, :description, :isDeleted, :active, :categoryId)";

    public static final String INSERT_FLAVOUR =
            "INSERT INTO flavour(flavour_name, description, brand_id, is_active, is_deleted, created_at, updated_at) " +
                    "VALUES(:flavourName, :description, :brandId, :isActive, :isDeleted, :createdAt, :updatedAt)";
}

