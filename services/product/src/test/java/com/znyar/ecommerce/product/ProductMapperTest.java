package com.znyar.ecommerce.product;

import com.znyar.ecommerce.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    ProductMapper mapper;
    Product product;
    Category category;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
        product = new Product();
        category = new Category(
                1,
                "category name",
                "category description",
                List.of(product)
        );
        product.setId(1);
        product.setName("product name");
        product.setDescription("product description");
        product.setPrice(BigDecimal.TEN);
        product.setCategory(category);
    }

    @Test
    void toProduct() {
        ProductRequest request = new ProductRequest(
                1,
                "product name",
                "product description",
                2,
                BigDecimal.TEN,
                1
        );
        Product product  = mapper.toProduct(request);
        assertEquals(product.getId(), request.id());
        assertEquals(product.getName(), request.name());
        assertEquals(product.getDescription(), request.description());
        assertEquals(product.getPrice(), request.price());
        assertEquals(product.getCategory().getId(), request.categoryId());

    }

    @Test
    void toProductResponse() {
        ProductResponse response = mapper.toProductResponse(product);
        assertEquals(product.getId(), response.id());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getPrice(), response.price());
        assertEquals(product.getCategory().getId(), response.categoryId());
        assertEquals(product.getCategory().getName(), response.categoryName());
        assertEquals(product.getCategory().getDescription(), response.categoryDescription());
    }

    @Test
    void toProductPurchaseResponse() {
        ProductPurchaseResponse response = mapper.toProductPurchaseResponse(product, 2);
        assertEquals(product.getId(), response.productId());
        assertEquals(product.getName(), response.name());
        assertEquals(product.getDescription(), response.description());
        assertEquals(product.getPrice(), response.price());
        assertEquals(2, response.quantity());
    }

}