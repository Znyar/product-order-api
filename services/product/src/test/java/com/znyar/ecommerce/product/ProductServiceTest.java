package com.znyar.ecommerce.product;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct() {
        //given
        ProductRequest request = new ProductRequest(
                1,
                "product name",
                "product description",
                2,
                BigDecimal.TEN,
                1
        );
        Product product = new Product();
        product.setId(1);
        product.setName("product name");
        product.setDescription("product description");
        product.setPrice(BigDecimal.TEN);
        //when
        when(mapper.toProduct(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        Integer productId = productService.createProduct(request);
        //then
        assertEquals(1, productId);
        verify(mapper, times(1)).toProduct(request);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void purchaseProducts() {
    }

    @Test
    void findById() {
        //given
        Product product = new Product();
        product.setId(1);
        product.setName("product name");
        product.setDescription("product description");
        product.setPrice(BigDecimal.TEN);
        ProductResponse response  = new ProductResponse(
                1,
                "product name",
                "product description",
                1,
                BigDecimal.TEN,
                1,
                "category name",
                "category description");
        //when
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(mapper.toProductResponse(product)).thenReturn(response);
        ProductResponse newResponse = productService.findById(1);
        //then
        assertEquals(response, newResponse);
        verify(productRepository, times(1)).findById(1);
        verify(mapper, times(1)).toProductResponse(product);
    }

    @Test
    void findByIdReturnsEntityNotFoundException() {
        //when
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productService.findById(1));
        assertEquals("Product not found with the ID:: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1);
        verify(mapper, times(0)).toProductResponse(any());
    }

    @Test
    void findAll() {

    }

}